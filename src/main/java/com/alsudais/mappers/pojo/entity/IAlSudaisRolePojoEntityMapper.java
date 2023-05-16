package com.alsudais.mappers.pojo.entity;


import com.alsudais.beans.request.RoleRequestBean;
import com.alsudais.entities.AlSudaisRoleDetail;
import com.alsudais.enums.RoleStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, RoleStatusEnum.class})
public interface IAlSudaisRolePojoEntityMapper {
    IAlSudaisRolePojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisRolePojoEntityMapper.class);
    @Mappings({
            @Mapping(target = "ardRoleId", source = "roleRequestBean.roleId"),
            @Mapping(target = "ardRoleName",source = "roleRequestBean.roleName"),
            @Mapping(target = "ardPlatformName",source = "roleRequestBean.platformName"),
            @Mapping(target = "ardStatus",expression = "java(RoleStatusEnum.ACTIVE.getValue())"),
            @Mapping(target = "ardCreatedBy",source = "roleRequestBean.createdBy"),
            @Mapping(target = "ardCreatedDate",expression = "java(LocalDateTime.now())")
    })
    AlSudaisRoleDetail rolePojoToRoleEntity(RoleRequestBean roleRequestBean);
    public static BiFunction<RoleRequestBean, AlSudaisRoleDetail, AlSudaisRoleDetail> updateRole = (roleRequestBean, alSudaisRoleDetail) -> {
        alSudaisRoleDetail.setArdRoleId(roleRequestBean.getRoleId() != null ? roleRequestBean.getRoleId() : alSudaisRoleDetail.getArdRoleId());
        alSudaisRoleDetail.setArdRoleName(roleRequestBean.getRoleName() != null ? roleRequestBean.getRoleName() : alSudaisRoleDetail.getArdRoleName());
        alSudaisRoleDetail.setArdPlatformName(roleRequestBean.getPlatformName() != null ? roleRequestBean.getPlatformName() : alSudaisRoleDetail.getArdPlatformName());
        alSudaisRoleDetail.setArdStatus(roleRequestBean.getStatus() != null ? roleRequestBean.getStatus() : alSudaisRoleDetail.getArdStatus());
        alSudaisRoleDetail.setArdModifiedBy(roleRequestBean.getModifiedBy() != null ? roleRequestBean.getModifiedBy() : alSudaisRoleDetail.getArdModifiedBy());
        alSudaisRoleDetail.setArdModifiedDate(LocalDateTime.now());
        return alSudaisRoleDetail;
    };
}
