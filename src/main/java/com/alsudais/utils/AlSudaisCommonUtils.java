package com.alsudais.utils;

import com.alsudais.commons.AlSudaisCommonConstants;
import io.r2dbc.spi.Row;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public enum AlSudaisCommonUtils {

    INSTANCE;

    private static final char[] POSSIBLE_CAPITAL_CHARACTERS_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] POSSIBLE_SMALL_CHARACTERS_STRING = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] POSSIBLE_NUMERIC_CHARACTERS_STRING = "0123456789".toCharArray();
    private static final char[] POSSIBLE_SPECIAL_CHARACTERS_STRING = "!@#$".toCharArray();

    private static final String CAMELCASE_REGEX_STRING = "([a-z0-9])([A-Z]+)";

    private static final String SNAKECASE_REPLACEMENT_STRING = "$1_$2";

    public String uniqueIdentifier(String... prefix) {
        return (prefix.length > BigInteger.ZERO.intValue() ? prefix[BigInteger.ZERO.intValue()] : AlSudaisCommonConstants.INSTANCE.BLANK_STRING).concat(UUID.randomUUID().toString().replace(AlSudaisCommonConstants.INSTANCE.HYPHEN_STRING, AlSudaisCommonConstants.INSTANCE.BLANK_STRING));
    }

    public String generateUniquePassword(int length) {
        return RandomStringUtils.random(length / 4, BigInteger.ZERO.intValue(), POSSIBLE_CAPITAL_CHARACTERS_STRING.length - BigInteger.ONE.intValue(), Boolean.FALSE, Boolean.FALSE, POSSIBLE_CAPITAL_CHARACTERS_STRING, new SecureRandom())
                .concat(RandomStringUtils.random(length / 4, BigInteger.ZERO.intValue(), POSSIBLE_SMALL_CHARACTERS_STRING.length - BigInteger.ONE.intValue(), Boolean.FALSE, Boolean.FALSE, POSSIBLE_SMALL_CHARACTERS_STRING, new SecureRandom()))
                .concat(RandomStringUtils.random(length / 4, BigInteger.ZERO.intValue(), POSSIBLE_NUMERIC_CHARACTERS_STRING.length - BigInteger.ONE.intValue(), Boolean.FALSE, Boolean.FALSE, POSSIBLE_NUMERIC_CHARACTERS_STRING, new SecureRandom()))
                .concat(RandomStringUtils.random(length / 4, BigInteger.ZERO.intValue(), POSSIBLE_SPECIAL_CHARACTERS_STRING.length - BigInteger.ONE.intValue(), Boolean.FALSE, Boolean.FALSE, POSSIBLE_SPECIAL_CHARACTERS_STRING, new SecureRandom()));
    }

    public String convertCamelCaseToLowerSnakeCase(String text){
        return text.replaceAll(CAMELCASE_REGEX_STRING, SNAKECASE_REPLACEMENT_STRING).toLowerCase();
    }

    public <T, K> T parseValue(Row row, K k, Class<T> t) {
        var value = switch (t.getSimpleName().toUpperCase()) {
            default -> row.get(k.toString(), t);
        };
        return (T) value;
    }
}
