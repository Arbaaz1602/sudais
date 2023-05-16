package com.alsudais.commons;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum AlSudaisReactiveSpecificationConstants {

    INSTANCE;

    public final String JOIN_AND = "AN";

    public final String JOIN_OR = "OR";

    public static final String OPERATOR_EQUAL = "EQ";

    public static final String OPERATOR_NOTEQUAL = "NE";

    public static final String OPERATOR_CONTAIN = "CT";

    public static final String OPERATOR_START_WITH = "SW";

    public static final String OPERATOR_ENDS_WITH = "EW";

    public static final String OPERATOR_GREATER_THAN = "GT";

    public static final String OPERATOR_LESS_THAN = "LT";

    public static final String OPERATOR_GREATER_THAN_EQUAL = "GE";

    public static final String OPERATOR_LESS_THAN_EQUAL = "LE";

    public static final String OPERATOR_IN = "IN";

    public static final String DATA_TYPE_STRING = "DS";

    public static final String DATA_TYPE_NUMBER = "DN";

    public static final String DATA_TYPE_BOOLEAN = "DB";

    public static final String DATA_TYPE_OTHER = "DO";

    public final String SPLIT_PATTERN_STRING = "`([^<]+)<([^>]+)>([^`]+)";

    public final String SPLIT_PATTERN_NUMBER = "`([^<]+)<([^>]+)>([^`]+)";

    public final String BOOLEAN = "boolean";

    public final String BYTE = "byte";

    public final String CHARACTER = "character";

    public final String SHORT = "short";

    public final String INTEGER = "integer";

    public final String LONG = "long";

    public final String FLOAT = "float";

    public final String DOUBLE = "double";

    public final String BIGINTEGER = "biginteger";

    public final String BIGDECIMAL = "bigdecimal";
}
