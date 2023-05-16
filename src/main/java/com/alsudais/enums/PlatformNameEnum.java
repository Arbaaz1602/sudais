package com.alsudais.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum PlatformNameEnum {

    ADMIN_BACKEND_PANEL("ADMIN_BACKEND_PANEL");
    @Getter
    private String value;

    public static List<String> getAllValues() {
        return List.of(PlatformNameEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
    }

}
