package com.alsudais.beans.request;

import com.alsudais.commons.LocaleMessageCodeConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import com.alsudais.commons.AlSudaisCommonConstants;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@JsonInclude(Include.NON_NULL)
public class SignUpRequestBean {
    @NotEmpty(message = LocaleMessageCodeConstants.EMAIL_CANT_BLANK)
    @Email(message = LocaleMessageCodeConstants.EMAIL_INVALID)
    @JsonProperty(value = "email_id")
    private String emailId;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "middle_name")
    private String middleName;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @NotEmpty(message = LocaleMessageCodeConstants.TENANT_ID_CANT_EMPTY)
    @JsonProperty(value = "tenant_id")
    private String tenantId;

    @JsonProperty(value = "country_id")
    private String countryId;

    @JsonProperty(value = "state_id")
    private String stateId;

    @JsonProperty(value = "city_id")
    private String cityId;

    @JsonProperty(value = "postal_code_id")
    private String postalCodeId;

    @JsonProperty(value = "nationality_id")
    private String nationalityId;

    @JsonProperty(value = "allocated_location_id")
    private String allocatedLocationId;

    @JsonProperty(value = "key_responsibility_area_id")
    private String keyResponsibilityAreaId;

    @JsonProperty(value = "roles")
    private Set<String> roles;

    @JsonProperty(value = "date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlSudaisCommonConstants.DOB_DATE_FORMAT)
    private LocalDate dateOfBirth;

    @JsonProperty(value = "user_module")
    private Set<UserRoleModuleSetRequestBean> userRoleModuleSetRequestBeans;

    @JsonProperty(value = "status")
    private String status;

    @JsonIgnore
    private String createdBy;

    @JsonIgnore
    private LocalDateTime createdDate;

    public String getEmailId() {
        return this.emailId.toLowerCase();
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId.toLowerCase();
    }
}
