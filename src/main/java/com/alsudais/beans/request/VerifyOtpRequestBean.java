package com.alsudais.beans.request;

import com.alsudais.commons.LocaleMessageCodeConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequestBean {
    @Email(message = LocaleMessageCodeConstants.EMAIL_INVALID)
    @NotBlank(message = LocaleMessageCodeConstants.EMAIL_CANT_BLANK)
    @JsonProperty(value = "email_id")
    private String emailId;

    @NotBlank(message = LocaleMessageCodeConstants.OTP_CANT_BLANK)
    private String otp;

}
