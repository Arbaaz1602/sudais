package com.alsudais.enums;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LanguageCodeEnum {

	ENGLISH("en"),
	ARABIC("ar"),
	URDU("ur");

	@Getter
	private String value;

	public static List<String> getAllValues() {
		return List.of(LanguageCodeEnum.values()).stream().map(data -> data.value).collect(Collectors.toList());
	}
}
