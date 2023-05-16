package com.alsudais.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum UserRoleStatusEnum {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    DELETED("DELETED");

    @Getter
    private String value;

    public static List<String> getAllValues(){
        return List.of(UserRoleStatusEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
    }
}