package com.alsudais.mappers.pojo.entity;

import com.alsudais.beans.request.RoleRequestBean;
import com.alsudais.beans.request.SignUpRequestBean;
import com.alsudais.beans.request.UserMasterRequestBean;
import com.alsudais.entities.AlSudaisRoleDetail;
import com.alsudais.entities.AlSudaisUserMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface IAlSudaisUserMasterPojoEntityMapper {

    IAlSudaisUserMasterPojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisUserMasterPojoEntityMapper.class);
    @Mappings({
            @Mapping(target = "aumEmailId", source = "signUpRequestBean.emailId"),
            @Mapping(target = "aumLastName", source = "signUpRequestBean.lastName"),
            @Mapping(target = "aumMiddleName", source = "signUpRequestBean.middleName"),
            @Mapping(target = "aumFirstName", source = "signUpRequestBean.firstName"),
            @Mapping(target = "aumAddress", source = "signUpRequestBean.address"),
            @Mapping(target = "aumGender",source = "signUpRequestBean.gender"),
            @Mapping(target = "aumPhoneNumber", source = "signUpRequestBean.phoneNumber"),
            @Mapping(target = "aumUserCode", source = "signUpRequestBean.tenantId"),
            @Mapping(target = "aumCountryId", source = "signUpRequestBean.countryId"),
            @Mapping(target = "aumStateId", source = "signUpRequestBean.stateId"),
            @Mapping(target = "aumCityId", source = "signUpRequestBean.cityId"),
            @Mapping(target = "aumPostalCodeId", source = "signUpRequestBean.postalCodeId"),
            @Mapping(target = "aumNationalityId", source = "signUpRequestBean.nationalityId"),
            @Mapping(target = "aumAllocatedLocationId", source = "signUpRequestBean.allocatedLocationId"),
            @Mapping(target = "aumKeyResponsibilityAreaId", source = "signUpRequestBean.keyResponsibilityAreaId"),
            @Mapping(target = "aumDateOfBirth", source = "signUpRequestBean.dateOfBirth"),
            @Mapping(target = "aumStatus", source = "signUpRequestBean.status"),
            @Mapping(target = "aumCreatedBy", source = "signUpRequestBean.createdBy"),
            @Mapping(target = "aumCreatedDate", expression = "java(LocalDateTime.now())")
    })
    public AlSudaisUserMaster userMasterPojoToUserMasterEntity(SignUpRequestBean signUpRequestBean);

    public static BiFunction<UserMasterRequestBean, AlSudaisUserMaster, AlSudaisUserMaster> updateUser = (userMasterRequestBean, alSudaisUserMaster) -> {
        alSudaisUserMaster.setAumUserId(userMasterRequestBean.getUserId() != null ? userMasterRequestBean.getUserId() : alSudaisUserMaster.getAumUserId());
        alSudaisUserMaster.setAumLastName(userMasterRequestBean.getLastName() != null ? userMasterRequestBean.getLastName() : alSudaisUserMaster.getAumLastName());
        alSudaisUserMaster.setAumMiddleName(userMasterRequestBean.getMiddleName() != null ? userMasterRequestBean.getMiddleName() : alSudaisUserMaster.getAumMiddleName());
        alSudaisUserMaster.setAumFirstName(userMasterRequestBean.getFirstName() != null ? userMasterRequestBean.getFirstName() : alSudaisUserMaster.getAumFirstName());
        alSudaisUserMaster.setAumAddress(userMasterRequestBean.getAddress() != null ? userMasterRequestBean.getAddress() : alSudaisUserMaster.getAumAddress());
        alSudaisUserMaster.setAumGender(userMasterRequestBean.getGender() != null ? userMasterRequestBean.getGender() : alSudaisUserMaster.getAumGender());
        alSudaisUserMaster.setAumCountryId(userMasterRequestBean.getCountryId() != null ? userMasterRequestBean.getCountryId() : alSudaisUserMaster.getAumCountryId());
        alSudaisUserMaster.setAumStateId(userMasterRequestBean.getStateId() != null ? userMasterRequestBean.getStateId() : alSudaisUserMaster.getAumStateId());
        alSudaisUserMaster.setAumCityId(userMasterRequestBean.getCityId() != null ? userMasterRequestBean.getCityId() : alSudaisUserMaster.getAumCityId());
        alSudaisUserMaster.setAumPostalCodeId(userMasterRequestBean.getPostalCodeId() != null ? userMasterRequestBean.getPostalCodeId() : alSudaisUserMaster.getAumPostalCodeId());
        alSudaisUserMaster.setAumNationalityId(userMasterRequestBean.getNationalityId() != null ? userMasterRequestBean.getNationalityId() : alSudaisUserMaster.getAumNationalityId());
        alSudaisUserMaster.setAumAllocatedLocationId(userMasterRequestBean.getAllocatedLocationId() != null ? userMasterRequestBean.getAllocatedLocationId() : alSudaisUserMaster.getAumAllocatedLocationId());
        alSudaisUserMaster.setAumKeyResponsibilityAreaId(userMasterRequestBean.getKeyResponsibilityAreaId() != null ? userMasterRequestBean.getKeyResponsibilityAreaId() : alSudaisUserMaster.getAumKeyResponsibilityAreaId());
        alSudaisUserMaster.setAumDateOfBirth(userMasterRequestBean.getDateOfBirth() != null ? userMasterRequestBean.getDateOfBirth() : alSudaisUserMaster.getAumDateOfBirth());
        alSudaisUserMaster.setAumProfilePhoto(userMasterRequestBean.getProfilePhoto() != null ? userMasterRequestBean.getProfilePhoto() : alSudaisUserMaster.getAumProfilePhoto());
        alSudaisUserMaster.setAumStatus(userMasterRequestBean.getStatus() != null ? userMasterRequestBean.getStatus() : alSudaisUserMaster.getAumStatus());
        alSudaisUserMaster.setAumModifiedBy(userMasterRequestBean.getModifiedBy() != null ? userMasterRequestBean.getModifiedBy() : alSudaisUserMaster.getAumModifiedBy());
        alSudaisUserMaster.setAumModifiedDate(LocalDateTime.now());
        return alSudaisUserMaster;
    };

}
