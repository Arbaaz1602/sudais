package com.alsudais.mappers.pojo.entity;

import com.alsudais.beans.request.UserOnlineStatusDetailRequestBean;
import com.alsudais.entities.AlSudaisUserOnlineStatusDetail;
import com.alsudais.enums.UserOnlineStatusDetailEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, UserOnlineStatusDetailEnum.class})
public interface IAlSudaisUserOnlineStatusDetailPojoEntityMapper {

    IAlSudaisUserOnlineStatusDetailPojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisUserOnlineStatusDetailPojoEntityMapper.class);

    @Mappings({
            @Mapping(target = "auadUserId", source = "userOnlineStatusDetailRequestBean.userId"),
            @Mapping(target = "auadStatus", expression = "java(UserOnlineStatusDetailEnum.ACTIVE.getValue())"),
            @Mapping(target = "auadCreatedBy", source = "userOnlineStatusDetailRequestBean.createdBy"),
            @Mapping(target = "auadCreatedDate", expression = "java(LocalDateTime.now())")
    })
    AlSudaisUserOnlineStatusDetail userOnlineStatusDetailPojoToRoleEntity(UserOnlineStatusDetailRequestBean userOnlineStatusDetailRequestBean);

    public static BiFunction<UserOnlineStatusDetailRequestBean, AlSudaisUserOnlineStatusDetail, AlSudaisUserOnlineStatusDetail> updateUserOnlineStatusDetail = (userOnlineStatusDetailRequestBean, alSudaisUserOnlineStatusDetail) -> {
        alSudaisUserOnlineStatusDetail.setAuadUserId(userOnlineStatusDetailRequestBean.getUserId() != null ? userOnlineStatusDetailRequestBean.getUserId() : alSudaisUserOnlineStatusDetail.getAuadUserId());
        alSudaisUserOnlineStatusDetail.setAuadStatus(userOnlineStatusDetailRequestBean.getStatus() != null ? userOnlineStatusDetailRequestBean.getStatus() : alSudaisUserOnlineStatusDetail.getAuadStatus());
        alSudaisUserOnlineStatusDetail.setAuadModifiedBy(userOnlineStatusDetailRequestBean.getModifiedBy() != null ? userOnlineStatusDetailRequestBean.getModifiedBy() : alSudaisUserOnlineStatusDetail.getAuadModifiedBy());
        alSudaisUserOnlineStatusDetail.setAuadModifiedDate(LocalDateTime.now());
        return alSudaisUserOnlineStatusDetail;
    };

}
