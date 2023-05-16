package com.alsudais.commons;

public enum AlSudaisCommonConstants {

	INSTANCE;

	public final String BLANK_STRING = "";

	public final String HYPHEN_STRING = "-";

	public final String UNDERSCORE_STRING = "_";

	public final String SPACE_STRING = " ";
	
	public final String PIPE_STRING = "|";
	
	public final String BACKQUOTE_STRING = "`";

	public final String SINGLEQUOTE_STRING = "'";

	public final String DOT_STRING = ".";
	
	public final String ATTHERATE_STRING = "@";

	public final String OPEN_CURLY_BRACKET_STRING = "{";

	public final String CLOSE_CURLY_BRACKET_STRING = "}";

	public final String OPEN_PARENTHESIS_STRING = "(";

	public final String CLOSE_PARENTHESIS_STRING = ")";

	public final String COLON_STRING = ":";

	public final String FORWARD_SLASH_STRING = "/";

	public final char FORWARD_SLASH_CHAR = '/';

	public final String DOUBLE_FORWARD_SLASH_STRING = "//";

	public final String PLUS_STRING = "+";
	
	public final String HASH_STRING = "#";
	
	public final String DOLLAR_STRING = "$";

	public final String COMMA_STRING = ",";
	
	public final String DOUBLE_QUOTE_STRING = "\"";
	
	public final String ASTERISK_STRING = "*";
	
	public final String NEWLINE_STRING = "\n";
	
	public final String LESSTHAN_STRING = "<";
	
	public final String GREATERTHAN_STRING = ">";
	
	public final String PERCENTAGE_STRING = "%";

	public final String SEMI_COLON_STRING = ";";

	public final String EQUAL_TO_STRING = "=";

	public final String FORWARD_SLASH_DOUBLE_ASTERISK_STRING = "/**";

	public final String PASSWORD_STRING = "password";

	public final String ACCESS_TOKEN_STRING = "access_token";

	public final String REFRESH_TOKEN_STRING = "refresh_token";

	public final String ID_TOKEN_STRING = "id_token";
	
	public final String IDTOKEN_STRING = "IdToken";
	
	public final String BEARER_STRING = "Bearer ";

	public final String TEMP_FILE_STRING = "TempFile";
	
	public final String DEFAULT_STRING = "Default";
	
	public final String SERVICE_STRING = "Service";

	public final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	public final String PHONE_NUMBER_REGEX = "^\\+(?:[0-9] ?){6,14}[0-9]$";

	public final String PHONE_NUMBER_WITHOUT_DIAL_CODE_REGEX = "^[0][1-9]\\d{9}$|^[1-9]\\d{9}$";

	public final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_`*!|.,:;'()?\"/-])(?=\\S+$).{8,}$";

	public final String DIGIT_ONLY_REGEX = "[0-9]+";
	
	public final String CHARACTERS_ONLY_REGEX = "[^A-Za-z]";
	
	public final String ALL_STRING = "ALL";
	
	public final String TWO_POSITION_SPLIT_REGEX = "(?<=\\G..)";
	
	public final String X_FORWARDED_FOR_STRING = "X-FORWARDED-FOR";

	public final String INTERNAL_SERVER_ERROR_MESSAGE = "INTERNAL_SERVER_ERROR_MESSAGE";

	public final String MESSAGES_STRING = "messages";

	public final String PERIOD_SPLITTER_STRING = "\\.";

	public static final String DOB_DATE_FORMAT = "yyyy-MM-dd";

	public final Integer THREE = 3;

	public final Integer FOUR = 4;

	public final String UNDERSCORE_AUD_STRING = "_aud";

	public final String DOUBLE_BACKSLASH_CARET_STRING = "\\^";

	public final String CARET_STRING = "^";

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";



}
