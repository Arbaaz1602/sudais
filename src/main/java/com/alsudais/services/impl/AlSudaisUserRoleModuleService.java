package com.alsudais.services.impl;

import com.alsudais.beans.request.UserRoleModuleRequestBean;
import com.alsudais.beans.request.UserRoleModuleSetRequestBean;
import com.alsudais.beans.response.PageResponseBean;
import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.beans.response.UserRoleModuleSetResponseBean;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.*;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.mappers.entity.pojo.IAlSudaisUserRoleModuleEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisUserRoleModulePojoEntityMapper;
import com.alsudais.repositories.*;
import com.alsudais.services.IAlSudaisUserRoleModuleService;
import com.alsudais.utils.AlSudaisReactiveSpecificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.relational.core.query.Query.query;

@Service
@Transactional
public class AlSudaisUserRoleModuleService implements IAlSudaisUserRoleModuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlSudaisModuleService.class);
    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;
    @Autowired
    private IAlSudaisUserRoleModuleMappingRepository alSudaisUserRoleModuleMappingRepository;
    @Autowired
    private IAlSudaisUserRoleModulePojoEntityMapper alSudaisUserRoleModulePojoEntityMapper;
    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;
    @Autowired
    private IAlSudaisUserMasterRepository alSudaisUserMasterRepository;
    @Autowired
    private IAlSudaisRoleDetailRepository alSudaisRoleDetailRepository;
    @Autowired
    private IAlSudaisModuleDetailRepository alSudaisModuleDetailRepository;

    @Autowired
    private IAlSudaisUserRoleMappingRepository alSudaisUserRoleMappingRepository;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public Mono<?> addUserRoleModule(UserRoleModuleRequestBean userRoleModuleRequestBean) {
        return this.alSudaisUserMasterRepository.findByAumUserId(userRoleModuleRequestBean.getUserId())
                .log()
                .defaultIfEmpty(AlSudaisUserMaster.builder().build())
                .zipWith(this.alSudaisRoleDetailRepository.findByArdRoleId(userRoleModuleRequestBean.getRoleId()).defaultIfEmpty(AlSudaisRoleDetail.builder().build()))
                .filter(tuple -> !ObjectUtils.isEmpty(tuple.getT1().getAumSeqId()) || !ObjectUtils.isEmpty(tuple.getT2().getArdSeqId()))
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .flatMap(__ -> {
                    Set<String> moduleIdSet = userRoleModuleRequestBean.getUserRoleModuleSetRequestBeans().parallelStream().map(UserRoleModuleSetRequestBean::getModuleId).collect(Collectors.toSet());
                    return this.alSudaisModuleDetailRepository.findByAmdModuleIdIn(moduleIdSet)
                            .collectList()
                            .map(alSudaisModuleDetailList -> alSudaisModuleDetailList.size() == moduleIdSet.size() ? userRoleModuleRequestBean : AlSudaisCommonConstants.INSTANCE.BLANK_STRING);
                })
                .filter(data -> data instanceof UserRoleModuleRequestBean)
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.MODULES_NOT_FOUND))))
                .flatMap(__ ->
                        (StringUtils.isNotEmpty(userRoleModuleRequestBean.getUserId()) ? this.alSudaisUserRoleModuleMappingRepository.deleteByAurmmUserId(userRoleModuleRequestBean.getUserId()) :
                                this.alSudaisUserRoleModuleMappingRepository.deleteByAurmmRoleId(userRoleModuleRequestBean.getRoleId())).thenReturn(userRoleModuleRequestBean)
                )
                .map(userRoleModuleRequestBeanInner -> {
                    userRoleModuleRequestBeanInner.setCreatedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    userRoleModuleRequestBeanInner.setCreatedDate(LocalDateTime.now());
                    return IAlSudaisUserRoleModulePojoEntityMapper.userRoleModulePojoToUserRoleModuleEntity.apply(userRoleModuleRequestBeanInner);
                })
                .flatMapMany(this.alSudaisUserRoleModuleMappingRepository::saveAll)
                .collectList()
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_ROLE_MODULE_ADD_SUCCESS)).build());
    }

    @Override
    public Mono<?> addUserRoleModuleInternal(UserRoleModuleRequestBean userRoleModuleRequestBean) {
        return Mono.just(userRoleModuleRequestBean)
                .flatMap(userRoleModuleRequestBeanInner -> {
                    Set<String> moduleIdSet = userRoleModuleRequestBeanInner.getUserRoleModuleSetRequestBeans().parallelStream().map(UserRoleModuleSetRequestBean::getModuleId).collect(Collectors.toSet());
                    return this.alSudaisModuleDetailRepository.findByAmdModuleIdIn(moduleIdSet)
                            .collectList()
                            .map(alSudaisModuleDetailList -> alSudaisModuleDetailList.size() == moduleIdSet.size() ? userRoleModuleRequestBeanInner : AlSudaisCommonConstants.INSTANCE.BLANK_STRING);
                })
                .filter(data -> data instanceof UserRoleModuleRequestBean)
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.MODULES_NOT_FOUND))))
                .flatMap(__ ->
                        (StringUtils.isNotEmpty(userRoleModuleRequestBean.getUserId()) ? this.alSudaisUserRoleModuleMappingRepository.deleteByAurmmUserId(userRoleModuleRequestBean.getUserId()) :
                                this.alSudaisUserRoleModuleMappingRepository.deleteByAurmmRoleId(userRoleModuleRequestBean.getRoleId())).thenReturn(userRoleModuleRequestBean)
                )
                .map(userRoleModuleRequestBeanInner -> {
                    userRoleModuleRequestBeanInner.setCreatedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                    userRoleModuleRequestBeanInner.setCreatedDate(LocalDateTime.now());
                    return IAlSudaisUserRoleModulePojoEntityMapper.userRoleModulePojoToUserRoleModuleEntity.apply(userRoleModuleRequestBeanInner);
                })
                .flatMapMany(this.alSudaisUserRoleModuleMappingRepository::saveAll)
                .collectList()
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_ROLE_MODULE_ADD_SUCCESS)).build());
    }

    @Override
    public Mono<?> fetchUserRoleModuleById(String userId, String roleId) {
        AtomicReference<Boolean> executeUserRoleQuery = new AtomicReference<>(Boolean.TRUE);
        return this.alSudaisUserRoleModuleMappingRepository.findByAurmmUserIdAndAurmmRoleId(userId, roleId)
                .log()
                .switchIfEmpty(this.alSudaisUserRoleModuleMappingRepository.findByAurmmUserId(userId))
                .switchIfEmpty(this.alSudaisUserRoleModuleMappingRepository.findByAurmmRoleId(roleId))
                .switchIfEmpty(Flux.defer(() -> this.alSudaisUserRoleMappingRepository.findByAurmUserId(userId)
                        .map(AlSudaisUserRoleMapping::getAurmRoleId)
                        .collect(Collectors.toSet())
                        .flatMapMany(roleIds -> {
                            executeUserRoleQuery.set(Boolean.FALSE);
                            return this.alSudaisUserRoleModuleMappingRepository.findByAurmmRoleIdIn(roleIds);
                        })
                        .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND)))))
                )
                .collectList()
                .flatMap(alSudaisUserRoleModuleMapping ->
                        Mono.zip(Mono.just(alSudaisUserRoleModuleMapping),
                                this.alSudaisModuleDetailRepository.findByAmdModuleIdIn(
                                                alSudaisUserRoleModuleMapping.parallelStream()
                                                        .flatMap(alSudaisUserRoleModuleMappingInner -> Stream.of(alSudaisUserRoleModuleMappingInner.getAurmmModuleId(), alSudaisUserRoleModuleMappingInner.getAurmmParentModuleId()))
                                                        .filter(StringUtils::isNotEmpty)
                                                        .collect(Collectors.toSet()))
                                        .collectMap(AlSudaisModuleDetail::getAmdModuleId, alSudaisModuleDetail -> alSudaisModuleDetail))
                )
                .map(IAlSudaisUserRoleModuleEntityPojoMapper.mapUserRoleModuleEntityPojoListMapping)
                .flatMap(userRoleModuleResponseBeanInner ->
                        executeUserRoleQuery.get().booleanValue() ? Mono.just(userId)
                                .filter(userIdInner -> StringUtils.isNotEmpty(userIdInner))
                                .flatMapMany(userIdInner -> this.alSudaisUserRoleMappingRepository.findByAurmUserId(userIdInner))
                                .map(alSudaisUserRoleMapping -> alSudaisUserRoleMapping.getAurmRoleId())
                                .collect(Collectors.toSet())
                                .flatMap(roleIds -> {
                                    LOGGER.info("RoleIds :: {}", roleIds);
                                    return (userRoleModuleResponseBeanInner.getUserRoleModuleSetResponseBeans().size() > BigInteger.ZERO.intValue() ?
                                            this.alSudaisUserRoleModuleMappingRepository.findByAurmmRoleIdInAndAurmmModuleIdNotIn(roleIds, userRoleModuleResponseBeanInner.getUserRoleModuleSetResponseBeans().parallelStream().map(UserRoleModuleSetResponseBean::getModuleId).collect(Collectors.toSet()))
                                            : this.alSudaisUserRoleModuleMappingRepository.findByAurmmRoleIdIn(roleIds))
                                            .collectList();
                                })
                                .flatMap(alSudaisUserRoleModuleMapping ->
                                        Mono.zip(Mono.just(alSudaisUserRoleModuleMapping),
                                                this.alSudaisModuleDetailRepository.findByAmdModuleIdIn(
                                                                alSudaisUserRoleModuleMapping.parallelStream()
                                                                        .flatMap(alSudaisUserRoleModuleMappingInner -> Stream.of(alSudaisUserRoleModuleMappingInner.getAurmmModuleId(), alSudaisUserRoleModuleMappingInner.getAurmmParentModuleId()))
                                                                        .filter(StringUtils::isNotEmpty)
                                                                        .collect(Collectors.toSet()))
                                                        .collectMap(AlSudaisModuleDetail::getAmdModuleId, alSudaisModuleDetail -> alSudaisModuleDetail)))
                                .map(IAlSudaisUserRoleModuleEntityPojoMapper.mapUserRoleModuleEntityPojoSetMapping)
                                .map(moduleSet -> userRoleModuleResponseBeanInner.getUserRoleModuleSetResponseBeans().addAll(moduleSet))
                                .thenReturn(userRoleModuleResponseBeanInner)
                                : Mono.just(userRoleModuleResponseBeanInner)
                )
                .map(userRoleModuleResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(userRoleModuleResponseBean).build());
    }

    @Override
    public Mono<?> fetchAllUserRoleModule(String filterCriteria, String sortCriteria, Integer page, Integer size) {
        Criteria criteria = Criteria.empty();
        if(StringUtils.isNoneEmpty(filterCriteria)) {
            String entityFilterCriteria = AlSudaisReactiveSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "aurmm");
            LOGGER.info("EntityFilterCriteria :: {}", entityFilterCriteria);

            criteria = AlSudaisReactiveSpecificationUtils.INSTANCE.buildCriteria(entityFilterCriteria, new ArrayList<>());
            LOGGER.info("Criteria :: {}", criteria);
        }

        Sort sort = Sort.by("aurmmSeqId");
        if(StringUtils.isNoneEmpty(sortCriteria)){
            sort = AlSudaisReactiveSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "aurmm");
        }
        return this.r2dbcEntityTemplate.select(AlSudaisUserRoleModuleMapping.class)
                .matching(query(criteria).with(PageRequest.of(page, size)).sort(sort))
                .all()
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .collectList()
                .map(IAlSudaisUserRoleModuleEntityPojoMapper.mapUserRoleModuleEntityPojoMapping)
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
    public Mono<?> updateUserRoleModule(UserRoleModuleRequestBean userRoleModuleRequestBean) {
        return this.alSudaisUserMasterRepository.findByAumUserId(userRoleModuleRequestBean.getUserId())
                .log()
                .defaultIfEmpty(AlSudaisUserMaster.builder().build())
                .zipWith(this.alSudaisRoleDetailRepository.findByArdRoleId(userRoleModuleRequestBean.getRoleId()).defaultIfEmpty(AlSudaisRoleDetail.builder().build()))
                .filter(tuple -> !ObjectUtils.isEmpty(tuple.getT1().getAumSeqId()) || !ObjectUtils.isEmpty(tuple.getT2().getArdSeqId()))
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .flatMap(__ -> {
                    Set<String> moduleIdSet = userRoleModuleRequestBean.getUserRoleModuleSetRequestBeans().parallelStream().map(UserRoleModuleSetRequestBean::getModuleId).collect(Collectors.toSet());
                    return this.alSudaisModuleDetailRepository.findByAmdModuleIdIn(moduleIdSet)
                            .collectList()
                            .map(alSudaisModuleDetailList -> alSudaisModuleDetailList.size() == moduleIdSet.size() ? userRoleModuleRequestBean : AlSudaisCommonConstants.INSTANCE.BLANK_STRING);
                })
                .filter(data -> data instanceof UserRoleModuleRequestBean)
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.MODULES_NOT_FOUND))))
                .flatMap(__ -> this.alSudaisUserRoleModuleMappingRepository.findByAurmmUserIdAndAurmmRoleId(userRoleModuleRequestBean.getUserId(), userRoleModuleRequestBean.getRoleId())
                        .single()
                        .flatMap(alSudaisUserRoleModuleMapping -> {
                            userRoleModuleRequestBean.setCreatedBy(alSudaisUserRoleModuleMapping.getAurmmCreatedBy());
                            userRoleModuleRequestBean.setCreatedDate(alSudaisUserRoleModuleMapping.getAurmmCreatedDate());
                            return Mono.just(userRoleModuleRequestBean);
                        })
                        .switchIfEmpty(Mono.just(userRoleModuleRequestBean))
                )
                .flatMap(__ ->
                        (StringUtils.isNotEmpty(userRoleModuleRequestBean.getUserId()) ? this.alSudaisUserRoleModuleMappingRepository.deleteByAurmmUserId(userRoleModuleRequestBean.getUserId()) :
                                this.alSudaisUserRoleModuleMappingRepository.deleteByAurmmRoleId(userRoleModuleRequestBean.getRoleId())).thenReturn(IAlSudaisUserRoleModulePojoEntityMapper.userRoleModulePojoToUserRoleModuleEntity.apply(userRoleModuleRequestBean))
                )
                .flatMapMany(this.alSudaisUserRoleModuleMappingRepository::saveAll)
                .collectList()
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_ROLE_MODULE_UPDATED_SUCCESS)).build());
    }

    @Override
    public Mono<?> deleteUserRoleModule(String userId, String roleId) {
        return this.alSudaisUserRoleModuleMappingRepository.deleteByAurmmUserId(userId)
                .filter(count -> count != BigInteger.ZERO.longValue())
                .switchIfEmpty(this.alSudaisUserRoleModuleMappingRepository.deleteByAurmmRoleId(roleId).defaultIfEmpty(BigInteger.ZERO.longValue()))
                .filter(count -> count != BigInteger.ZERO.longValue())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .map(__ -> ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_DELETED)).build());
    }
}
