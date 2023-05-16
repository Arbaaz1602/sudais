package com.alsudais.mappers.entity.pojo;

import com.alsudais.beans.response.UserMasterResponseBean;
import com.alsudais.entities.AlSudaisUserMaster;
import com.alsudais.utils.AlSudaisCommonUtils;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring")
public interface IAlSudaisUserMasterEntityPojoMapper {

    IAlSudaisUserMasterEntityPojoMapper INSTANCE = Mappers.getMapper(IAlSudaisUserMasterEntityPojoMapper.class);

    @Mappings({
            @Mapping(target = "userId", source = "alSudaisUserMaster.aumUserId"),
            @Mapping(target = "emailId", source = "alSudaisUserMaster.aumEmailId"),
            @Mapping(target = "lastName", source = "alSudaisUserMaster.aumLastName"),
            @Mapping(target = "middleName", source = "alSudaisUserMaster.aumMiddleName"),
            @Mapping(target = "firstName", source = "alSudaisUserMaster.aumFirstName"),
            @Mapping(target = "address", source = "alSudaisUserMaster.aumAddress"),
            @Mapping(target = "gender", source = "alSudaisUserMaster.aumGender"),
            @Mapping(target = "phoneNumber", source = "alSudaisUserMaster.aumPhoneNumber"),
            @Mapping(target = "userCode", source = "alSudaisUserMaster.aumUserCode"),
            @Mapping(target = "countryId", source = "alSudaisUserMaster.aumCountryId"),
            @Mapping(target = "stateId", source = "alSudaisUserMaster.aumStateId"),
            @Mapping(target = "cityId", source = "alSudaisUserMaster.aumCityId"),
            @Mapping(target = "postalCodeId", source = "alSudaisUserMaster.aumPostalCodeId"),
            @Mapping(target = "nationalityId", source = "alSudaisUserMaster.aumNationalityId"),
            @Mapping(target = "allocatedLocationId", source = "alSudaisUserMaster.aumAllocatedLocationId"),
            @Mapping(target = "keyResponsibilityAreaId", source = "alSudaisUserMaster.aumKeyResponsibilityAreaId"),
            @Mapping(target = "dateOfBirth", source = "alSudaisUserMaster.aumDateOfBirth"),
            @Mapping(target = "profilePhoto", source = "alSudaisUserMaster.aumProfilePhoto"),
            @Mapping(target = "status", source = "alSudaisUserMaster.aumStatus"),
    })
    public UserMasterResponseBean mapUserMasterEntityPojoMapping(AlSudaisUserMaster alSudaisUserMaster);

    public static BiFunction<Row, RowMetadata, UserMasterResponseBean> mapUserHistoricalData = (row, rowMetadata) ->  UserMasterResponseBean.builder()
            .emailId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_email_id", String.class))
            .lastName(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_last_name", String.class))
            .middleName(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_middle_name", String.class))
            .firstName(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_first_name", String.class))
            .address(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_address", String.class))
            .gender(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_gender", String.class))
            .phoneNumber(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_phone_number", String.class))
            .userCode(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_user_code", String.class))
            .countryId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_country_id", String.class))
            .stateId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_state_id", String.class))
            .cityId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_city_id", String.class))
            .postalCodeId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_postal_code_id", String.class))
            .nationalityId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_nationality_id", String.class))
            .allocatedLocationId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_allocated_location_id", String.class))
            .keyResponsibilityAreaId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_key_responsibility_area_id", String.class))
            .dateOfBirth(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_date_of_birth", LocalDate.class))
            .profilePhoto(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_profile_photo", String.class))
            .status(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_status", String.class))
            .addedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_created_date", LocalDateTime.class))
            .modifiedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aum_modified_date", LocalDateTime.class))
            .build();

}
