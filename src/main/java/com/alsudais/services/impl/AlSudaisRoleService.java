package com.alsudais.services.impl;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.beans.request.RoleRequestBean;
import com.alsudais.beans.response.AuditResponseBean;
import com.alsudais.beans.response.PageResponseBean;
import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisPlatformDetail;
import com.alsudais.entities.AlSudaisRoleDetail;
import com.alsudais.enums.LanguageCodeEnum;
import com.alsudais.enums.RoleStatusEnum;
import com.alsudais.enums.UuidPrefixEnum;
import com.alsudais.exceptions.AlSudaisAlreadyExistException;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.mappers.entity.pojo.IAlSudaisPlatformEntityPojoMapper;
import com.alsudais.mappers.entity.pojo.IAlSudaisRoleEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisRolePojoEntityMapper;
import com.alsudais.repositories.IAlSudaisRoleDetailRepository;
import com.alsudais.services.IAlSudaisRoleService;
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
public class AlSudaisRoleService implements IAlSudaisRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlSudaisModuleService.class);
    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;
    @Autowired
    private IAlSudaisRoleDetailRepository alSudaisRoleDetailRepository;
    @Autowired
    private IAlSudaisRolePojoEntityMapper alSudaisRolePojoEntityMapper;
    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<?> addRole(RoleRequestBean roleRequestBean) {
        return this.alSudaisRoleDetailRepository.findByArdRoleNameIgnoreCase(roleRequestBean.getRoleName())
                .log()
                .flatMap(alSudaisRoleDetail -> {
                    LOGGER.info("AlSudaisRoleDetail :: {} ", alSudaisRoleDetail);
                    return Mono.error(new AlSudaisAlreadyExistException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.ROLE_ALREADY_EXIST)));
                })
                .cast(RoleRequestBean.class)
                .switchIfEmpty(Mono.defer(() -> {
                    String roleId = AlSudaisCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.ROLE.getValue());
                    LOGGER.info("RoleId :: {} ", roleId);

                    roleRequestBean.setRoleId(roleId);
                    roleRequestBean.setCreatedBy(alSudaisLocaleResolverConfig.getIdentity());

                    return Mono.just(roleRequestBean);
                }))
                .map(this.alSudaisRolePojoEntityMapper.INSTANCE::rolePojoToRoleEntity)
                .flatMap(this.alSudaisRoleDetailRepository::save)
                .map(IAlSudaisRoleEntityPojoMapper.mapRoleEntityPojoMapping)
                .map(roleResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(roleResponseBean).build());
    }

    @Override
    public Mono<?> fetchRoleByRoleId(String roleId) {
        return this.alSudaisRoleDetailRepository.findByArdRoleId(roleId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisRoleEntityPojoMapper.mapRoleEntityPojoMapping)
                .map(roleResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(roleResponseBean).build());
    }

    @Override
    public Mono<?> fetchAllRole(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        Criteria criteria = Criteria.empty();
        if(StringUtils.isNoneEmpty(filterCriteria)) {
            String entityFilterCriteria = AlSudaisReactiveSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "ard");
            LOGGER.info("EntityFilterCriteria :: {}", entityFilterCriteria);

            criteria = AlSudaisReactiveSpecificationUtils.INSTANCE.buildCriteria(entityFilterCriteria, new ArrayList<>());
            LOGGER.info("Criteria :: {}", criteria);
        }

        Sort sort = Sort.by("ardSeqId");
        if(StringUtils.isNoneEmpty(sortCriteria)){
            sort = AlSudaisReactiveSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "ard");
        }
        return this.r2dbcEntityTemplate.select(AlSudaisRoleDetail.class)
                .matching(query(criteria).with(PageRequest.of(page, size)).sort(sort))
                .all()
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisRoleEntityPojoMapper.mapRoleEntityPojoMapping)
                .collectList()
                .zipWith(this.alSudaisRoleDetailRepository.count())
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
    public Mono<?> updateRole(RoleRequestBean roleRequestBean) {
        return this.alSudaisRoleDetailRepository.findByArdRoleId(roleRequestBean.getRoleId())
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .flatMap(alSudaisRoleDetail -> {
                    LOGGER.info("AlSudaisRoleDetail :: {} ", alSudaisRoleDetail);
                    roleRequestBean.setModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    return this.alSudaisRoleDetailRepository.save(this.alSudaisRolePojoEntityMapper.INSTANCE.updateRole.apply(roleRequestBean, alSudaisRoleDetail));
                })
                .map(alSudaisRoleDetail -> ServiceResponseBean.builder().status(Boolean.TRUE).data(IAlSudaisRoleEntityPojoMapper.mapRoleEntityPojoMapping.apply(alSudaisRoleDetail)).build());
    }

    @Override
    public Mono<?> deleteRole(String roleId) {
        return this.alSudaisRoleDetailRepository.findByArdRoleId(roleId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(alSudaisRoleDetail -> {
                    alSudaisRoleDetail.setArdStatus(RoleStatusEnum.DELETED.getValue());
                    alSudaisRoleDetail.setArdModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    alSudaisRoleDetail.setArdModifiedDate(LocalDateTime.now());
                    return alSudaisRoleDetail;
                })
                .flatMap(this.alSudaisRoleDetailRepository::save)
                .map(alSudaisRoleDetail -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_DELETED)).build());
    }

    @Override
    public Mono<?> fetchHistoricalData(String roleId) {
        ReactiveAuditQuery annotation = AlSudaisRoleDetail.class.getAnnotation(ReactiveAuditQuery.class);
        LOGGER.info("Query :: {}", annotation.query());

        return this.databaseClient.sql(annotation.query())
                .bind("role_id", roleId)
                .map(IAlSudaisRoleEntityPojoMapper.mapRoleHistoricalData)
                .all()
                .collectList()
                .filter(list -> !list.isEmpty())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(list -> ServiceResponseBean.builder()
                        .status(Boolean.TRUE)
                        .data(AuditResponseBean.builder().id(roleId).payload(list).build())
                        .build());
    }
}
