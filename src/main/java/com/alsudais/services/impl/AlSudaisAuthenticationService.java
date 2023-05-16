package com.alsudais.services.impl;


import com.alsudais.beans.NotificationDetailPayloadBean;
import com.alsudais.beans.request.*;
import com.alsudais.beans.response.*;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.configs.AlSudaisCacheConfig;
import com.alsudais.configs.AlSudaisKeyCloakConfig;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.entities.AlSudaisUserMaster;
import com.alsudais.enums.CacheKeyEnum;
import com.alsudais.enums.UserAttributesEnum;
import com.alsudais.enums.UserStatusEnum;
import com.alsudais.enums.UuidPrefixEnum;
import com.alsudais.exceptions.AlSudaisCustomException;
import com.alsudais.feign.clients.IAlSudaisNotificationServiceReactiveFeignClient;
import com.alsudais.mappers.pojo.entity.IAlSudaisUserMasterPojoEntityMapper;
import com.alsudais.repositories.IAlSudaisUserMasterRepository;
import com.alsudais.services.IAlSudaisAuthenticationService;
import com.alsudais.services.IAlSudaisUserCodeSequenceService;
import com.alsudais.services.IAlSudaisUserRoleModuleService;
import com.alsudais.services.IAlSudaisUserRoleService;
import com.alsudais.utils.AlSudaisCommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.resource.AuthorizationResource;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
public class AlSudaisAuthenticationService implements IAlSudaisAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String applicationName;

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlSudaisKeyCloakConfig alSudaisKeyCloakConfig;

    @Autowired
    private IAlSudaisUserMasterRepository alSudaisUserMasterRepository;

    @Autowired
    private IAlSudaisNotificationServiceReactiveFeignClient alSudaisNotificationServiceReactiveFeignClient;

    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @Autowired
    private IAlSudaisUserMasterPojoEntityMapper alSudaisUserMasterPojoEntityMapper;

    @Autowired
    private IAlSudaisUserRoleService alSudaisUserRoleService;

    @Autowired
    private IAlSudaisUserRoleModuleService alSudaisUserRoleModuleService;

    @Autowired
    private IAlSudaisUserCodeSequenceService alSudaisUserCodeSequenceService;

    @Value("${redis.api.timeout:60}")
    private Long redisApiTimeout;
    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    @Override
    public Mono<?> signUpByAdmin(SignUpRequestBean signUpRequestBean) {
        AtomicReference<String> tempPasswordAtomicReference = new AtomicReference<>();
        return this.alSudaisUserMasterRepository.findByAumEmailIdIgnoreCase(signUpRequestBean.getEmailId())
                .log()
                .defaultIfEmpty(AlSudaisUserMaster.builder().build())
                .filter(alSudaisUserMaster -> alSudaisUserMaster.getAumSeqId() == null)
                .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_WITH_EMAIL_ALREADY_EXIST).formatted(signUpRequestBean.getEmailId()))))
                .flatMap(__ -> {
                    try {
                        UsersResource usersResource = this.keycloak.realm(this.alSudaisKeyCloakConfig.getKeycloakRealm()).users();
                        LOGGER.info("Users Resource :: {}", usersResource);
                        LOGGER.info("Users Resource :: {}", usersResource.count());

                        String userId = AlSudaisCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.USER.getValue());
                        LOGGER.info("User Id :: {} ", userId);

                        UserRepresentation userRepresentation = new UserRepresentation();
                        userRepresentation.setEnabled(Boolean.TRUE);
                        userRepresentation.setUsername(signUpRequestBean.getEmailId());
                        userRepresentation.setFirstName(signUpRequestBean.getFirstName());
                        userRepresentation.setLastName(signUpRequestBean.getLastName());
                        userRepresentation.setEmail(signUpRequestBean.getEmailId());
                        userRepresentation.setEmailVerified(Boolean.TRUE);
                        userRepresentation.setAttributes(Map.of(
                                        UserAttributesEnum.ROLE.getValue(), Objects.nonNull(signUpRequestBean.getRoles()) ? List.copyOf(signUpRequestBean.getRoles()) : List.of(AlSudaisCommonConstants.INSTANCE.BLANK_STRING),
                                        UserAttributesEnum.USER_ID.getValue(), List.of(userId)
                                )
                        );

                        Response response = usersResource.create(userRepresentation);
                        LOGGER.info("Response :: {}", response);
                        LOGGER.info("Response :: {}", response.getStatus());
                        LOGGER.info("Response :: {}", response.readEntity(String.class));

                        if (response.getStatus() == HttpStatus.CREATED.value()) {
                            String keycloakId = CreatedResponseUtil.getCreatedId(response);
                            LOGGER.info("KeycloakId :: {}", keycloakId);
                            String temporaryPassword = AlSudaisCommonUtils.INSTANCE.generateUniquePassword(8);
                            LOGGER.info("Temporary password :: {}", temporaryPassword);

                            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                            credentialRepresentation.setTemporary(Boolean.FALSE);
                            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                            credentialRepresentation.setValue(temporaryPassword);

                            tempPasswordAtomicReference.set(temporaryPassword);

                            UserResource userResource = null;
                            try {
                                userResource = usersResource.get(keycloakId);
                                userResource.resetPassword(credentialRepresentation);

                                AlSudaisUserMaster alSudaisUserMaster = this.alSudaisUserMasterPojoEntityMapper.userMasterPojoToUserMasterEntity(signUpRequestBean);
                                alSudaisUserMaster.setAumUserId(userId);
                                alSudaisUserMaster.setAumKeycloakId(keycloakId);
                                alSudaisUserMaster.setAumStatus(UserStatusEnum.ACTIVE.getValue());
                                alSudaisUserMaster.setAumCreatedBy(this.alSudaisLocaleResolverConfig.getIdentity());
                                return Mono.just(alSudaisUserMaster);
                            } catch (BadRequestException e) {
                                if (userResource != null) userResource.remove();

                                String error = e.getResponse().readEntity(String.class);
                                LOGGER.info("Error :: {}", error);

                                JsonNode jsonNode = this.objectMapper.readTree(error);
                                LOGGER.info("JsonNode :: {}", jsonNode);

                                return Mono.error(new AlSudaisCustomException(jsonNode.get("error_description").asText()));
                            }
                        } else
                            return Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_CREATION_FAILURE)));
                    } catch (Exception e) {
                        LOGGER.error("Exception :: ", e);
                        return Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_CREATION_FAILURE)));
                    }
                })
                .flatMap(alSudaisUserMaster -> this.alSudaisUserCodeSequenceService.fetchUserCodeSequenceByIdentifier(signUpRequestBean.getTenantId().toUpperCase())
                        .cast(ServiceResponseBean.class)
                        .map(ServiceResponseBean::getData)
                        .cast(UserCodeSequenceResponseBean.class)
                        .map(userCodeSequenceResponseBean -> userCodeSequenceResponseBean.getIdentifier().concat(AlSudaisCommonConstants.INSTANCE.UNDERSCORE_STRING).concat(String.format("%04d", userCodeSequenceResponseBean.getCounter())))
                        .flatMap(userCode -> {
                            alSudaisUserMaster.setAumUserCode(userCode);
                            return Mono.just(alSudaisUserMaster);
                        })
                )
                .flatMap(this.alSudaisUserMasterRepository::save)
                .flatMap(alSudaisUserMaster -> {
                    if (signUpRequestBean.getRoles() != null && !signUpRequestBean.getRoles().isEmpty())
                        this.alSudaisUserRoleService.addUserRole(UserRoleRequestBean.builder().userId(alSudaisUserMaster.getAumUserId()).roleIds(signUpRequestBean.getRoles()).build()).subscribe();

                    if (signUpRequestBean.getUserRoleModuleSetRequestBeans() != null && !signUpRequestBean.getUserRoleModuleSetRequestBeans().isEmpty())
                        this.alSudaisUserRoleModuleService.addUserRoleModuleInternal(UserRoleModuleRequestBean.builder().userId(alSudaisUserMaster.getAumUserId()).userRoleModuleSetRequestBeans(signUpRequestBean.getUserRoleModuleSetRequestBeans()).build()).subscribe();
                    return Mono.just(alSudaisUserMaster);
                })
                .publishOn(Schedulers.parallel())
                .subscribeOn(Schedulers.parallel())
                .doOnSuccess(alSudaisUserMaster -> this.alSudaisNotificationServiceReactiveFeignClient.sendEmailEvent(NotificationDetailPayloadBean.builder()
                                .to(List.of(alSudaisUserMaster.getAumEmailId()))
                                .subjectLine("User creation")
                                .templateName("SIGN_UP")
                                .notificationType("EMAIL")
                                .dataMap(Map.of("email_id", alSudaisUserMaster.getAumEmailId(), "temp_password", tempPasswordAtomicReference.get()))
                                .build())
                        .subscribe())
                .map(alSudaisUserMaster -> ServiceResponseBean.builder()
                        .status(Boolean.TRUE)
                        .data(SignUpResponseBean.builder().userId(alSudaisUserMaster.getAumUserId()).build())
                        .message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_CREATION_SUCCESS))
                        .build());
    }

    @Override
    public Mono<?> signIn(SignInRequestBean signInRequestBean) {
        return this.alSudaisUserMasterRepository.findByAumEmailIdIgnoreCaseOrAumUserCodeIgnoreCase(signInRequestBean.getEmailIdOrUserCode(), signInRequestBean.getEmailIdOrUserCode())
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_WITH_EMAIL_OR_USERCODE_NOT_PRESENT).formatted(signInRequestBean.getEmailIdOrUserCode()))))
                .flatMap(alSudaisUserMaster -> {
                    try {
                        UsersResource usersResource = this.keycloak.realm(this.alSudaisKeyCloakConfig.getKeycloakRealm()).users();
                        LOGGER.info("Users Resource :: {}", usersResource);

                        UserResource userResource = usersResource.get(alSudaisUserMaster.getAumKeycloakId());
                        LOGGER.info("User Resource :: {}", userResource);

                        AccessTokenResponse accessTokenResponse = null;
                        try {
                            accessTokenResponse = this.alSudaisKeyCloakConfig.authzClient().obtainAccessToken(alSudaisUserMaster.getAumEmailId(), signInRequestBean.getPassword());
                            LOGGER.info("Access Token Response :: {}", accessTokenResponse);
                        } catch (HttpResponseException httpResponseException) {
                            if (httpResponseException.getStatusCode() == HttpStatus.UNAUTHORIZED.value())
                                return Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.INCORRECT_CREDENTIALS)));
                        }

                        LOGGER.info("UserName :: {}", this.alSudaisLocaleResolverConfig.getIdentity());

                        return Mono.just(ServiceResponseBean.builder().data(SignInResponseBean.builder()
                                        .userId(alSudaisUserMaster.getAumUserId())
                                        .emailId(alSudaisUserMaster.getAumEmailId())
                                        .accessToken(accessTokenResponse.getToken())
                                        .expiresIn(accessTokenResponse.getExpiresIn())
                                        .refreshExpiresIn(accessTokenResponse.getRefreshExpiresIn())
                                        .refreshToken(accessTokenResponse.getRefreshToken())
                                        .tokenType(accessTokenResponse.getTokenType())
                                        .idToken(accessTokenResponse.getIdToken())
                                        .notBeforePolicy(accessTokenResponse.getNotBeforePolicy())
                                        .sessionState(accessTokenResponse.getSessionState())
                                        .profilePhoto(alSudaisUserMaster.getAumProfilePhoto())
                                        .firstName(alSudaisUserMaster.getAumFirstName())
                                        .middleName(alSudaisUserMaster.getAumMiddleName())
                                        .lastName(alSudaisUserMaster.getAumLastName())
                                        .emailId(alSudaisUserMaster.getAumEmailId())
                                        .build())
                                .message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.SIGN_IN_SUCCESS))
                                .status(Boolean.TRUE)
                                .build());
                    } catch (Exception e) {
                        LOGGER.error("Exception :: ", e);
                        return Mono.just(ServiceResponseBean.builder().message(e.getMessage()).build());
                    }
                });
    }

    @Override
    public Mono<?> verifyToken(String accessToken) {
        return Mono.just(accessToken)
                .log()
                .map(accessTokenInner -> {
                    try {
                        AuthorizationResource authorizationResource = this.alSudaisKeyCloakConfig.authzClient().authorization(accessTokenInner);
                        LOGGER.info("AuthorizationResource :: {}", authorizationResource);

                        AuthorizationResponse authorizationResponse = authorizationResource.authorize();
                        LOGGER.info("AuthorizationResponse :: {}", authorizationResponse);

                        return ServiceResponseBean.builder().status(Boolean.TRUE).build();
                    } catch (RuntimeException e) {
                        if (e.getCause() instanceof HttpResponseException httpResponseException) {
                            if (httpResponseException.getBytes() == null)
                                return Mono.error(new AlSudaisCustomException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
                            try {
                                String error = new String(httpResponseException.getBytes(), StandardCharsets.UTF_8);
                                LOGGER.info("Error :: {}", error);
                                JsonNode jsonNode = this.objectMapper.readTree(error);
                                LOGGER.info("JsonNode :: {}", jsonNode);
                                return Mono.error(new AlSudaisCustomException(jsonNode.get("error_description").asText()));
                            } catch (JsonProcessingException ex) {
                                LOGGER.error("JsonProcessingException :: ", ex);
                                return Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.TOKEN_VERIFICATION_FAILURE)));
                            }
                        }
                        return Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.TOKEN_VERIFICATION_FAILURE)));
                    }
                });
    }

    @Override
    public Mono<?> resetPassword(ResetPasswordRequestBean resetPasswordRequestBean) {
        return this.alSudaisUserMasterRepository.findByAumUserId(resetPasswordRequestBean.getUserId())
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_NOT_PRESENT))))
                .flatMap(alSudaisUserMaster -> {
                    try {
                        AccessTokenResponse accessTokenResponse = this.alSudaisKeyCloakConfig.authzClient().obtainAccessToken(alSudaisUserMaster.getAumEmailId(), resetPasswordRequestBean.getOldPassword());
                        LOGGER.info("Access Token Response :: {}", accessTokenResponse);

                        UsersResource usersResource = this.keycloak.realm(this.alSudaisKeyCloakConfig.getKeycloakRealm()).users();
                        LOGGER.info("Users Resource :: {}", usersResource);

                        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                        credentialRepresentation.setValue(resetPasswordRequestBean.getNewPassword());

                        UserResource userResource = usersResource.get(alSudaisUserMaster.getAumKeycloakId());
                        userResource.resetPassword(credentialRepresentation);

                        alSudaisUserMaster.setAumStatus(UserStatusEnum.ACTIVE.getValue());
                        alSudaisUserMaster.setAumModifiedBy(this.applicationName);
                        alSudaisUserMaster.setAumModifiedDate(LocalDateTime.now());

                        return Mono.just(alSudaisUserMaster);
                    } catch (Exception e) {
                        try {
                            if (e instanceof BadRequestException badRequestException) {
                                String error = badRequestException.getResponse().readEntity(String.class);
                                LOGGER.info("Error :: {}", error);

                                JsonNode jsonNode = this.objectMapper.readTree(error);
                                LOGGER.info("JsonNode :: {}", jsonNode);
                                return Mono.error(new AlSudaisCustomException(jsonNode.get("error_description").asText()));
                            }
                            return Mono.error(new AlSudaisCustomException(e.getMessage()));
                        } catch (JsonProcessingException ex) {
                            LOGGER.error("JsonProcessingException :: ", ex);
                            return Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.PASSWORD_RESET_FAILURE)));
                        }
                    }
                })
                .map(this.alSudaisUserMasterRepository::save)
                .flatMap(signUpRequestBeanInner -> Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.PASSWORD_RESET_SUCCESS)).build()));
    }

    @Override
    public Mono<?> forgotPassword(ForgotPasswordRequestBean forgotPasswordRequestBean) {
        return this.alSudaisUserMasterRepository.findByAumEmailIdIgnoreCase(forgotPasswordRequestBean.getEmailId())
                .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_NOT_PRESENT))))
                .zipWith(Mono.just(new DecimalFormat("000000").format(new Random().nextInt(999999))))
                .flatMap(tuple2 -> {
                    LOGGER.info("User :: {}", tuple2.getT1());
                    LOGGER.info("OTP :: {}", tuple2.getT2());

                    String cacheKey = CacheKeyEnum.FORGOT_PASSWORD.getValue().concat(tuple2.getT1().getAumUserId());
                    LOGGER.info("CacheKey :: {}", cacheKey);

                    Mono<?> putResult = this.reactiveRedisTemplate.opsForValue().set(cacheKey, tuple2.getT2())
                            .map(__ -> this.reactiveRedisTemplate.expire(cacheKey, Duration.ofSeconds(60))).thenReturn(tuple2);

                    return putResult.map(o -> tuple2);
                })
                .doOnSuccess(tuple2 -> this.alSudaisNotificationServiceReactiveFeignClient.sendEmailEvent(NotificationDetailPayloadBean.builder()
                                .to(List.of(tuple2.getT1().getAumEmailId()))
                                .subjectLine("Forgot Password")
                                .templateName("FORGOT_PASSWORD")
                                .notificationType("EMAIL")
                                .dataMap(Map.of("email_id", tuple2.getT1().getAumEmailId(), "otp", tuple2.getT2()))
                                .build())
                        .subscribe())
                .map(tuple2 -> ServiceResponseBean.builder()
                        .status(Boolean.TRUE)
                        .message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.OTP_SENT_SUCCESS))
                        .build());
    }

    @Override
    public Mono<?> verifyOtp(VerifyOtpRequestBean verifyOtpRequestBean) {
        return this.alSudaisUserMasterRepository.findByAumEmailIdIgnoreCase(verifyOtpRequestBean.getEmailId())
                .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_NOT_PRESENT))))
                .flatMap(alSudaisUserMaster -> this.reactiveRedisTemplate.opsForValue().get(CacheKeyEnum.FORGOT_PASSWORD.getValue().concat(alSudaisUserMaster.getAumUserId()))
                        .doOnError(__ -> Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.OTP_NOT_FOUND))))
                        .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.OTP_NOT_FOUND))))
                        .map(otp -> {
                            if (verifyOtpRequestBean.getOtp().equals(otp)) {
                                this.reactiveRedisTemplate.delete(CacheKeyEnum.FORGOT_PASSWORD.getValue().concat(alSudaisUserMaster.getAumUserId())).subscribe();
                                return ServiceResponseBean.builder().status(Boolean.TRUE).data(VerifyOtpResponseBean.builder().userId(alSudaisUserMaster.getAumUserId()).build())
                                        .message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.OTP_VERIFICATION_SUCCESS))
                                        .build();
                            } else
                                return ServiceResponseBean.builder().message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.OTP_VERIFICATION_FAILED)).build();
                        }));
    }

    @Override
    public Mono<?> signOut(String userId) {
        return this.alSudaisUserMasterRepository.findByAumUserId(userId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_NOT_PRESENT))))
                .map(alSudaisUserMaster -> {
                    UsersResource usersResource = this.keycloak.realm(this.alSudaisKeyCloakConfig.getKeycloakRealm()).users();
                    LOGGER.info("Users Resource :: {}", usersResource);
                    usersResource.get(alSudaisUserMaster.getAumKeycloakId()).logout();

                    return ServiceResponseBean.builder().status(Boolean.TRUE).message(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.SIGN_OUT_SUCCESS)).build();
                });
    }

    public Mono<?> updateUserAttributes(String userId, Map<String, List<String>> attributes) {
        return this.alSudaisUserMasterRepository.findByAumUserId(userId)
                .log()
                .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_NOT_PRESENT))))
                .flatMap(alSudaisUserMaster -> {
                    UsersResource usersResource = this.keycloak.realm(this.alSudaisKeyCloakConfig.getKeycloakRealm()).users();
                    LOGGER.info("Users Resource :: {}", usersResource);

                    UserResource userResource = usersResource.get(alSudaisUserMaster.getAumKeycloakId());
                    LOGGER.info("User Resource :: {}", userResource);

                    return Mono.just(userResource);

                })
                .switchIfEmpty(Mono.error(new AlSudaisCustomException(this.alSudaisLocaleResolverConfig.toLocale(LocaleMessageCodeConstants.USER_NOT_PRESENT))))
                .doOnSuccess(userResource -> {
                    UserRepresentation userRepresentation = userResource.toRepresentation();
                    Map<String, List<String>> userRepresentationAttributes = userRepresentation.getAttributes();
                    userRepresentationAttributes.putAll(attributes);
                    userRepresentation.setAttributes(userRepresentationAttributes);
                    userResource.update(userRepresentation);
                });
    }
}
