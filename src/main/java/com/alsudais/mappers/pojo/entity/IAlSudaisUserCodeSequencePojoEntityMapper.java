package com.alsudais.mappers.pojo.entity;

import com.alsudais.beans.request.UserCodeSequenceRequestBean;
import com.alsudais.entities.AlSudaisUserCodeSequence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IAlSudaisUserCodeSequencePojoEntityMapper {


    IAlSudaisPlatformPojoEntityMapper INSTANCE = Mappers.getMapper(IAlSudaisPlatformPojoEntityMapper.class);
    @Mappings({
            @Mapping(target = "aucsIdentifier", source = "userCodeSequenceRequestBean.identifier"),
            @Mapping(target = "aucsCounter",source = "userCodeSequenceRequestBean.counter")
    })
    AlSudaisUserCodeSequence userRoleCodeSequencePojoToUserRoleCodeSequenceEntity(UserCodeSequenceRequestBean userCodeSequenceRequestBean);

}
