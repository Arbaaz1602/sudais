package com.alsudais.entities.callbacks;

import com.alsudais.annotations.ReactiveAudited;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.enums.EntityCallBackTypeEnum;
import com.alsudais.utils.AlSudaisCommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.event.AfterDeleteCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AlSudaisAfterSaveEntityCallback implements AfterSaveCallback<Object>, AfterDeleteCallback<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String SEQUENCE_FETCH_QUERY = "nextval('public.revinfo_seq')";

    private static final String INSERT_INTO_STRING = "INSERT INTO ";
    private static final String VALUES_STRING = "VALUES";

    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Publisher<Object> onAfterSave(Object entity, OutboundRow outboundRow, SqlIdentifier table) {
        if (entity.getClass().isAnnotationPresent(ReactiveAudited.class))
            auditEntity(entity, EntityCallBackTypeEnum.AFTER_SAVE);

        return Mono.just(entity);
    }

    public Object onAfterDelete(Object entity) {
        if (entity.getClass().isAnnotationPresent(ReactiveAudited.class))
            auditEntity(entity, EntityCallBackTypeEnum.AFTER_DELETE);

        return entity;
    }

    private long computeRevTypeValue(LocalDateTime createdDate, LocalDateTime lastModifiedDate, EntityCallBackTypeEnum entityCallBackTypeEnum) {
        if (entityCallBackTypeEnum.getValue().equalsIgnoreCase(EntityCallBackTypeEnum.AFTER_DELETE.getValue()))
            return BigInteger.TWO.longValue();

        if (Objects.isNull(lastModifiedDate))
            return BigInteger.ZERO.longValue();

        return BigInteger.ONE.longValue();
    }


    private void auditEntity(Object entity, EntityCallBackTypeEnum entityCallBackTypeEnum) {
        Table tableAnnotation = entity.getClass().getAnnotation(Table.class);

        StringBuilder queryStringBuilder = new StringBuilder(INSERT_INTO_STRING.concat(tableAnnotation.value()
                        .concat(AlSudaisCommonConstants.INSTANCE.UNDERSCORE_AUD_STRING))
                .concat(AlSudaisCommonConstants.INSTANCE.OPEN_PARENTHESIS_STRING));

        StringBuilder queryParameterStringBuilder = new StringBuilder();

        AtomicReference<LocalDateTime> createdDateAtomicReference = new AtomicReference<>(null);
        AtomicReference<LocalDateTime> lastModifiedDateAtomicReference = new AtomicReference<>(null);
        Arrays.stream(entity.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(Boolean.TRUE);

            if (!field.isAnnotationPresent(Id.class)) {
                String columnName = field.isAnnotationPresent(Column.class) && StringUtils.isNotEmpty(field.getAnnotation(Column.class).value())
                        ? field.getAnnotation(Column.class).value()
                        : AlSudaisCommonUtils.INSTANCE.convertCamelCaseToLowerSnakeCase(field.getName());
                LOGGER.debug("Column Name :: {}", columnName);

                queryStringBuilder.append(columnName).append(AlSudaisCommonConstants.INSTANCE.COMMA_STRING);
                try {
                    if (field.isAnnotationPresent(CreatedDate.class)) {
                        LOGGER.debug("Created Date :: {}", field.get(entity));
                        if (Objects.nonNull(field.get(entity)))
                            createdDateAtomicReference.set((LocalDateTime) field.get(entity));
                    }
                    if (field.isAnnotationPresent(LastModifiedDate.class)) {
                        LOGGER.debug("LastModified Date :: {}", field.get(entity));
                        if (Objects.nonNull(field.get(entity)))
                            lastModifiedDateAtomicReference.set((LocalDateTime) field.get(entity));
                    }

                    switch (field.getType().getSimpleName().toUpperCase()) {
                        case "STRING", "LOCALDATETIME", "LOCALDATE", "LOCALTIME", "OBEJCT" -> {
                            if (Objects.nonNull(field.get(entity)))
                                queryParameterStringBuilder.append(AlSudaisCommonConstants.INSTANCE.SINGLEQUOTE_STRING).append(field.get(entity)).append(AlSudaisCommonConstants.INSTANCE.SINGLEQUOTE_STRING.concat(AlSudaisCommonConstants.INSTANCE.COMMA_STRING));
                            else
                                queryParameterStringBuilder.append(field.get(entity)).append(AlSudaisCommonConstants.INSTANCE.COMMA_STRING);
                        }
                        case "LONG", "INTEGER", "INT", "DOUBLE", "FLOAT" ->
                                queryParameterStringBuilder.append(field.get(entity)).append(AlSudaisCommonConstants.INSTANCE.COMMA_STRING);

                        default ->
                                queryParameterStringBuilder.append(field.get(entity)).append(AlSudaisCommonConstants.INSTANCE.COMMA_STRING);
                    }
                } catch (Exception e) {
                    LOGGER.error("Exception Occurred :: ", e);
                }
            }
        });

        queryStringBuilder.append("rev,revtype"
                .concat(AlSudaisCommonConstants.INSTANCE.CLOSE_PARENTHESIS_STRING)
                .concat(VALUES_STRING)
                .concat(AlSudaisCommonConstants.INSTANCE.OPEN_PARENTHESIS_STRING));

        long revTypeValue = computeRevTypeValue(createdDateAtomicReference.get(), lastModifiedDateAtomicReference.get(), entityCallBackTypeEnum);
        LOGGER.info("RevTypeValue :: {}", revTypeValue);

        queryParameterStringBuilder.append(SEQUENCE_FETCH_QUERY).append(AlSudaisCommonConstants.INSTANCE.COMMA_STRING).append(revTypeValue);
        queryStringBuilder.append(queryParameterStringBuilder);
        queryStringBuilder.append(AlSudaisCommonConstants.INSTANCE.CLOSE_PARENTHESIS_STRING);
        LOGGER.info("Query :: {}", queryStringBuilder);

        this.databaseClient.sql(queryStringBuilder.toString()).then().doOnError(error -> LOGGER.error("Error ::", error)).subscribe();

    }
}
