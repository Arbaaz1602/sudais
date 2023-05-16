package com.alsudais.mappers.pojo.entity;

import com.alsudais.beans.request.UserRoleModuleRequestBean;
import com.alsudais.entities.AlSudaisUserRoleModuleMapping;
import com.alsudais.enums.UserRoleModuleStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, UserRoleModuleStatusEnum.class})
public interface IAlSudaisUserRoleModulePojoEntityMapper {

    IAlSudaisUserRoleModulePojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisUserRoleModulePojoEntityMapper.class);
    public static Function<UserRoleModuleRequestBean, List<AlSudaisUserRoleModuleMapping>> userRoleModulePojoToUserRoleModuleEntity = userRoleModuleRequestBean ->
            userRoleModuleRequestBean.getUserRoleModuleSetRequestBeans()
                    .parallelStream()
                    .map(userRoleModuleSetRequestBean -> {
                        AlSudaisUserRoleModuleMapping.AlSudaisUserRoleModuleMappingBuilder alSudaisUserRoleModuleMapping = AlSudaisUserRoleModuleMapping
                                .builder()
                                .aurmmRoleId(userRoleModuleRequestBean.getRoleId())
                                .aurmmUserId(userRoleModuleRequestBean.getUserId())
                                .aurmmParentModuleId(userRoleModuleSetRequestBean.getParentModuleId())
                                .aurmmModuleId(userRoleModuleSetRequestBean.getModuleId())
                                .aurmmFullControl(userRoleModuleSetRequestBean.getFullControl())
                                .aurmmNoAccess(userRoleModuleSetRequestBean.getNoAccess())
                                .aurmmRemark(userRoleModuleRequestBean.getRemark())
                                .aurmmStatus(userRoleModuleRequestBean.getStatus())
                                .aurmmCreatedBy(userRoleModuleRequestBean.getCreatedBy())
                                .aurmmCreatedDate(userRoleModuleRequestBean.getCreatedDate())
                                .aurmmModifiedBy(userRoleModuleRequestBean.getModifiedBy())
                                .aurmmModifiedDate(userRoleModuleRequestBean.getModifiedDate());

                        if (userRoleModuleSetRequestBean.getFullControl())
                            alSudaisUserRoleModuleMapping.aurmmRead(Boolean.TRUE)
                                    .aurmmApprover(Boolean.TRUE)
                                    .aurmmModify(Boolean.TRUE)
                                    .aurmmReadAndExecute(Boolean.TRUE)
                                    .aurmmFullControl(Boolean.TRUE)
                                    .aurmmWrite(Boolean.TRUE)
                                    .aurmmNoAccess(Boolean.FALSE);
                        else if (userRoleModuleSetRequestBean.getNoAccess())
                            alSudaisUserRoleModuleMapping.aurmmRead(Boolean.FALSE)
                                    .aurmmApprover(Boolean.FALSE)
                                    .aurmmModify(Boolean.FALSE)
                                    .aurmmReadAndExecute(Boolean.FALSE)
                                    .aurmmWrite(Boolean.FALSE)
                                    .aurmmFullControl(Boolean.FALSE);
                        else
                            alSudaisUserRoleModuleMapping.aurmmRead(userRoleModuleSetRequestBean.getRead())
                                    .aurmmReadAndExecute(userRoleModuleSetRequestBean.getReadAndExecute())
                                    .aurmmModify(userRoleModuleSetRequestBean.getModify())
                                    .aurmmWrite(userRoleModuleSetRequestBean.getWrite())
                                    .aurmmRead(userRoleModuleSetRequestBean.getRead())
                                    .aurmmApprover(userRoleModuleSetRequestBean.getApprover());

                        return alSudaisUserRoleModuleMapping.build();
                    })
                    .collect(Collectors.toList());


    public static BiFunction<UserRoleModuleRequestBean, AlSudaisUserRoleModuleMapping, AlSudaisUserRoleModuleMapping> updateUserRoleModule = (userRoleModuleRequestBean, alSudaisUserRoleModuleMapping) -> {
        alSudaisUserRoleModuleMapping.setAurmmUserId(userRoleModuleRequestBean.getUserId() != null ? userRoleModuleRequestBean.getUserId() : alSudaisUserRoleModuleMapping.getAurmmUserId());
        alSudaisUserRoleModuleMapping.setAurmmRoleId(userRoleModuleRequestBean.getRoleId() != null ? userRoleModuleRequestBean.getRoleId() : alSudaisUserRoleModuleMapping.getAurmmRoleId());
        alSudaisUserRoleModuleMapping.setAurmmRemark(userRoleModuleRequestBean.getRemark() != null ? userRoleModuleRequestBean.getRemark() : alSudaisUserRoleModuleMapping.getAurmmRemark());
        alSudaisUserRoleModuleMapping.setAurmmStatus(userRoleModuleRequestBean.getStatus() != null ? userRoleModuleRequestBean.getStatus() : alSudaisUserRoleModuleMapping.getAurmmStatus());
        alSudaisUserRoleModuleMapping.setAurmmModifiedBy(userRoleModuleRequestBean.getModifiedBy() != null ? userRoleModuleRequestBean.getModifiedBy() : alSudaisUserRoleModuleMapping.getAurmmModifiedBy());
        alSudaisUserRoleModuleMapping.setAurmmModifiedDate(LocalDateTime.now());
        return alSudaisUserRoleModuleMapping;
    };
}