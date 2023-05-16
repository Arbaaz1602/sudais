package com.alsudais.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum CacheKeyEnum {
    FORGOT_PASSWORD("FORGOT_PASSWORD_");
    @Getter
    private String value;

    public static List<String> getAllValues() {
        return List.of(CacheKeyEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
    }
}