package com.alsudais.mappers.entity.pojo;

import com.alsudais.beans.response.UserCodeSequenceResponseBean;
import com.alsudais.entities.AlSudaisUserCodeSequence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IAlSudaisUserCodeSequenceEntityPojoMapper {

    IAlSudaisUserCodeSequenceEntityPojoMapper INSTANCE = Mappers.getMapper(IAlSudaisUserCodeSequenceEntityPojoMapper.class);

    @Mappings({
            @Mapping(target = "identifier", source = "alSudaisUserCodeSequence.aucsIdentifier"),
            @Mapping(target = "counter", source = "alSudaisUserCodeSequence.aucsCounter")
    })
    public UserCodeSequenceResponseBean mapUserCodeSequenceEntityPojoMapping(AlSudaisUserCodeSequence alSudaisUserCodeSequence);

}
