package com.alsudais.services.impl;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.beans.request.PlatformRequestBean;
import com.alsudais.beans.response.AuditResponseBean;
import com.alsudais.beans.response.PageResponseBean;
import com.alsudais.beans.response.PlatformResponseBean;
import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisPlatformDetail;
import com.alsudais.enums.PlatformStatusEnum;
import com.alsudais.enums.UuidPrefixEnum;
import com.alsudais.exceptions.AlSudaisAlreadyExistException;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.mappers.entity.pojo.IAlSudaisPlatformEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisPlatformPojoEntityMapper;
import com.alsudais.repositories.IAlSudaisPlatformRepository;
import com.alsudais.services.IAlSudaisPlatformService;
import com.alsudais.utils.AlSudaisCommonUtils;
import com.alsudais.utils.AlSudaisReactiveSpecificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.data.relational.core.query.Query.query;

@Service
@Transactional
public class AlSudaisPlatformService implements IAlSudaisPlatformService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlSudaisModuleService.class);
    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;
    @Autowired
    private IAlSudaisPlatformRepository alSudaisPlatformRepository;
    @Autowired
    private IAlSudaisPlatformPojoEntityMapper alSudaisPlatformPojoEntityMapper;
    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;
    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<?> addPlatform(PlatformRequestBean platformRequestBean) {
        return this.alSudaisPlatformRepository.findByApdPlatformNameIgnoreCase(platformRequestBean.getPlatformName())
                .log()
                .flatMap(alSudaisPlatform -> {
                    LOGGER.info("AlSudaisPlatformDetail :: {} ", alSudaisPlatform);
                    return Mono.error(new AlSudaisAlreadyExistException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.PLATFORM_ALREADY_EXIST)));
                })
                .cast(PlatformRequestBean.class)
                .switchIfEmpty(Mono.defer(() -> {
                    String platformId = AlSudaisCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.PLATFORM.getValue());
                    LOGGER.info("PlatformId :: {} ", platformId);

                    platformRequestBean.setPlatformId(platformId);
                    platformRequestBean.setCreatedBy(alSudaisLocaleResolverConfig.getIdentity());

                    return Mono.just(platformRequestBean);
                }))
                .map(this.alSudaisPlatformPojoEntityMapper.INSTANCE::platformPojoToRoleEntity)
                .flatMap(this.alSudaisPlatformRepository::save)
                .map(IAlSudaisPlatformEntityPojoMapper.mapPlatformEntityPojoMapping)
                .map(platformResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(platformResponseBean).build());
    }

    @Override
    public Mono<?> fetchPlatformById(String platformId) {
        return this.alSudaisPlatformRepository.findByApdPlatformId(platformId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisPlatformEntityPojoMapper.mapPlatformEntityPojoMapping)
                .map(platformResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(platformResponseBean).build());
    }

    @Override
    public Mono<?> fetchAllPlatform(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        Criteria criteria = Criteria.empty();
        if (StringUtils.isNoneEmpty(filterCriteria)) {
            String entityFilterCriteria = AlSudaisReactiveSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "apd");
            LOGGER.info("EntityFilterCriteria :: {}", entityFilterCriteria);

            criteria = AlSudaisReactiveSpecificationUtils.INSTANCE.buildCriteria(entityFilterCriteria, new ArrayList<>());
            LOGGER.info("Criteria :: {}", criteria);
        }

        Sort sort = Sort.by("apdSeqId");
        if (StringUtils.isNoneEmpty(sortCriteria)) {
            sort = AlSudaisReactiveSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "apd");
        }
        return this.r2dbcEntityTemplate.select(AlSudaisPlatformDetail.class)
                .matching(query(criteria).with(PageRequest.of(page, size)).sort(sort))
                .all()
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisPlatformEntityPojoMapper.mapPlatformEntityPojoMapping)
                .collectList()
                .zipWith(this.alSudaisPlatformRepository.count())
                .map(tuple ->
                        ServiceResponseBean.builder().status(Boolean.TRUE).data(PageResponseBean.builder()
                                        .payload(tuple.getT1())
                                        .numberOfElements(tuple.getT1().size())
                                        .pageSize(size)
                                        .totalElements(tuple.getT2())
                                        .currentPage(page)
                                        .build())
                                .build()
                );

    }

    @Override
    public Mono<?> updatePlatform(PlatformRequestBean platformRequestBean) {
        return this.alSudaisPlatformRepository.findByApdPlatformId(platformRequestBean.getPlatformId())
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .flatMap(alSudaisPlatform -> {
                    LOGGER.info("AlSudaisPlatformDetail :: {} ", alSudaisPlatform);
                    platformRequestBean.setModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    return this.alSudaisPlatformRepository.save(this.alSudaisPlatformPojoEntityMapper.INSTANCE.updatePlatform.apply(platformRequestBean, alSudaisPlatform));
                })
                .map(alSudaisPlatform -> ServiceResponseBean.builder().status(Boolean.TRUE).data(IAlSudaisPlatformEntityPojoMapper.mapPlatformEntityPojoMapping.apply(alSudaisPlatform)).build());
    }

    @Override
    public Mono<?> deletePlatform(String platformId) {
        return this.alSudaisPlatformRepository.findByApdPlatformId(platformId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(alSudaisPlatform -> {
                    alSudaisPlatform.setApdStatus(PlatformStatusEnum.DELETED.getValue());
                    alSudaisPlatform.setApdModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    alSudaisPlatform.setApdModifiedDate(LocalDateTime.now());
                    return alSudaisPlatform;
                })
                .flatMap(this.alSudaisPlatformRepository::save)
                .map(alSudaisPlatform -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_DELETED)).build());
    }

    @Override
    public Mono<?> fetchHistoricalData(String platformId) {
        ReactiveAuditQuery annotation = AlSudaisPlatformDetail.class.getAnnotation(ReactiveAuditQuery.class);
        LOGGER.info("Query :: {}", annotation.query());

        return this.databaseClient.sql(annotation.query())
                .bind("platform_id", platformId)
                .map(IAlSudaisPlatformEntityPojoMapper.mapPlatformHistoricalData)
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

