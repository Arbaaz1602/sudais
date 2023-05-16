package com.alsudais.mappers.pojo.entity;

import com.alsudais.beans.request.UserRoleRequestBean;
import com.alsudais.entities.AlSudaisUserRoleMapping;
import com.alsudais.enums.UserRoleStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", imports = {LocalDateTime.class, UserRoleStatusEnum.class})
public interface IAlSudaisUserRolePojoEntityMapper {

    IAlSudaisUserRolePojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisUserRolePojoEntityMapper.class);

    public static Function<UserRoleRequestBean, List<AlSudaisUserRoleMapping>> addUserRoleMapping = userRoleRequestBean ->
        userRoleRequestBean.getRoleIds().parallelStream().map(roleId ->
            AlSudaisUserRoleMapping.builder()
                    .aurmUserId(userRoleRequestBean.getUserId())
                    .aurmRoleId(roleId)
                    .aurmCreatedBy(userRoleRequestBean.getCreatedBy())
                    .aurmCreatedDate(LocalDateTime.now())
                    .aurmStatus(userRoleRequestBean.getStatus())
                    .build()
        ).collect(Collectors.toList());

    public static Function<UserRoleRequestBean, List<AlSudaisUserRoleMapping>> updateUserRoleMapping = userRoleRequestBean ->
            userRoleRequestBean.getRoleIds().parallelStream().map(roleId ->
                    AlSudaisUserRoleMapping.builder()
                            .aurmUserId(userRoleRequestBean.getUserId())
                            .aurmRoleId(roleId)
                            .aurmCreatedBy(userRoleRequestBean.getCreatedBy())
                            .aurmCreatedDate(LocalDateTime.now())
                            .aurmStatus(userRoleRequestBean.getStatus())
                            .build()
            ).collect(Collectors.toList());

}