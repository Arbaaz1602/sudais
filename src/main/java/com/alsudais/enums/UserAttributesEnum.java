package com.alsudais.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum UserAttributesEnum {

    ROLE("Role"),
    USER_ID("UserId")
    ;

    @Getter
    private String value;

    public static List<String> getAllValues() {
        return List.of(UserAttributesEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
    }
}
