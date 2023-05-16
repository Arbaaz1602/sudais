package com.alsudais.mappers.pojo.entity;

import com.alsudais.beans.request.PlatformRequestBean;
import com.alsudais.entities.AlSudaisPlatformDetail;
import com.alsudais.enums.PlatformStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, PlatformStatusEnum.class})
public interface IAlSudaisPlatformPojoEntityMapper {

    IAlSudaisPlatformPojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisPlatformPojoEntityMapper.class);
    @Mappings({
            @Mapping(target = "apdPlatformId", source = "platformRequestBean.platformId"),
            @Mapping(target = "apdPlatformName",source = "platformRequestBean.platformName"),
            @Mapping(target = "apdStatus",expression = "java(PlatformStatusEnum.ACTIVE.getValue())"),
            @Mapping(target = "apdCreatedBy",source = "platformRequestBean.createdBy"),
            @Mapping(target = "apdCreatedDate",expression = "java(LocalDateTime.now())")
    })
    AlSudaisPlatformDetail platformPojoToRoleEntity(PlatformRequestBean platformRequestBean);

    public static BiFunction<PlatformRequestBean, AlSudaisPlatformDetail, AlSudaisPlatformDetail> updatePlatform = (platformRequestBean, alSudaisPlatformDetail) -> {
        alSudaisPlatformDetail.setApdPlatformId(platformRequestBean.getPlatformId() != null ? platformRequestBean.getPlatformId() : alSudaisPlatformDetail.getApdPlatformId());
        alSudaisPlatformDetail.setApdPlatformName(platformRequestBean.getPlatformName() != null ? platformRequestBean.getPlatformName() : alSudaisPlatformDetail.getApdPlatformName());
        alSudaisPlatformDetail.setApdStatus(platformRequestBean.getStatus() != null ? platformRequestBean.getStatus() : alSudaisPlatformDetail.getApdStatus());
        alSudaisPlatformDetail.setApdModifiedBy(platformRequestBean.getModifiedBy() != null ? platformRequestBean.getModifiedBy() : alSudaisPlatformDetail.getApdModifiedBy());
        alSudaisPlatformDetail.setApdModifiedDate(LocalDateTime.now());
        return alSudaisPlatformDetail;
    };

}
