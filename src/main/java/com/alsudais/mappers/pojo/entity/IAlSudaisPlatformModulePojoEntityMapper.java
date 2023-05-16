package com.alsudais.mappers.pojo.entity;

import com.alsudais.beans.request.PlatformModuleRequestBean;
import com.alsudais.entities.AlSudaisPlatformModuleMapping;
import com.alsudais.enums.PlatformModuleStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, PlatformModuleStatusEnum.class})
public interface IAlSudaisPlatformModulePojoEntityMapper {

    IAlSudaisPlatformModulePojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisPlatformModulePojoEntityMapper.class);

    public static Function<PlatformModuleRequestBean, List<AlSudaisPlatformModuleMapping>> addPlatformModuleMapping = platformModuleRequestBean ->
            platformModuleRequestBean.getModuleIds().parallelStream().map(moduleId ->
                    AlSudaisPlatformModuleMapping.builder()
                            .apmmPlatformId(platformModuleRequestBean.getPlatformId())
                            .apmmModuleId(moduleId)
                            .apmmCreatedBy(platformModuleRequestBean.getCreatedBy())
                            .apmmCreatedDate(LocalDateTime.now())
                            .apmmStatus(platformModuleRequestBean.getStatus())
                            .build()
            ).collect(Collectors.toList());

    public static Function<PlatformModuleRequestBean, List<AlSudaisPlatformModuleMapping>> updatePlatformModuleMapping = platformModuleRequestBean ->
            platformModuleRequestBean.getModuleIds().parallelStream().map(moduleId ->
                    AlSudaisPlatformModuleMapping.builder()
                            .apmmPlatformId(platformModuleRequestBean.getPlatformId())
                            .apmmModuleId(moduleId)
                            .apmmCreatedBy(platformModuleRequestBean.getCreatedBy())
                            .apmmCreatedDate(LocalDateTime.now())
                            .apmmStatus(platformModuleRequestBean.getStatus())
                            .apmmModifiedBy(platformModuleRequestBean.getModifiedBy())
                            .apmmModifiedDate(LocalDateTime.now())
                            .build()
            ).collect(Collectors.toList());

}
