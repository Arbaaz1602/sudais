package com.alsudais.services;

import com.alsudais.beans.request.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface IAlSudaisAuthenticationService {

	public Mono<?> signUpByAdmin(SignUpRequestBean signUpRequestBean);

	public Mono<?> signIn(SignInRequestBean signInRequestBean);
	
	public Mono<?> verifyToken(String accessToken);

	public Mono<?> resetPassword(ResetPasswordRequestBean resetPasswordRequestBean);

	public Mono<?> forgotPassword(ForgotPasswordRequestBean forgotPasswordRequestBean);

	public Mono<?> verifyOtp(VerifyOtpRequestBean verifyOtpRequestBean);

	public Mono<?> signOut(String userId);

	public Mono<?> updateUserAttributes(String userId, Map<String, List<String>> attributes);
}
