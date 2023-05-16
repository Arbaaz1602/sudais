package com.alsudais.mappers.entity.pojo;

import com.alsudais.beans.response.ModuleResponseBean;
import com.alsudais.entities.AlSudaisModuleDetail;
import com.alsudais.utils.AlSudaisCommonUtils;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IAlSudaisModuleEntityPojoMapper {

    public static Function<AlSudaisModuleDetail, ModuleResponseBean> mapModuleEntityPojoMapping = alSudaisModuleDetail -> ModuleResponseBean.builder()
            .moduleId(alSudaisModuleDetail.getAmdModuleId())
            .moduleName(alSudaisModuleDetail.getAmdModuleName())
            .parentModuleId(alSudaisModuleDetail.getAmdParentModuleId())
            .sortOrder(alSudaisModuleDetail.getAmdSortOrder())
            .status(alSudaisModuleDetail.getAmdStatus())
            .build();

    public static Function<List<AlSudaisModuleDetail>, List<ModuleResponseBean>> mapModuleEntityListtoPojoListMapping = alSudaisModuleDetailList -> alSudaisModuleDetailList.stream()
            .map(alSudaisModuleDetail -> {
                ModuleResponseBean.ModuleResponseBeanBuilder moduleResponseBeanBuilder = ModuleResponseBean.builder();
                moduleResponseBeanBuilder.moduleId(alSudaisModuleDetail.getAmdModuleId())
                        .moduleName(alSudaisModuleDetail.getAmdModuleName())
                        .parentModuleId(alSudaisModuleDetail.getAmdParentModuleId())
                        .sortOrder(alSudaisModuleDetail.getAmdSortOrder())
                        .status(alSudaisModuleDetail.getAmdStatus());
                return moduleResponseBeanBuilder.build();
            }).collect(Collectors.toList());

    public static BiFunction<Row, RowMetadata, ModuleResponseBean> mapModuleDetailHistoricalData = (row, rowMetadata) ->  ModuleResponseBean.builder()
            .moduleName(AlSudaisCommonUtils.INSTANCE.parseValue(row, "amd_module_name", String.class))
            .parentModuleId(AlSudaisCommonUtils.INSTANCE.parseValue(row, "amd_parent_module_id", String.class))
            .sortOrder(AlSudaisCommonUtils.INSTANCE.parseValue(row, "amd_sort_order", Integer.class))
            .status(AlSudaisCommonUtils.INSTANCE.parseValue(row, "amd_status", String.class))
            .addedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "amd_created_date", LocalDateTime.class))
            .modifiedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "amd_modified_date", LocalDateTime.class))
            .build();

}
