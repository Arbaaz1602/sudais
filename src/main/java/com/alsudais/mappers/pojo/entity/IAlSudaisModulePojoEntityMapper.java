package com.alsudais.mappers.pojo.entity;

import com.alsudais.beans.request.ModuleRequestBean;
import com.alsudais.entities.AlSudaisModuleDetail;
import com.alsudais.enums.ModuleStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, ModuleStatusEnum.class})
public interface IAlSudaisModulePojoEntityMapper {
    IAlSudaisModulePojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisModulePojoEntityMapper.class);

    @Mappings({
            @Mapping(target = "amdModuleId", source = "moduleRequestBean.moduleId"),
            @Mapping(target = "amdModuleName", source = "moduleRequestBean.moduleName"),
            @Mapping(target = "amdParentModuleId", source = "moduleRequestBean.parentModuleId"),
            @Mapping(target = "amdSortOrder", source = "moduleRequestBean.sortOrder"),
            @Mapping(target = "amdStatus", expression = "java(ModuleStatusEnum.ACTIVE.getValue())"),
            @Mapping(target = "amdCreatedBy", source = "moduleRequestBean.createdBy"),
            @Mapping(target = "amdCreatedDate", expression = "java(LocalDateTime.now())")
    })
    AlSudaisModuleDetail modulePojoToModuleEntity(ModuleRequestBean moduleRequestBean);

    public static BiFunction<ModuleRequestBean, AlSudaisModuleDetail, AlSudaisModuleDetail> updateModule = (moduleRequestBean, alSudaisModuleDetail) -> {
        alSudaisModuleDetail.setAmdModuleId(moduleRequestBean.getModuleId() != null ? moduleRequestBean.getModuleId() : alSudaisModuleDetail.getAmdModuleId());
        alSudaisModuleDetail.setAmdModuleName(moduleRequestBean.getModuleName() != null ? moduleRequestBean.getModuleName() : alSudaisModuleDetail.getAmdModuleName());
        alSudaisModuleDetail.setAmdParentModuleId(moduleRequestBean.getParentModuleId() != null ? moduleRequestBean.getParentModuleId() : alSudaisModuleDetail.getAmdParentModuleId());
        alSudaisModuleDetail.setAmdSortOrder(moduleRequestBean.getSortOrder() != null ? moduleRequestBean.getSortOrder() : alSudaisModuleDetail.getAmdSortOrder());
        alSudaisModuleDetail.setAmdStatus(moduleRequestBean.getStatus() != null ? moduleRequestBean.getStatus() : alSudaisModuleDetail.getAmdStatus());
        alSudaisModuleDetail.setAmdModifiedBy(moduleRequestBean.getModifiedBy() != null ? moduleRequestBean.getModifiedBy() : alSudaisModuleDetail.getAmdModifiedBy());
        alSudaisModuleDetail.setAmdModifiedDate(LocalDateTime.now());
        return alSudaisModuleDetail;
    };
}