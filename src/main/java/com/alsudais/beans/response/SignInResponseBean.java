package com.alsudais.beans.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SignInResponseBean {

	@JsonProperty(value = "access_token")
	private String accessToken;

	@JsonProperty(value = "expires_in")
	private Long expiresIn;

	@JsonProperty(value = "refresh_expires_in")
	private Long refreshExpiresIn;

	@JsonProperty(value = "refresh_token")
	private String refreshToken;

	@JsonProperty(value = "token_type")
	private String tokenType;

	@JsonProperty(value = "id_token")
	private String idToken;

	@JsonProperty(value = "not_before_policy")
	private Integer notBeforePolicy;

	@JsonProperty(value = "session_state")
	private String sessionState;

	@JsonProperty(value = "user_id")
	private String userId;

	@JsonProperty(value = "email_id")
	private String emailId;
	
	@JsonProperty(value = "login_session_id")
	private Long loginSessionId;
	
	@JsonProperty(value = "last_login")
	private String lastLogin;
	
	@JsonProperty(value = "last_logout")
	private String lastLogout;
	
	@JsonProperty(value = "first_name")
	private String firstName;

	@JsonProperty(value = "middle_name")
	private String middleName;
	
	@JsonProperty(value = "last_name")
	private String lastName;

	@JsonProperty(value = "profile_photo")
	private String profilePhoto;
}
