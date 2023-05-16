package com.alsudais.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum UserStatusEnum {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    SUSPEND("SUSPEND"),
    DELETE("DELETE"),
    ;

    @Getter
    private String value;

    public static List<String> getAllValues() {
        return List.of(UserStatusEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
    }
}
