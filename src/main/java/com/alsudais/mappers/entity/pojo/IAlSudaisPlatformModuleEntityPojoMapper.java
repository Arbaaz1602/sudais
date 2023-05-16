package com.alsudais.mappers.entity.pojo;

import com.alsudais.beans.response.PlatformModuleResponseBean;
import com.alsudais.entities.AlSudaisPlatformModuleMapping;
import com.alsudais.utils.AlSudaisCommonUtils;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IAlSudaisPlatformModuleEntityPojoMapper {

    public static Function<List<AlSudaisPlatformModuleMapping>, PlatformModuleResponseBean> mapPlatformModuleEntityPojoMapping = alSudaisPlatformModuleMappingList ->
            PlatformModuleResponseBean.builder()
                    .platformId(alSudaisPlatformModuleMappingList.get(BigInteger.ZERO.intValue()).getApmmPlatformId())
                    .moduleIds(alSudaisPlatformModuleMappingList.parallelStream().map(AlSudaisPlatformModuleMapping::getApmmModuleId).collect(Collectors.toSet()))
                    .build();

    public static BiFunction<Row, RowMetadata, PlatformModuleResponseBean> mapPlatformModuleHistoricalData = (row, rowMetadata) ->  PlatformModuleResponseBean.builder()
            .moduleIds(Set.of(AlSudaisCommonUtils.INSTANCE.parseValue(row, "apmm_module_id", String.class)))
            .addedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "apmm_created_date", LocalDateTime.class))
            .modifiedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "apmm_modified_date", LocalDateTime.class))
            .build();

}
