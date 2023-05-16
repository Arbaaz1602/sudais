package com.alsudais.services.impl;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.beans.request.UserOnlineStatusDetailRequestBean;
import com.alsudais.beans.response.AuditResponseBean;
import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisUserOnlineStatusDetail;
import com.alsudais.enums.UserRoleModuleStatusEnum;
import com.alsudais.enums.UserStatusEnum;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.exceptions.AlSudaisNotActiveException;
import com.alsudais.mappers.entity.pojo.IAlSudaisUserOnlineStatusDetailEntityPojoMapper;
import com.alsudais.mappers.pojo.entity.IAlSudaisUserOnlineStatusDetailPojoEntityMapper;
import com.alsudais.repositories.IAlSudaisUserMasterRepository;
import com.alsudais.repositories.IAlSudaisUserOnlineStatusDetailRepository;
import com.alsudais.services.IAlSudaisUserOnlineStatusDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class AlSudaisUserOnlineStatusDetailService implements IAlSudaisUserOnlineStatusDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlSudaisModuleService.class);
    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;
    @Autowired
    private IAlSudaisUserOnlineStatusDetailRepository iAlSudaisUserOnlineStatusDetailRepository;
    @Autowired
    private IAlSudaisUserOnlineStatusDetailPojoEntityMapper alSudaisUserOnlineStatusDetailPojoEntityMapper;
    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @Autowired
    private IAlSudaisUserMasterRepository alSudaisUserMasterRepository;
    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<?> fetchUserOnlineStatusByUserId(String userId) {
        return this.alSudaisUserMasterRepository.findByAumUserId(userId)
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .filter(alSudaisUserMaster -> (alSudaisUserMaster.getAumStatus().equalsIgnoreCase(UserStatusEnum.ACTIVE.getValue())))
                .switchIfEmpty(Mono.error(new AlSudaisNotActiveException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_IS_NOT_ACTIVE))))
                .then(Mono.defer(() -> this.iAlSudaisUserOnlineStatusDetailRepository.findByAuadUserId(userId)
                        .cast(AlSudaisUserOnlineStatusDetail.class)
                        .switchIfEmpty(Mono.defer(() -> Mono.just(AlSudaisUserOnlineStatusDetail.builder()
                                .auadUserId(userId)
                                .auadStatus(UserRoleModuleStatusEnum.INACTIVE.getValue())
                                .auadCreatedBy(alSudaisLocaleResolverConfig.getIdentity())
                                .auadCreatedDate(LocalDateTime.now())
                                .build()))
                                .flatMap(this.iAlSudaisUserOnlineStatusDetailRepository::save))
                        .map(IAlSudaisUserOnlineStatusDetailEntityPojoMapper.mapUserOnlineStatusDetailEntityPojoMapping)
                        .map(userOnlineStatusResponseBean -> ServiceResponseBean.builder().status(Boolean.TRUE).data(userOnlineStatusResponseBean).build())));

    }

    @Override
    public Mono<?> updateUserOnlineStatus(UserOnlineStatusDetailRequestBean userOnlineStatusDetailRequestBean) {
        return this.alSudaisUserMasterRepository.findByAumUserId(userOnlineStatusDetailRequestBean.getUserId())
                .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                .filter(alSudaisUserMaster -> (alSudaisUserMaster.getAumStatus().equalsIgnoreCase(UserStatusEnum.ACTIVE.getValue())))
                .switchIfEmpty(Mono.error(new AlSudaisNotActiveException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_IS_NOT_ACTIVE))))
                .then(Mono.defer(() -> this.iAlSudaisUserOnlineStatusDetailRepository.findByAuadUserId(userOnlineStatusDetailRequestBean.getUserId())
                        .log()
                        .switchIfEmpty(Mono.error(new AlSudaisDataNotFoundException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.DATA_NOT_FOUND))))
                        .flatMap(alSudaisUserOnlineStatus -> {
                            LOGGER.info("AlSudaisUserOnlineStatus :: {} ", alSudaisUserOnlineStatus);
                            userOnlineStatusDetailRequestBean.setModifiedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                            return this.iAlSudaisUserOnlineStatusDetailRepository.save(this.alSudaisUserOnlineStatusDetailPojoEntityMapper.INSTANCE.updateUserOnlineStatusDetail.apply(userOnlineStatusDetailRequestBean, alSudaisUserOnlineStatus));
                        })
                        .map(alSudaisUserOnlineStatus -> ServiceResponseBean.builder().status(Boolean.TRUE).data(IAlSudaisUserOnlineStatusDetailEntityPojoMapper.mapUserOnlineStatusDetailEntityPojoMapping.apply(alSudaisUserOnlineStatus)).build())));

    }

    @Override
    public Mono<?> fetchHistoricalData(String userId) {
        ReactiveAuditQuery annotation = AlSudaisUserOnlineStatusDetail.class.getAnnotation(ReactiveAuditQuery.class);
        LOGGER.info("Query :: {}", annotation.query());

        return this.databaseClient.sql(annotation.query())
                .bind("user_id", userId)
                .map(IAlSudaisUserOnlineStatusDetailEntityPojoMapper.mapUserOnlineStatusHistoricalData)
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
