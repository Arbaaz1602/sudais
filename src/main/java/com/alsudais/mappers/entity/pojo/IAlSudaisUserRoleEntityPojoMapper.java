package com.alsudais.mappers.entity.pojo;

import com.alsudais.beans.response.UserOnlineStatusDetailResponseBean;
import com.alsudais.beans.response.UserRoleResponseBean;
import com.alsudais.entities.AlSudaisUserRoleMapping;
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

public interface IAlSudaisUserRoleEntityPojoMapper {

    public static Function<List<AlSudaisUserRoleMapping>, UserRoleResponseBean> mapUserRoleEntityPojoMapping = alSudaisUserRoleMappingList ->
            UserRoleResponseBean.builder()
            .userId(alSudaisUserRoleMappingList.get(BigInteger.ZERO.intValue()).getAurmUserId())
            .roleIds(alSudaisUserRoleMappingList.parallelStream().map(AlSudaisUserRoleMapping::getAurmRoleId).collect(Collectors.toSet()))
            .build();


    public static Function<List<AlSudaisUserRoleMapping>, List<UserRoleResponseBean>> mapUserRoleEntityListPojoListMapping = alSudaisUserRoleMappingList -> alSudaisUserRoleMappingList.parallelStream()
                .collect(Collectors.groupingBy(AlSudaisUserRoleMapping::getAurmUserId))
                .entrySet()
                .parallelStream()
                .map(entry ->
                    UserRoleResponseBean.builder()
                            .userId(entry.getKey())
                            .roleIds(entry.getValue().parallelStream().map(AlSudaisUserRoleMapping::getAurmRoleId).collect(Collectors.toSet()))
                            .build()
                ).collect(Collectors.toList());

    public static Function<List<AlSudaisUserRoleMapping>, List<UserRoleResponseBean>> mapUserRoleEntityListtoPojoListMapping = alSudaisUserRoleMappingList -> alSudaisUserRoleMappingList.stream()
            .map(alSudaisUserRoleMapping -> {
                UserRoleResponseBean.UserRoleResponseBeanBuilder userRoleResponseBeanBuilder = UserRoleResponseBean.builder();
                userRoleResponseBeanBuilder
                        .userId(alSudaisUserRoleMapping.getAurmUserId());
//                        .roleIds(alSudaisUserRoleMapping.getAurmRoleIds())
//                        .status(alSudaisUserRoleMapping.getAurmStatus());
                return userRoleResponseBeanBuilder.build();
            }).collect(Collectors.toList());

    public static BiFunction<Row, RowMetadata, UserRoleResponseBean> mapUserRoleHistoricalData = (row, rowMetadata) ->  UserRoleResponseBean.builder()
            .roleIds(Set.of(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aurm_role_id", String.class)))
            .addedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aurm_created_date", LocalDateTime.class))
            .modifiedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "aurm_modified_date", LocalDateTime.class))
            .build();

}
