package com.alsudais.mappers.entity.pojo;

import com.alsudais.beans.response.PlatformResponseBean;
import com.alsudais.entities.AlSudaisPlatformDetail;
import com.alsudais.utils.AlSudaisCommonUtils;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IAlSudaisPlatformEntityPojoMapper {

    public static Function<AlSudaisPlatformDetail, PlatformResponseBean> mapPlatformEntityPojoMapping = alSudaisPlatformDetail -> PlatformResponseBean.builder()
            .platformId(alSudaisPlatformDetail.getApdPlatformId())
            .platformName(alSudaisPlatformDetail.getApdPlatformName())
            .status(alSudaisPlatformDetail.getApdStatus())
            .build();

    public static Function<List<AlSudaisPlatformDetail>, List<PlatformResponseBean>> mapPlatformEntityListtoPojoListMapping = alSudaisPlatformList -> alSudaisPlatformList.stream()
            .map(alSudaisPlatformDetail -> {
                PlatformResponseBean.PlatformResponseBeanBuilder platformResponseBeanBuilder = PlatformResponseBean.builder();
                platformResponseBeanBuilder.platformId(alSudaisPlatformDetail.getApdPlatformId())
                        .platformName(alSudaisPlatformDetail.getApdPlatformName())
                        .status(alSudaisPlatformDetail.getApdStatus())
                        .addedOn(alSudaisPlatformDetail.getApdCreatedDate())
                        .modifiedOn(alSudaisPlatformDetail.getApdModifiedDate());
                return platformResponseBeanBuilder.build();
            }).collect(Collectors.toList());


    public static BiFunction<Row, RowMetadata, PlatformResponseBean> mapPlatformHistoricalData = (row, rowMetadata) ->  PlatformResponseBean.builder()
            .platformName(AlSudaisCommonUtils.INSTANCE.parseValue(row, "apd_platform_name", String.class))
            .status(AlSudaisCommonUtils.INSTANCE.parseValue(row, "apd_status", String.class))
            .addedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "apd_created_date", LocalDateTime.class))
            .modifiedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "apd_modified_date", LocalDateTime.class))
            .build();

}
