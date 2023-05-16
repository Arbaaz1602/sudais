package com.alsudais.services.impl;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.beans.request.UserRoleRequestBean;
import com.alsudais.beans.response.AuditResponseBean;
import com.alsudais.beans.response.PageResponseBean;
import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisUserRoleMapping;
import com.alsudais.enums.UserAttributesEnum;
import com.alsudais.enums.UserRoleStatusEnum;
import com.alsudais.exceptions.AlSudaisAlreadyExistException;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.mappers.entity.pojo.IAlSudaisUserRoleEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisUserRolePojoEntityMapper;
import com.alsudais.repositories.IAlSudaisUserRoleMappingRepository;
import com.alsudais.services.IAlSudaisAuthenticationService;
import com.alsudais.services.IAlSudaisUserRoleService;
import com.alsudais.utils.AlSudaisReactiveSpecificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.springframework.data.relational.core.query.Query.query;

@Service
@Transactional
public class AlSudaisUserRoleService implements IAlSudaisUserRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlSudaisModuleService.class);

    @Autowired
    private IAlSudaisUserRoleMappingRepository alSudaisUserRoleMappingRepository;

    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @Autowired
    @Lazy
    private IAlSudaisAuthenticationService alSudaisAuthenticationService;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;
    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<?> fetchAllUserRole(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        Criteria criteria = Criteria.empty();
        if (StringUtils.isNoneEmpty(filterCriteria)) {
            String entityFilterCriteria = AlSudaisReactiveSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "aurm");
            LOGGER.info("EntityFilterCriteria :: {}", entityFilterCriteria);

            criteria = AlSudaisReactiveSpecificationUtils.INSTANCE.buildCriteria(entityFilterCriteria, new ArrayList<>());
            LOGGER.info("Criteria :: {}", criteria);
        }

        Sort sort = Sort.by("aurmSeqId");
        if (StringUtils.isNoneEmpty(sortCriteria)) {
            sort = AlSudaisReactiveSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "aurm");
        }

        return this.r2dbcEntityTemplate.select(AlSudaisUserRoleMapping.class)
                .matching(query(criteria).with(PageRequest.of(page, size)).sort(sort))
                .all()
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .collectList()
                .map(IAlSudaisUserRoleEntityPojoMapper.mapUserRoleEntityListPojoListMapping)
                .map(userRoleResponseBeanList -> ServiceResponseBean.builder().status(Boolean.TRUE).data(userRoleResponseBeanList).build());
    }

    @Override
    public Mono<?> addUserRole(UserRoleRequestBean userRoleRequestBean) {
        return this.alSudaisUserRoleMappingRepository.findByAurmUserIdAndAurmRoleIdIn(userRoleRequestBean.getUserId(), userRoleRequestBean.getRoleIds())
                .switchIfEmpty(Flux.just(AlSudaisUserRoleMapping.builder().build()))
                .collectList()
                .map(data -> {
                    if (data.size() != BigInteger.ZERO.intValue())
                        userRoleRequestBean.getRoleIds().removeAll(data.parallelStream().map(AlSudaisUserRoleMapping::getAurmRoleId).collect(Collectors.toSet()));

                    userRoleRequestBean.setStatus(UserRoleStatusEnum.ACTIVE.getValue());
                    userRoleRequestBean.setCreatedBy(this.alSudaisLocaleResolverConfig.getIdentity());

                    return userRoleRequestBean;
                })
                .filter(userRoleRequestBeanInner -> userRoleRequestBeanInner.getRoleIds().size() > BigInteger.ZERO.intValue())
                .switchIfEmpty(Mono.error(new AlSudaisAlreadyExistException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_ROLE_ALREADY_EXIST))))
                .map(IAlSudaisUserRolePojoEntityMapper.addUserRoleMapping)
                .flatMapMany(this.alSudaisUserRoleMappingRepository::saveAll)
                .count()
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_ROLE_ADD_SUCCESS)).build());
    }

    @Override
    public Mono<?> fetchUserRoleById(String userId) {
        return this.alSudaisUserRoleMappingRepository.findByAurmUserId(userId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .collectList()
                .map(IAlSudaisUserRoleEntityPojoMapper.mapUserRoleEntityPojoMapping)
                .map(userRoleResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(userRoleResponseBean).build());
    }

    @Override
    public Mono<?> updateUserRole(UserRoleRequestBean userRoleRequestBean) {
        AtomicReference<List<String>> atomicReferenceRoleIds = new AtomicReference<>(userRoleRequestBean.getRoleIds().stream().toList());
        return this.alSudaisUserRoleMappingRepository.findByAurmUserId(userRoleRequestBean.getUserId())
                .switchIfEmpty(Flux.just(AlSudaisUserRoleMapping.builder().build()))
                .collectList()
                .flatMap(data -> {
                    userRoleRequestBean.setStatus(UserRoleStatusEnum.ACTIVE.getValue());
                    userRoleRequestBean.setCreatedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    if (data.size() == BigInteger.ONE.intValue() && data.get(BigInteger.ZERO.intValue()).getAurmSeqId() == null)
                        return Mono.zip(Mono.just(userRoleRequestBean), Mono.just(AlSudaisCommonConstants.INSTANCE.BLANK_STRING));

                    Set<String> newRoleIdsSet = new HashSet<>(userRoleRequestBean.getRoleIds());
                    Set<String> existingRoleIdsSet = data.parallelStream().map(AlSudaisUserRoleMapping::getAurmRoleId).collect(Collectors.toSet());

                    userRoleRequestBean.getRoleIds().removeAll(existingRoleIdsSet);
                    existingRoleIdsSet.removeAll(newRoleIdsSet);

                    return Mono.zip(Mono.just(userRoleRequestBean), Mono.just(existingRoleIdsSet));
                })
                .flatMap(tuple -> tuple.getT2() instanceof Set ? this.alSudaisUserRoleMappingRepository.deleteByAurmUserIdAndAurmRoleIdIn(tuple.getT1().getUserId(), (Set<String>) tuple.getT2()).thenReturn(tuple.getT1()) : Mono.just(tuple.getT1()))
                .filter(userRoleRequestBeanInner -> userRoleRequestBeanInner.getRoleIds().size() > BigInteger.ZERO.intValue())
                .switchIfEmpty(Mono.error(new AlSudaisAlreadyExistException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_ROLE_ALREADY_EXIST))))
                .map(IAlSudaisUserRolePojoEntityMapper.updateUserRoleMapping)
                .flatMapMany(this.alSudaisUserRoleMappingRepository::saveAll)
                .count()
                .flatMap(alSudaisUserRoleMapping -> this.alSudaisAuthenticationService.updateUserAttributes(userRoleRequestBean.getUserId(), Map.of(UserAttributesEnum.ROLE.getValue(), List.copyOf(atomicReferenceRoleIds.get()))).thenReturn(alSudaisUserRoleMapping))
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_ROLE_UPDATE_SUCCESS)).build());
    }

    @Override
    public Mono<?> deleteUserRoleById(String userId) {
        return this.alSudaisUserRoleMappingRepository.deleteByAurmUserId(userId)
                .filter(count -> count != BigInteger.ZERO.longValue())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .flatMap(alSudaisUserRoleMapping -> this.alSudaisAuthenticationService.updateUserAttributes(userId, Map.of(UserAttributesEnum.ROLE.getValue(), List.of(AlSudaisCommonConstants.INSTANCE.BLANK_STRING))).thenReturn(alSudaisUserRoleMapping))
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_DELETED)).build());
    }

    @Override
    public Mono<?> fetchHistoricalData(String userId) {
        ReactiveAuditQuery annotation = AlSudaisUserRoleMapping.class.getAnnotation(ReactiveAuditQuery.class);
        LOGGER.info("Query :: {}", annotation.query());

        return this.databaseClient.sql(annotation.query())
                .bind("user_id", userId)
                .map(IAlSudaisUserRoleEntityPojoMapper.mapUserRoleHistoricalData)
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
