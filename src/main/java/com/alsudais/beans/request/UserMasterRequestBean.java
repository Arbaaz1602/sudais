package com.alsudais.beans.request;

import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.commons.LocaleMessageCodeConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class UserMasterRequestBean {

    @NotNull(message = LocaleMessageCodeConstants.USER_ID_CANT_BLANK)
    @JsonProperty(value = "user_id")
    private String userId;

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

    @JsonProperty(value = "profile_photo")
    private String profilePhoto;

    @JsonProperty(value = "roles")
    private Set<String> roles;

    @JsonProperty(value = "date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AlSudaisCommonConstants.DOB_DATE_FORMAT)
    private LocalDate dateOfBirth;

    @JsonProperty(value = "status")
    private String status;

    @JsonIgnore
    private String modifiedBy;
}

