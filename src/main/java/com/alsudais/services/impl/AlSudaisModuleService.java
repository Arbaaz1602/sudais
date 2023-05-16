package com.alsudais.services.impl;


import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.beans.request.ModuleRequestBean;
import com.alsudais.beans.response.AuditResponseBean;
import com.alsudais.beans.response.PageResponseBean;
import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisModuleDetail;
import com.alsudais.entities.AlSudaisPlatformDetail;
import com.alsudais.enums.ModuleStatusEnum;
import com.alsudais.enums.UuidPrefixEnum;
import com.alsudais.exceptions.AlSudaisAlreadyExistException;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.mappers.entity.pojo.IAlSudaisModuleEntityPojoMapper;
import com.alsudais.mappers.entity.pojo.IAlSudaisPlatformEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisModulePojoEntityMapper;
import com.alsudais.repositories.IAlSudaisModuleDetailRepository;
import com.alsudais.services.IAlSudaisModuleService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.data.relational.core.query.Query.query;

@Service
@Transactional
public class AlSudaisModuleService implements IAlSudaisModuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlSudaisModuleService.class);
    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;
    @Autowired
    private IAlSudaisModuleDetailRepository alSudaisModuleDetailRepository;
    @Autowired
    private IAlSudaisModulePojoEntityMapper alSudaisModulePojoEntityMapper;
    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;
    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;
    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<?> addModule(ModuleRequestBean moduleRequestBean) {
        return this.alSudaisModuleDetailRepository.findByAmdModuleNameIgnoreCase(moduleRequestBean.getModuleName())
                .log()
                .flatMap(alSudaisModuleDetail -> {
                    LOGGER.info("AlSudaisModuleDetail :: {} ", alSudaisModuleDetail);
                    return Mono.error(new AlSudaisAlreadyExistException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.MODULE_ALREADY_EXIST)));
                })
                .cast(ModuleRequestBean.class)
                .switchIfEmpty(Mono.defer(() -> {
                    String moduleDetailId = AlSudaisCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.MODULE.getValue());
                    LOGGER.info("ModuleDetailId :: {} ", moduleDetailId);

                    moduleRequestBean.setModuleId(moduleDetailId);
                    moduleRequestBean.setCreatedBy(alSudaisLocaleResolverConfig.getIdentity());

                    return Mono.just(moduleRequestBean);
                }))
                .map(this.alSudaisModulePojoEntityMapper.INSTANCE::modulePojoToModuleEntity)
                .flatMap(this.alSudaisModuleDetailRepository::save)
                .map(IAlSudaisModuleEntityPojoMapper.mapModuleEntityPojoMapping)
                .map(moduleResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(moduleResponseBean).build());
    }

    @Override
    public Mono<?> fetchModuleByModuleId(String moduleId) {
        return this.alSudaisModuleDetailRepository.findByAmdModuleId(moduleId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisModuleEntityPojoMapper.mapModuleEntityPojoMapping)
                .map(moduleResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(moduleResponseBean).build());
    }

    @Override
    public Mono<?> fetchAllModule(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        Criteria criteria = Criteria.empty();
        if(StringUtils.isNoneEmpty(filterCriteria)) {
            String entityFilterCriteria = AlSudaisReactiveSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "amd");
            LOGGER.info("EntityFilterCriteria :: {}", entityFilterCriteria);

            criteria = AlSudaisReactiveSpecificationUtils.INSTANCE.buildCriteria(entityFilterCriteria, new ArrayList<>());
            LOGGER.info("Criteria :: {}", criteria);
        }

        Sort sort = Sort.by("amdSeqId");
        if(StringUtils.isNoneEmpty(sortCriteria)){
            sort = AlSudaisReactiveSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "amd");
        }
        return this.r2dbcEntityTemplate.select(AlSudaisModuleDetail.class)
                .matching(query(criteria).with(PageRequest.of(page, size)).sort(sort))
                .all()
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisModuleEntityPojoMapper.mapModuleEntityPojoMapping)
                .collectList()
                .zipWith(this.alSudaisModuleDetailRepository.count())
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
    public Mono<?> updateModule(ModuleRequestBean moduleRequestBean) {
        return this.alSudaisModuleDetailRepository.findByAmdModuleId(moduleRequestBean.getModuleId())
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .flatMap(alSudaisModuleDetail -> {
                    LOGGER.info("AlSudaisModuleDetail :: {} ", alSudaisModuleDetail);
                    moduleRequestBean.setModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    return this.alSudaisModuleDetailRepository.save(this.alSudaisModulePojoEntityMapper.INSTANCE.updateModule.apply(moduleRequestBean, alSudaisModuleDetail));
                })
                .map(alSudaisModuleDetail -> ServiceResponseBean.builder().status(Boolean.TRUE).data(IAlSudaisModuleEntityPojoMapper.mapModuleEntityPojoMapping.apply(alSudaisModuleDetail)).build());
    }

    @Override
    public Mono<?> deleteModule(String moduleId) {
        return this.alSudaisModuleDetailRepository.findByAmdModuleId(moduleId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(alSudaisModuleDetail -> {
                    alSudaisModuleDetail.setAmdStatus(ModuleStatusEnum.DELETED.getValue());
                    alSudaisModuleDetail.setAmdModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    alSudaisModuleDetail.setAmdModifiedDate(LocalDateTime.now());
                    return alSudaisModuleDetail;
                })
                .flatMap(this.alSudaisModuleDetailRepository::save)
                .map(alSudaisModuleDetail -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_DELETED)).build());
    }

    @Override
    public Mono<?> fetchHistoricalData(String moduleId) {
        ReactiveAuditQuery annotation = AlSudaisModuleDetail.class.getAnnotation(ReactiveAuditQuery.class);
        LOGGER.info("Query :: {}", annotation.query());

        return this.databaseClient.sql(annotation.query())
                .bind("module_id", moduleId)
                .map(IAlSudaisModuleEntityPojoMapper.mapModuleDetailHistoricalData)
                .all()
                .collectList()
                .filter(list -> !list.isEmpty())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(list -> ServiceResponseBean.builder()
                        .status(Boolean.TRUE)
                        .data(AuditResponseBean.builder().id(moduleId).payload(list).build())
                        .build());
    }
}