package com.alsudais.mappers.entity.pojo;

import com.alsudais.beans.response.UserRoleModuleResponseBean;
import com.alsudais.beans.response.UserRoleModuleSetResponseBean;
import com.alsudais.entities.AlSudaisModuleDetail;
import com.alsudais.entities.AlSudaisUserRoleModuleMapping;
import org.apache.commons.lang3.StringUtils;
import reactor.util.function.Tuple2;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IAlSudaisUserRoleModuleEntityPojoMapper {

//    public static Function<List<AlSudaisUserRoleModuleMapping>, UserRoleModuleResponseBean> mapUserRoleModuleEntityPojoListMapping = alSudaisUserRoleModuleMappingList ->
//        UserRoleModuleResponseBean.builder()
//                .userId(alSudaisUserRoleModuleMappingList.get(BigInteger.ZERO.intValue()).getAurmmUserId())
//                .roleId(alSudaisUserRoleModuleMappingList.get(BigInteger.ZERO.intValue()).getAurmmRoleId())
//                .userRoleModuleSetResponseBeans(alSudaisUserRoleModuleMappingList.stream().map(alSudaisUserRoleModuleMapping ->
//                    UserRoleModuleSetResponseBean.builder()
//                            .moduleId(alSudaisUserRoleModuleMapping.getAurmmModuleId())
//                            .parentModuleId(alSudaisUserRoleModuleMapping.getAurmmParentModuleId())
//                            .read(alSudaisUserRoleModuleMapping.getAurmmRead())
//                            .write(alSudaisUserRoleModuleMapping.getAurmmWrite())
//                            .fullControl(alSudaisUserRoleModuleMapping.getAurmmFullControl())
//                            .modify(alSudaisUserRoleModuleMapping.getAurmmModify())
//                            .noAccess(alSudaisUserRoleModuleMapping.getAurmmNoAccess())
//                            .approver(alSudaisUserRoleModuleMapping.getAurmmApprover())
//                            .readAndExecute(alSudaisUserRoleModuleMapping.getAurmmReadAndExecute())
//                            .remark(alSudaisUserRoleModuleMapping.getAurmmRemark())
//                            .status(alSudaisUserRoleModuleMapping.getAurmmStatus())
//                            .addedOn(alSudaisUserRoleModuleMapping.getAurmmModifiedDate())
//                            .lastUpdatedOn(alSudaisUserRoleModuleMapping.getAurmmModifiedDate())
//                            .lastUpdatedBy(alSudaisUserRoleModuleMapping.getAurmmModifiedBy())
//                            .build()
//                ).collect(Collectors.toSet()))
//                .build();

    public static Function<Tuple2<List<AlSudaisUserRoleModuleMapping>, Map<String, AlSudaisModuleDetail>>, UserRoleModuleResponseBean> mapUserRoleModuleEntityPojoListMapping = tuple2 -> {

        List<AlSudaisUserRoleModuleMapping> alSudaisUserRoleModuleMappingList = tuple2.getT1();
        Map<String, AlSudaisModuleDetail> alSudaisModuleDetailMap = tuple2.getT2();

        return UserRoleModuleResponseBean.builder()
                .userId(alSudaisUserRoleModuleMappingList.get(BigInteger.ZERO.intValue()).getAurmmUserId())
                .roleId(alSudaisUserRoleModuleMappingList.get(BigInteger.ZERO.intValue()).getAurmmRoleId())
                .userRoleModuleSetResponseBeans(alSudaisUserRoleModuleMappingList.stream().map(alSudaisUserRoleModuleMapping ->
                        UserRoleModuleSetResponseBean.builder()
                                .moduleId(alSudaisUserRoleModuleMapping.getAurmmModuleId())
                                .moduleName(StringUtils.isNotEmpty(alSudaisUserRoleModuleMapping.getAurmmModuleId()) ? alSudaisModuleDetailMap.get(alSudaisUserRoleModuleMapping.getAurmmModuleId()).getAmdModuleName() : null)
                                .parentModuleId(alSudaisUserRoleModuleMapping.getAurmmParentModuleId())
                                .parentModuleName(StringUtils.isNotEmpty(alSudaisUserRoleModuleMapping.getAurmmParentModuleId()) ? alSudaisModuleDetailMap.get(alSudaisUserRoleModuleMapping.getAurmmParentModuleId()).getAmdModuleName() : null)
                                .read(alSudaisUserRoleModuleMapping.getAurmmRead())
                                .write(alSudaisUserRoleModuleMapping.getAurmmWrite())
                                .fullControl(alSudaisUserRoleModuleMapping.getAurmmFullControl())
                                .modify(alSudaisUserRoleModuleMapping.getAurmmModify())
                                .noAccess(alSudaisUserRoleModuleMapping.getAurmmNoAccess())
                                .approver(alSudaisUserRoleModuleMapping.getAurmmApprover())
                                .readAndExecute(alSudaisUserRoleModuleMapping.getAurmmReadAndExecute())
                                .remark(alSudaisUserRoleModuleMapping.getAurmmRemark())
                                .status(alSudaisUserRoleModuleMapping.getAurmmStatus())
                                .addedOn(alSudaisUserRoleModuleMapping.getAurmmModifiedDate())
                                .lastUpdatedOn(alSudaisUserRoleModuleMapping.getAurmmModifiedDate())
                                .lastUpdatedBy(alSudaisUserRoleModuleMapping.getAurmmModifiedBy())
                                .build()
                ).collect(Collectors.toSet()))
                .build();
    };

    public static Function<Tuple2<List<AlSudaisUserRoleModuleMapping>, Map<String, AlSudaisModuleDetail>>, Set<UserRoleModuleSetResponseBean>> mapUserRoleModuleEntityPojoSetMapping = tuple2 -> {
        List<AlSudaisUserRoleModuleMapping> alSudaisUserRoleModuleMappingList = tuple2.getT1();
        Map<String, AlSudaisModuleDetail> alSudaisModuleDetailMap = tuple2.getT2();

        return alSudaisUserRoleModuleMappingList.stream().map(alSudaisUserRoleModuleMapping ->
                UserRoleModuleSetResponseBean.builder()
                        .moduleId(alSudaisUserRoleModuleMapping.getAurmmModuleId())
                        .moduleName(StringUtils.isNotEmpty(alSudaisUserRoleModuleMapping.getAurmmModuleId()) ? alSudaisModuleDetailMap.get(alSudaisUserRoleModuleMapping.getAurmmModuleId()).getAmdModuleName() : null)
                        .parentModuleId(alSudaisUserRoleModuleMapping.getAurmmParentModuleId())
                        .parentModuleName(StringUtils.isNotEmpty(alSudaisUserRoleModuleMapping.getAurmmParentModuleId()) ? alSudaisModuleDetailMap.get(alSudaisUserRoleModuleMapping.getAurmmParentModuleId()).getAmdModuleName() : null)
                        .read(alSudaisUserRoleModuleMapping.getAurmmRead())
                        .write(alSudaisUserRoleModuleMapping.getAurmmWrite())
                        .fullControl(alSudaisUserRoleModuleMapping.getAurmmFullControl())
                        .modify(alSudaisUserRoleModuleMapping.getAurmmModify())
                        .noAccess(alSudaisUserRoleModuleMapping.getAurmmNoAccess())
                        .approver(alSudaisUserRoleModuleMapping.getAurmmApprover())
                        .readAndExecute(alSudaisUserRoleModuleMapping.getAurmmReadAndExecute())
                        .remark(alSudaisUserRoleModuleMapping.getAurmmRemark())
                        .status(alSudaisUserRoleModuleMapping.getAurmmStatus())
                        .addedOn(alSudaisUserRoleModuleMapping.getAurmmModifiedDate())
                        .lastUpdatedOn(alSudaisUserRoleModuleMapping.getAurmmModifiedDate())
                        .lastUpdatedBy(alSudaisUserRoleModuleMapping.getAurmmModifiedBy())
                        .build()
        ).collect(Collectors.toSet());
    };


    public static Function<List<AlSudaisUserRoleModuleMapping>, Set<UserRoleModuleResponseBean>> mapUserRoleModuleEntityPojoMapping = alSudaisUserRoleModuleMappingList -> {

//        Map<String, Map<String, List<AlSudaisUserRoleModuleMapping>>> collect = alSudaisUserRoleModuleMappingList.stream().collect(Collectors.groupingBy(data -> StringUtils.isNotEmpty(data.getAurmmRoleId()) ? data.getAurmmRoleId() : "ROLE", Collectors.groupingBy(data -> StringUtils.isNotEmpty(data.getAurmmUserId()) ? data.getAurmmUserId() : "USER")));
//        System.err.println("Collect :: " + collect);
        return Collections.singleton((UserRoleModuleResponseBean.builder()
                .userId(alSudaisUserRoleModuleMappingList.get(BigInteger.ZERO.intValue()).getAurmmUserId())
                .roleId(alSudaisUserRoleModuleMappingList.get(BigInteger.ZERO.intValue()).getAurmmRoleId())
                .userRoleModuleSetResponseBeans(alSudaisUserRoleModuleMappingList.stream().map(alSudaisUserRoleModuleMapping ->
                        UserRoleModuleSetResponseBean.builder()
                                .moduleId(alSudaisUserRoleModuleMapping.getAurmmModuleId())
                                .parentModuleId(alSudaisUserRoleModuleMapping.getAurmmParentModuleId())
                                .read(alSudaisUserRoleModuleMapping.getAurmmRead())
                                .write(alSudaisUserRoleModuleMapping.getAurmmWrite())
                                .fullControl(alSudaisUserRoleModuleMapping.getAurmmFullControl())
                                .modify(alSudaisUserRoleModuleMapping.getAurmmModify())
                                .noAccess(alSudaisUserRoleModuleMapping.getAurmmNoAccess())
                                .approver(alSudaisUserRoleModuleMapping.getAurmmApprover())
                                .readAndExecute(alSudaisUserRoleModuleMapping.getAurmmReadAndExecute())
                                .remark(alSudaisUserRoleModuleMapping.getAurmmRemark())
                                .status(alSudaisUserRoleModuleMapping.getAurmmStatus())
                                .addedOn(alSudaisUserRoleModuleMapping.getAurmmModifiedDate())
                                .lastUpdatedOn(alSudaisUserRoleModuleMapping.getAurmmModifiedDate())
                                .lastUpdatedBy(alSudaisUserRoleModuleMapping.getAurmmModifiedBy())
                                .build()

                ).collect(Collectors.toSet()))
                .build()));

    };
}
