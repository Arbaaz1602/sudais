package com.alsudais.services.impl;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.beans.CompanyShortInfoBean;
import com.alsudais.beans.request.UserMasterRequestBean;
import com.alsudais.beans.response.*;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisUserMaster;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.feign.clients.IAlSudaisContractEntitiesReactiveFeignClient;
import com.alsudais.mappers.entity.pojo.IAlSudaisUserMasterEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisUserMasterPojoEntityMapper;
import com.alsudais.repositories.IAlSudaisUserMasterRepository;
import com.alsudais.services.IAlSudaisUserRoleModuleService;
import com.alsudais.services.IAlSudaisUserRoleService;
import com.alsudais.services.IAlSudaisUserService;
import com.alsudais.utils.AlSudaisReactiveSpecificationUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.springframework.data.relational.core.query.Query.query;

@Service
@Transactional
public class AlSudaisUserService implements IAlSudaisUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAlSudaisUserMasterRepository alSudaisUserMasterRepository;

    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @Autowired
    private IAlSudaisUserRoleService alSudaisUserRoleService;

    @Autowired
    private IAlSudaisUserRoleModuleService alSudaisUserRoleModuleService;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private IAlSudaisContractEntitiesReactiveFeignClient alSudaisContractEntitiesReactiveFeignClient;

    @Override
    public Mono<?> fetchUserById(String userId) {
        return this.alSudaisUserMasterRepository.findByAumUserId(userId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisUserMasterEntityPojoMapper.INSTANCE::mapUserMasterEntityPojoMapping)
                .flatMap(userMasterResponseBean -> this.alSudaisUserRoleModuleService.fetchUserRoleModuleById(userMasterResponseBean.getUserId(), null)
                        .cast(ServiceResponseBean.class)
                        .onErrorReturn(ServiceResponseBean.builder().build())
                        .filter(ServiceResponseBean::getStatus)
                        .map(ServiceResponseBean::getData)
                        .cast(UserRoleModuleResponseBean.class)
                        .map(userRoleModuleResponseBeanInner -> {
                            userMasterResponseBean.setUserRoleModuleSetResponseBeans(userRoleModuleResponseBeanInner.getUserRoleModuleSetResponseBeans());
                            return userMasterResponseBean;
                        }).thenReturn(userMasterResponseBean)
                )
                .flatMap(userMasterResponseBean ->
                        this.alSudaisUserRoleService.fetchUserRoleById(userMasterResponseBean.getUserId())
                                .cast(ServiceResponseBean.class)
                                .onErrorReturn(ServiceResponseBean.builder().build())
                                .filter(ServiceResponseBean::getStatus)
                                .map(ServiceResponseBean::getData)
                                .cast(UserRoleResponseBean.class)
                                .map(userRoleResponseBeanInner -> {
                                    userMasterResponseBean.setRoleIds(userRoleResponseBeanInner.getRoleIds());
                                    return userMasterResponseBean;
                                }).thenReturn(userMasterResponseBean)
                )
                .map(userMasterResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(userMasterResponseBean).build());
    }

    @Override
    public Mono<?> fetchAll(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        Criteria criteria = Criteria.empty();
        if (StringUtils.isNoneEmpty(filterCriteria)) {
            String entityFilterCriteria = AlSudaisReactiveSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "aum");
            LOGGER.info("EntityFilterCriteria :: {}", entityFilterCriteria);

            criteria = AlSudaisReactiveSpecificationUtils.INSTANCE.buildCriteria(entityFilterCriteria, new ArrayList<>());
            LOGGER.info("Criteria :: {}", criteria);
        }

        Sort sort = Sort.by("aumSeqId");
        if (StringUtils.isNoneEmpty(sortCriteria)) {
            sort = AlSudaisReactiveSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "aum");
        }

        AtomicReference<Map<String, Set<String>>> roleMapAtomicReference = new AtomicReference<>();
        AtomicReference<Map<Long, CompanyShortInfoBean>> companyShortInfoMapAtomicReference = new AtomicReference<>();
        return this.r2dbcEntityTemplate.select(AlSudaisUserMaster.class)
                .matching(query(criteria).with(PageRequest.of(page, size)).sort(sort))
                .all()
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisUserMasterEntityPojoMapper.INSTANCE::mapUserMasterEntityPojoMapping)
                .collectList()
                .map(userMasterResponseBeans -> Tuples.of(userMasterResponseBeans, userMasterResponseBeans.stream().map(UserMasterResponseBean::getUserId).collect(Collectors.joining(AlSudaisCommonConstants.INSTANCE.CARET_STRING))))
                .flatMap(tuple -> this.alSudaisUserRoleService.fetchAllUserRole("`UserId<IN>".concat(tuple.getT2()), null, BigInteger.ZERO.intValue(), Integer.MAX_VALUE)
                        .cast(ServiceResponseBean.class)
                        .onErrorReturn(ServiceResponseBean.builder().build())
                        .filter(ServiceResponseBean::getStatus)
                        .map(serviceResponseBean -> (List<UserRoleResponseBean>) serviceResponseBean.getData())
                        .flatMapMany(Flux::fromIterable)
                        .collectMap(UserRoleResponseBean::getUserId, UserRoleResponseBean::getRoleIds)
                        .doOnSuccess(roleMapAtomicReference::set)
                        .thenReturn(tuple)
                )
                .flatMap(tuple -> {
                    Set<Long> companyIds = tuple.getT1().parallelStream()
                            .map(UserMasterResponseBean::getUserCode)
                            .filter(userCode -> userCode != null && !userCode.isEmpty())
                            .map(userCode -> userCode.split(AlSudaisCommonConstants.INSTANCE.UNDERSCORE_STRING)[BigInteger.ZERO.intValue()])
                            .map(companyIdString -> {
                                try {
                                    return Long.parseLong(companyIdString);
                                } catch (NumberFormatException e) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

                    if (!companyIds.isEmpty())
                        return this.alSudaisContractEntitiesReactiveFeignClient.fetchCompaniesById(companyIds)
                                .collectMap(CompanyShortInfoBean::getId, companyShortInforBean -> companyShortInforBean)
                                .doOnSuccess(companyShortInfoMapAtomicReference::set)
                                .thenReturn(tuple.getT1());

                    return Mono.just(tuple.getT1());
                })
                .flatMap(userMasterResponseBeanList -> {
                    Map<String, Set<String>> roleMap = roleMapAtomicReference.get();
                    Map<Long, CompanyShortInfoBean> companyShortInfoMap = companyShortInfoMapAtomicReference.get();

                    for (UserMasterResponseBean userMasterResponseBean : userMasterResponseBeanList) {
                        if (roleMap != null && roleMap.containsKey(userMasterResponseBean.getUserId()))
                            userMasterResponseBean.setRoleIds((Set<String>) roleMap.get(userMasterResponseBean.getUserId()));

                        Optional.ofNullable(userMasterResponseBean.getUserCode())
                                .filter(userCode -> !userCode.isEmpty())
                                .map(userCode -> userCode.split(AlSudaisCommonConstants.INSTANCE.UNDERSCORE_STRING)[BigInteger.ZERO.intValue()])
                                .map(companyIdString -> {
                                    try {
                                        return Long.parseLong(companyIdString);
                                    } catch (NumberFormatException e) {
                                        return null;
                                    }
                                })
                                .ifPresent(companyCode -> userMasterResponseBean.setCompanyShortInfoBean(companyShortInfoMap.get(companyCode)));
                    }
                    return Mono.just(userMasterResponseBeanList);
                })
                .zipWith(this.alSudaisUserMasterRepository.count())
                .map(tuple -> ServiceResponseBean.builder().status(Boolean.TRUE).data(PageResponseBean.builder()
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
    public Mono<?> updateUser(UserMasterRequestBean userMasterRequestBean) {
        return this.alSudaisUserMasterRepository.findByAumUserId(userMasterRequestBean.getUserId())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .flatMap(alSudaisUserMaster -> {
                    userMasterRequestBean.setModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    return this.alSudaisUserMasterRepository.save(IAlSudaisUserMasterPojoEntityMapper.updateUser.apply(userMasterRequestBean, alSudaisUserMaster));
                })
                .map(alSudaisUserMaster -> ServiceResponseBean.builder().status(Boolean.TRUE).data(IAlSudaisUserMasterEntityPojoMapper.INSTANCE.mapUserMasterEntityPojoMapping(alSudaisUserMaster)).build());
    }

    @Override
    public Mono<?> fetchUserByTenantId(String tenantId) {
        return this.alSudaisUserMasterRepository.findByAumUserCodeStartsWithIgnoreCase(tenantId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(IAlSudaisUserMasterEntityPojoMapper.INSTANCE::mapUserMasterEntityPojoMapping)
                .collectList()
                .map(userList -> ServiceResponseBean.builder().status(Boolean.TRUE).data(userList).build());
    }

    @Override
    public Mono<?> fetchHistoricalData(String userId) {
        ReactiveAuditQuery annotation = AlSudaisUserMaster.class.getAnnotation(ReactiveAuditQuery.class);
        LOGGER.info("Query :: {}", annotation.query());

        return this.databaseClient.sql(annotation.query())
                .bind("user_id", userId)
                .map(IAlSudaisUserMasterEntityPojoMapper.mapUserHistoricalData)
                .all()
                .collectList()
                .filter(list -> !list.isEmpty())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(list -> ServiceResponseBean.builder()
                        .status(Boolean.TRUE)
                        .data(AuditResponseBean.builder().id(userId).payload(list).build())
                        .build());
    }
}
