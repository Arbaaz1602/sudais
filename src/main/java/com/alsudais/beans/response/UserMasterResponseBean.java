package com.alsudais.beans.response;

import com.alsudais.beans.CompanyShortInfoBean;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMasterResponseBean {

    @JsonProperty(value = "user_id")
    private String userId;

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

    @JsonProperty(value = "user_code")
    private String userCode;

    @JsonProperty(value = "role_ids")
    private Set<String> roleIds;

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

    @JsonProperty(value = "date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlSudaisCommonConstants.DOB_DATE_FORMAT)
    private LocalDate dateOfBirth;

    @JsonProperty(value = "profile_photo")
    private String profilePhoto;

    @JsonProperty(value = "user_module")
    private Set<UserRoleModuleSetResponseBean> userRoleModuleSetResponseBeans;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "added_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlSudaisCommonConstants.DATE_FORMAT)
    private LocalDateTime addedOn;
    @JsonProperty(value = "modified_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlSudaisCommonConstants.DATE_FORMAT)
    private LocalDateTime modifiedOn;

    @JsonProperty(value = "company_info")
    private CompanyShortInfoBean companyShortInfoBean;
}
