package com.alsudais.mappers.entity.pojo;


import com.alsudais.beans.response.UserOnlineStatusDetailResponseBean;
import com.alsudais.entities.AlSudaisUserOnlineStatusDetail;
import com.alsudais.utils.AlSudaisCommonUtils;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.time.LocalDateTime;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IAlSudaisUserOnlineStatusDetailEntityPojoMapper {

    public static Function<AlSudaisUserOnlineStatusDetail, UserOnlineStatusDetailResponseBean> mapUserOnlineStatusDetailEntityPojoMapping = alSudaisUserOnlineStatusDetail -> UserOnlineStatusDetailResponseBean.builder()
            .userId(alSudaisUserOnlineStatusDetail.getAuadUserId())
            .status(alSudaisUserOnlineStatusDetail.getAuadStatus())
            .build();

    public static BiFunction<Row, RowMetadata, UserOnlineStatusDetailResponseBean> mapUserOnlineStatusHistoricalData = (row, rowMetadata) ->  UserOnlineStatusDetailResponseBean.builder()
            .status(AlSudaisCommonUtils.INSTANCE.parseValue(row, "auad_status", String.class))
            .addedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "auad_created_date", LocalDateTime.class))
            .modifiedOn(AlSudaisCommonUtils.INSTANCE.parseValue(row, "auad_modified_date", LocalDateTime.class))
            .build();
}
