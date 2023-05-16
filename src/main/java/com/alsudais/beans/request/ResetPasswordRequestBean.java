package com.alsudais.beans.request;

import com.alsudais.commons.LocaleMessageCodeConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class ResetPasswordRequestBean {

	@NotEmpty(message = LocaleMessageCodeConstants.USER_ID_CANT_BLANK)
	@JsonProperty(value = "user_id")
	private String userId;

	@Email(message = LocaleMessageCodeConstants.EMAIL_INVALID)
	@NotBlank(message = LocaleMessageCodeConstants.EMAIL_CANT_BLANK)
	@JsonProperty(value = "email_id")
	private String emailId;

	@NotBlank(message = LocaleMessageCodeConstants.OLD_PASSWORD_CANT_EMPTY)
	@JsonProperty(value = "old_password")
	private String oldPassword;

	@NotBlank(message = LocaleMessageCodeConstants.NEW_PASSWORD_CANT_EMPTY)
	@JsonProperty(value = "new_password")
	String newPassword;
	
	public String getEmailId() {
		return this.emailId.toLowerCase();
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId.toLowerCase();
	}
}
