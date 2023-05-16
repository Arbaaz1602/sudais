package com.alsudais.services.impl;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.beans.request.PlatformModuleRequestBean;
import com.alsudais.beans.response.AuditResponseBean;
import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisPlatformModuleMapping;
import com.alsudais.enums.PlatformModuleStatusEnum;
import com.alsudais.exceptions.AlSudaisAlreadyExistException;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.mappers.entity.pojo.IAlSudaisPlatformModuleEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisPlatformModulePojoEntityMapper;
import com.alsudais.repositories.IAlSudaisPlatformModuleMappingRepository;
import com.alsudais.services.IAlSudaisPlatformModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlSudaisPlatformModuleService implements IAlSudaisPlatformModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlSudaisModuleService.class);

    @Autowired
    private IAlSudaisPlatformModuleMappingRepository alSudaisPlatformModuleMappingRepository;

    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<?> addPlatformModule(PlatformModuleRequestBean platformModuleRequestBean) {
        return this.alSudaisPlatformModuleMappingRepository.findByApmmPlatformIdAndApmmModuleIdIn(platformModuleRequestBean.getPlatformId(), platformModuleRequestBean.getModuleIds())
                .switchIfEmpty(Flux.just(AlSudaisPlatformModuleMapping.builder().build()))
                .collectList()
                .map(data -> {
                    if (data.size() != BigInteger.ZERO.intValue())
                        platformModuleRequestBean.getModuleIds().removeAll(data.parallelStream().map(AlSudaisPlatformModuleMapping::getApmmModuleId).collect(Collectors.toSet()));

                    platformModuleRequestBean.setStatus(PlatformModuleStatusEnum.ACTIVE.getValue());
                    platformModuleRequestBean.setCreatedBy(this.alSudaisLocaleResolverConfig.getIdentity());

                    return platformModuleRequestBean;
                })
                .filter(platformModuleRequestBeanInner -> platformModuleRequestBeanInner.getModuleIds().size() > BigInteger.ZERO.intValue())
                .switchIfEmpty(Mono.error(new AlSudaisAlreadyExistException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.PLATFORM_MODULE_ALREADY_EXIST))))
                .map(IAlSudaisPlatformModulePojoEntityMapper.addPlatformModuleMapping)
                .flatMapMany(this.alSudaisPlatformModuleMappingRepository::saveAll)
                .count()
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.PLATFORM_MODULE_ADD_SUCCESS)).build());
    }

    @Override
    public Mono<?> fetchPlatformModuleById(String platformId) {
        return this.alSudaisPlatformModuleMappingRepository.findByApmmPlatformId(platformId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .collectList()
                .map(IAlSudaisPlatformModuleEntityPojoMapper.mapPlatformModuleEntityPojoMapping)
                .map(platformModuleResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(platformModuleResponseBean).build());
    }

    @Override
    public Mono<?> updatePlatformModule(PlatformModuleRequestBean platformModuleRequestBean) {
        return this.alSudaisPlatformModuleMappingRepository.findByApmmPlatformId(platformModuleRequestBean.getPlatformId())
                .switchIfEmpty(Flux.just(AlSudaisPlatformModuleMapping.builder().build()))
                .collectList()
                .flatMap(data -> {
                    platformModuleRequestBean.setStatus(PlatformModuleStatusEnum.ACTIVE.getValue());
                    platformModuleRequestBean.setCreatedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    platformModuleRequestBean.setModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    if (data.size() == BigInteger.ONE.intValue() && data.get(BigInteger.ZERO.intValue()).getApmmSeqId() == null)
                        return Mono.zip(Mono.just(platformModuleRequestBean), Mono.just(AlSudaisCommonConstants.INSTANCE.BLANK_STRING));

                    Set<String> newModuleIdsSet = new HashSet(platformModuleRequestBean.getModuleIds());
                    Set<String> existingModuleIdsSet = data.parallelStream().map(AlSudaisPlatformModuleMapping::getApmmModuleId).collect(Collectors.toSet());

                    platformModuleRequestBean.getModuleIds().removeAll(existingModuleIdsSet);
                    existingModuleIdsSet.removeAll(newModuleIdsSet);

                    return Mono.zip(Mono.just(platformModuleRequestBean), Mono.just(existingModuleIdsSet));
                })
                .flatMap(tuple -> tuple.getT2() instanceof Set ? this.alSudaisPlatformModuleMappingRepository.deleteByApmmPlatformIdAndApmmModuleIdIn(tuple.getT1().getPlatformId(), (Set<String>) tuple.getT2()).thenReturn(tuple.getT1()) : Mono.just(tuple.getT1()))
                .filter(platformModuleRequestBeanInner -> platformModuleRequestBeanInner.getModuleIds().size() > BigInteger.ZERO.intValue())
                .switchIfEmpty(Mono.error(new AlSudaisAlreadyExistException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.PLATFORM_MODULE_ALREADY_EXIST))))
                .map(IAlSudaisPlatformModulePojoEntityMapper.updatePlatformModuleMapping)
                .flatMapMany(this.alSudaisPlatformModuleMappingRepository::saveAll)
                .count()
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.PLATFORM_MODULE_UPDATE_SUCCESS)).build());
    }

    @Override
    public Mono<?> deletePlatformModuleById(String platformId) {
        return this.alSudaisPlatformModuleMappingRepository.deleteByApmmPlatformId(platformId)
                .filter(count -> count != BigInteger.ZERO.longValue())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_DELETED)).build());
    }

    @Override
    public Mono<?> fetchHistoricalData(String platformId) {
        ReactiveAuditQuery annotation = AlSudaisPlatformModuleMapping.class.getAnnotation(ReactiveAuditQuery.class);
        LOGGER.info("Query :: {}", annotation.query());

        return this.databaseClient.sql(annotation.query())
                .bind("platform_id", platformId)
                .map(IAlSudaisPlatformModuleEntityPojoMapper.mapPlatformModuleHistoricalData)
                .all()
                .collectList()
                .filter(list -> !list.isEmpty())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(list -> ServiceResponseBean.builder()
                        .status(Boolean.TRUE)
                        .data(AuditResponseBean.builder().id(platformId).payload(list).build())
                        .build());
    }
}
