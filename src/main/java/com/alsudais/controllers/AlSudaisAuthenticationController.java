package com.alsudais.controllers;


import com.alsudais.beans.request.*;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.alsudais.services.IAlSudaisAuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;

@Validated
@RestController
@RequestMapping(path = "/${iam-service-version}/authentication")
public class AlSudaisAuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAlSudaisAuthenticationService alSudaisAuthenticationService;

    @PostMapping(path = "/sign-up-by-admin")
    public Mono<?> signUpByAdmin(@Valid @RequestBody SignUpRequestBean signUpRequestBean) {
        return this.alSudaisAuthenticationService.signUpByAdmin(signUpRequestBean);
    }

    @PostMapping(path = "/sign-in")
    public Mono<?> signIn(@Valid @RequestBody SignInRequestBean signInRequestBean) {
        return this.alSudaisAuthenticationService.signIn(signInRequestBean);
    }


    @GetMapping(path = "/verify-access-token")
    public Mono<?> verifyAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) @NotEmpty(message = LocaleMessageCodeConstants.ACCESS_TOKEN_CANT_EMPTY) String accessToken) {
        return this.alSudaisAuthenticationService.verifyToken(accessToken.startsWith(AlSudaisCommonConstants.INSTANCE.BEARER_STRING)
                ? accessToken.replace(AlSudaisCommonConstants.INSTANCE.BEARER_STRING, AlSudaisCommonConstants.INSTANCE.BLANK_STRING)
                : accessToken);
    }

//    @GetMapping(path = "/verify-id-token")
//    public Mono<?> verifyIdToken(@RequestHeader("IdToken") @NotBlank(message = "ID_TOKEN_CANT_EMPTY") String idToken) {
//        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).build());
//    }

    @PostMapping(value = "/reset-password")
    public Mono<?> resetPassword(@Valid @RequestBody ResetPasswordRequestBean resetPasswordRequestBean){
        return this.alSudaisAuthenticationService.resetPassword(resetPasswordRequestBean);
    }

    @PostMapping(value = "/forgot-password")
    public Mono<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestBean forgotPasswordRequestBean){
        return this.alSudaisAuthenticationService.forgotPassword(forgotPasswordRequestBean);
    }

    @PostMapping(value = "/verify-otp")
    public Mono<?> verifyOtp(@Valid @RequestBody VerifyOtpRequestBean verifyOtpRequestBean){
        return this.alSudaisAuthenticationService.verifyOtp(verifyOtpRequestBean);
    }

    @GetMapping(path = "/sign-out")
    public Mono<?> signOut(@RequestParam("user_id") @NotEmpty(message = LocaleMessageCodeConstants.USER_ID_CANT_BLANK) String userId) {
        return this.alSudaisAuthenticationService.signOut(userId);
    }
}
