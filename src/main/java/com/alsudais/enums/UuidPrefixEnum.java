package com.alsudais.enums;

import java.util.List;
import java.util.stream.Collectors;

import ch.qos.logback.classic.model.LevelModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UuidPrefixEnum {

	USER("USER_"),
	COMPANY("COMPANY_"),
	MODULE("MODULE_ID_"),
	ROLE("ROLE_ID_"),
	USER_ROLE("USER_ROLE_ID_"),
    PLATFORM("PLATFORM_ID_");
    @Getter
	private String value;

	public static List<String> getAllValues() {
		return List.of(UuidPrefixEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
	}
}
