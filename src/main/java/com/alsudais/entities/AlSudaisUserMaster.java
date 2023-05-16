package com.alsudais.entities;

import com.alsudais.annotations.ReactiveAuditQuery;
import com.alsudais.annotations.ReactiveAudited;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ReactiveAudited
@Table(value = "identity_access_management.alsudais_user_master")
@ReactiveAuditQuery(name = "user_master_audit", query = "SELECT * FROM identity_access_management.alsudais_user_master WHERE aum_user_id = :user_id")
public class AlSudaisUserMaster {

    @Id
    @Column(value = "aum_seq_id")
    private Long aumSeqId;

    @Column(value = "aum_user_id")
    private String aumUserId;

    @Column(value = "aum_keycloak_id")
    private String aumKeycloakId;

    @Column(value = "aum_email_id")
    private String aumEmailId;

    @Column(value = "aum_last_name")
    private String aumLastName;

    @Column(value = "aum_middle_name")
    private String aumMiddleName;

    @Column(value = "aum_first_name")
    private String aumFirstName;

    @Column(value = "aum_gender")
    private String aumGender;

    @Column(value = "aum_phone_number")
    private String aumPhoneNumber;

    @Column(value = "aum_user_code")
    private String aumUserCode;

    @Column(value = "aum_status")
    private String aumStatus;

    @Column(value = "aum_country_id")
    private String aumCountryId;

    @Column(value = "aum_state_id")
    private String aumStateId;

    @Column(value = "aum_city_id")
    private String aumCityId;

    @Column(value = "aum_postal_code_id")
    private String aumPostalCodeId;

    @Column(value = "aum_nationality_id")
    private String aumNationalityId;

    @Column(value = "aum_allocated_location_id")
    private String aumAllocatedLocationId;

    @Column(value = "aum_key_responsibility_area_id")
    private String aumKeyResponsibilityAreaId;

    @Column(value = "aum_date_of_birth")
    private LocalDate aumDateOfBirth;

    @Column(value = "aum_address")
    private String aumAddress;

    @Column(value = "aum_profile_photo")
    private String aumProfilePhoto;

    @CreatedBy
    @Column(value = "aum_created_by")
    private String aumCreatedBy;

    @CreatedDate
    @Column(value = "aum_created_date")
    private LocalDateTime aumCreatedDate;

    @LastModifiedBy
    @Column(value = "aum_modified_by")
    private String aumModifiedBy;

    @LastModifiedDate
    @Column(value = "aum_modified_date")
    private LocalDateTime aumModifiedDate;
}
