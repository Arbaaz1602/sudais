package com.alsudais.beans.request;


import com.alsudais.commons.LocaleMessageCodeConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Data
@JsonInclude(Include.NON_NULL)
public class SignInRequestBean {

    @NotEmpty(message = LocaleMessageCodeConstants.EMAIL_OR_USERCODE_CANT_BLANK)
    @JsonProperty(value = "email_id_or_user_code")
    private String emailIdOrUserCode;

    @NotEmpty(message = LocaleMessageCodeConstants.PASSWORD_CANT_BLANK)
    private String password;
}
