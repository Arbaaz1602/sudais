package com.alsudais.mappers.entity.pojo;

import com.alsudais.beans.response.RoleResponseBean;
import com.alsudais.entities.AlSudaisRoleDetail;
import com.alsudais.utils.AlSudaisCommonUtils;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IAlSudaisRoleEntityPojoMapper {

    public static Function<AlSudaisRoleDetail, RoleResponseBean> mapRoleEntityPojoMapping = alSudaisRoleDetail -> RoleResponseBean.builder()
            .roleId(alSudaisRoleDetail.getArdRoleId())
            .roleName(alSudaisRoleDetail.getArdRoleName())
            .platformName(alSudaisRoleDetail.getArdPlatformName())
            .status(alSudaisRoleDetail.getArdStatus())
            .build();

    public static Function<List<AlSudaisRoleDetail>, List<RoleResponseBean>> mapRoleEntityListtoPojoListMapping = alSudaisRoleDetailList -> alSudaisRoleDetailList.stream()
            .map(alSudaisRoleDetail -> {
                RoleResponseBean.RoleResponseBeanBuilder roleResponseBeanBuilder = RoleResponseBean.builder();
                roleResponseBeanBuilder.roleId(alSudaisRoleDetail.getArdRoleId())
                        .roleName(alSudaisRoleDetail.getArdRoleName())
                        .platformName(alSudaisRoleDetail.getArdPlatformName())
                        .status(alSudaisRoleDetail.getArdStatus());
                return roleResponseBeanBuilder.build();
            }).collect(Collectors.toList());

    public static BiFunction<Row, RowMetadata, RoleResponseBean> mapRoleHistoricalData = (row, rowMetadata) ->  RoleResponseBean.builder()
            .roleName(AlSudaisCommonUtils.INSTANCE.parseValue(row, "ard_role_name", String.class))
            .platformName(AlSudaisCommonUtils.INSTANCE.parseValue(row, "ard_platform_name", String.class))
            .status(AlSudaisCommonUtils.INSTANCE.parseValue(row, "ard_status", String.class))
            .addedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "ard_created_date", LocalDateTime.class))
            .modifiedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "ard_modified_date", LocalDateTime.class))
            .build();

}
