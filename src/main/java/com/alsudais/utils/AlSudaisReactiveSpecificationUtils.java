package com.alsudais.utils;

import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.commons.AlSudaisReactiveSpecificationConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.query.Criteria;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum AlSudaisReactiveSpecificationUtils {

    INSTANCE;

    public String convertFilterCriteriaToEntityFilterCriteria(String filterCriteria, String prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = Pattern.compile(AlSudaisReactiveSpecificationConstants.INSTANCE.SPLIT_PATTERN_STRING + AlSudaisCommonConstants.INSTANCE.PIPE_STRING + AlSudaisReactiveSpecificationConstants.INSTANCE.SPLIT_PATTERN_NUMBER).matcher(filterCriteria);
        while(matcher.find()) {
            String field = matcher.group(BigInteger.ONE.intValue());
            stringBuilder.append(AlSudaisCommonConstants.INSTANCE.BACKQUOTE_STRING);
            stringBuilder.append(prefix.concat(StringUtils.capitalize(field)));

            String operator = matcher.group(BigInteger.TWO.intValue());
            stringBuilder.append(AlSudaisCommonConstants.INSTANCE.LESSTHAN_STRING);
            stringBuilder.append(operator);
            stringBuilder.append(AlSudaisCommonConstants.INSTANCE.GREATERTHAN_STRING);

            String value = matcher.group(AlSudaisCommonConstants.INSTANCE.THREE);
            stringBuilder.append(value);
        }
        return stringBuilder.toString();
    }

    public Sort convertSortCriteriaToEntitySortCriteria(String sortCriteria, String prefix) {
        String[] sortCriteriaArray = sortCriteria.split(AlSudaisCommonConstants.INSTANCE.COLON_STRING);
        Sort sort = Sort.by(prefix.concat(StringUtils.capitalize(sortCriteriaArray[BigInteger.ZERO.intValue()])));
        return sortCriteriaArray.length > BigInteger.ONE.intValue() && sortCriteriaArray[BigInteger.ONE.intValue()].equalsIgnoreCase(Sort.Direction.DESC.name()) ? sort.descending() : sort.ascending();
    }


    public Criteria buildCriteria(String filterCriteria, List<Expression<? extends Comparable<?>>> externalExpressionList){
        List<Expression<? extends Comparable<?>>> expressionList = new ArrayList<Expression<? extends Comparable<?>>>();
        if(externalExpressionList != null)
            expressionList.addAll(externalExpressionList);

        expressionList.addAll(parseQuery(filterCriteria));
        return new CustomeCriteria(expressionList).build();
    }

    public static class CustomeCriteria {
        private List<Expression<? extends Comparable<?>>> expressions;

        public CustomeCriteria(List<Expression<? extends Comparable<?>>> expressions) {
            this.expressions = expressions;
        }

        public List<Expression<? extends Comparable<?>>> getExpressions() {
            return expressions;
        }

        public void setExpressions(List<Expression<? extends Comparable<?>>> expressions) {
            this.expressions = expressions;
        }

        public Criteria build() {
            Criteria criteria = Criteria.empty();
            for(Expression<? extends Comparable<?>> expression:expressions) {
                if (expression != null && expression.getField() != null && expression.getOperator() != null && (expression.getValue() != null || expression.getValues() != null)) {
                    Criteria tempCriteria = handleExpression(expression);
                    if (AlSudaisReactiveSpecificationConstants.INSTANCE.JOIN_AND.equalsIgnoreCase(expression.getJoin()))
                        criteria = criteria.and(tempCriteria);
                    else if (AlSudaisReactiveSpecificationConstants.INSTANCE.JOIN_OR.equalsIgnoreCase(expression.getJoin()))
                        criteria = criteria.or(tempCriteria);
                }
            }
            return criteria;
        }
    }

    private static <E extends Comparable<E>> Criteria handleExpression(Expression<E> expression) {
        return switch(expression.getOperator()) {
            case AlSudaisReactiveSpecificationConstants.OPERATOR_EQUAL -> Criteria.where(expression.getField()).is(expression.getValue()).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_NOTEQUAL -> Criteria.where(expression.getField()).not(expression.getValue()).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_CONTAIN -> Criteria.where(expression.getField()).like(AlSudaisCommonConstants.INSTANCE.PERCENTAGE_STRING + expression.getValue() + AlSudaisCommonConstants.INSTANCE.PERCENTAGE_STRING).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_START_WITH -> Criteria.where(expression.getField()).like(expression.getValue() + AlSudaisCommonConstants.INSTANCE.PERCENTAGE_STRING).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_ENDS_WITH -> Criteria.where(expression.getField()).like(AlSudaisCommonConstants.INSTANCE.PERCENTAGE_STRING + expression.getValue()).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_GREATER_THAN ->  Criteria.where(expression.getField()).greaterThan(expression.getValue()).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_LESS_THAN -> Criteria.where(expression.getField()).lessThan(expression.getValue()).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_GREATER_THAN_EQUAL -> Criteria.where(expression.getField()).greaterThanOrEquals(expression.getValue()).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_LESS_THAN_EQUAL ->
                    Criteria.where(expression.getField()).lessThanOrEquals(expression.getValue()).ignoreCase(Boolean.TRUE);
            case AlSudaisReactiveSpecificationConstants.OPERATOR_IN ->
                    Criteria.where(expression.getField()).in(expression.getValues());
            default -> throw new IllegalArgumentException("Unexpected value :: " + expression.getOperator());
        };
    }

    private static List<Expression<? extends Comparable<?>>> parseQuery(String query) {
        List<Expression<? extends Comparable<?>>> expressionList = new ArrayList<>();
        query = (null == query || query.isEmpty()) ? AlSudaisCommonConstants.INSTANCE.BLANK_STRING : query;
        Matcher matcher = Pattern.compile(AlSudaisReactiveSpecificationConstants.INSTANCE.SPLIT_PATTERN_STRING + AlSudaisCommonConstants.INSTANCE.PIPE_STRING + AlSudaisReactiveSpecificationConstants.INSTANCE.SPLIT_PATTERN_NUMBER).matcher(query);
        while(matcher.find()) {
            String group = matcher.group();
            String field = matcher.group(BigInteger.ONE.intValue());
            String operator = matcher.group(BigInteger.TWO.intValue());
            String value = matcher.group(AlSudaisCommonConstants.INSTANCE.THREE);
            String join = (operator.contains(AlSudaisCommonConstants.INSTANCE.COLON_STRING)) ? operator.split(AlSudaisCommonConstants.INSTANCE.COLON_STRING)[BigInteger.ONE.intValue()] : AlSudaisReactiveSpecificationConstants.INSTANCE.JOIN_AND;
            operator = (operator.contains(AlSudaisCommonConstants.INSTANCE.COLON_STRING)) ? operator.split(AlSudaisCommonConstants.INSTANCE.COLON_STRING)[BigInteger.ZERO.intValue()] : operator;

            List<String> values = null;
            if(operator.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.OPERATOR_IN))
                values = Arrays.stream(value.split(AlSudaisCommonConstants.INSTANCE.DOUBLE_BACKSLASH_CARET_STRING)).toList();

            if(group.equals(AlSudaisReactiveSpecificationConstants.INSTANCE.SPLIT_PATTERN_NUMBER))
                expressionList.add(new Expression<Double>(field, operator, join, Double.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Double.class::cast).collect(Collectors.toList()) : null));
            else {
                if(field.contains(AlSudaisCommonConstants.INSTANCE.COLON_STRING)) {
                    String[] fieldArray = field.split(AlSudaisCommonConstants.INSTANCE.COLON_STRING);
                    String fieldName = fieldArray[BigInteger.ZERO.intValue()];
                    String fieldType = fieldArray[BigInteger.ONE.intValue()];
                    try {
                        if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.BOOLEAN))
                            expressionList.add(new Expression<Boolean>(fieldName, operator, join, Boolean.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Boolean.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.BYTE))
                            expressionList.add(new Expression<Byte>(fieldName, operator, join, Byte.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Byte.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.CHARACTER))
                            expressionList.add(new Expression<Character>(fieldName, operator, join, Character.valueOf(value.charAt(BigInteger.ZERO.intValue())), (values != null && !values.isEmpty()) ? values.stream().map(Character.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.SHORT))
                            expressionList.add(new Expression<Short>(fieldName, operator, join, Short.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Short.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.INTEGER))
                            expressionList.add(new Expression<Integer>(fieldName, operator, join, Integer.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Integer.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.LONG))
                            expressionList.add(new Expression<Long>(fieldName, operator, join, Long.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Long.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.FLOAT))
                            expressionList.add(new Expression<Float>(fieldName, operator, join, Float.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Float.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.DOUBLE))
                            expressionList.add(new Expression<Double>(fieldName, operator, join, Double.valueOf(value), (values != null && !values.isEmpty()) ? values.stream().map(Double.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.BIGINTEGER))
                            expressionList.add(new Expression<BigInteger>(fieldName, operator, join, new BigInteger(value), (values != null && !values.isEmpty()) ? values.stream().map(BigInteger.class::cast).collect(Collectors.toList()) : null));
                        else if(fieldType.equalsIgnoreCase(AlSudaisReactiveSpecificationConstants.INSTANCE.BIGDECIMAL))
                            expressionList.add(new Expression<BigDecimal>(fieldName, operator, join, new BigDecimal(value), (values != null && !values.isEmpty()) ? values.stream().map(BigDecimal.class::cast).collect(Collectors.toList()) : null));
                        else
                            expressionList.add(new Expression<String>(fieldName, operator, join, value, values));
                    }
                    catch(Exception ex) {
                        expressionList.add(new Expression<String>(field, operator, join, value, values));
                    }
                }
                else {
                    expressionList.add(new Expression<String>(field, operator, join, value, values));
                }
            }
        }
        return expressionList;
    }

    public String manipulateFilterCriteria(String filterCriteria, Set<String> dateFields) {
        StringBuffer stringBuffer = new StringBuffer();
        Matcher matcher = Pattern.compile(AlSudaisReactiveSpecificationConstants.INSTANCE.SPLIT_PATTERN_STRING + AlSudaisCommonConstants.INSTANCE.PIPE_STRING + AlSudaisReactiveSpecificationConstants.INSTANCE.SPLIT_PATTERN_NUMBER).matcher(filterCriteria);
        while(matcher.find()) {
            String field = matcher.group(BigInteger.ONE.intValue());

            stringBuffer.append(AlSudaisCommonConstants.INSTANCE.BACKQUOTE_STRING);
            stringBuffer.append(field);

            String operator = matcher.group(BigInteger.TWO.intValue());
            stringBuffer.append(AlSudaisCommonConstants.INSTANCE.LESSTHAN_STRING);
            stringBuffer.append(operator);
            stringBuffer.append(AlSudaisCommonConstants.INSTANCE.GREATERTHAN_STRING);

            String value = matcher.group(AlSudaisCommonConstants.INSTANCE.THREE);
            if(dateFields != null && dateFields.contains(field)) {
                String[] splittedDateArray = value.split(AlSudaisCommonConstants.INSTANCE.FORWARD_SLASH_STRING, -BigInteger.ONE.intValue());

                StringBuilder stringBuilder = new StringBuilder();
                if(splittedDateArray.length == AlSudaisCommonConstants.INSTANCE.THREE) {
                    if(splittedDateArray[BigInteger.TWO.intValue()].length() == AlSudaisCommonConstants.INSTANCE.FOUR)
                        stringBuilder.append(splittedDateArray[BigInteger.TWO.intValue()]);

                    stringBuilder.append(AlSudaisCommonConstants.INSTANCE.HYPHEN_STRING)
                            .append(splittedDateArray[BigInteger.ONE.intValue()])
                            .append(AlSudaisCommonConstants.INSTANCE.HYPHEN_STRING)
                            .append(splittedDateArray[BigInteger.ZERO.intValue()]);
                } else if(splittedDateArray.length == BigInteger.TWO.intValue()) {
                    if(splittedDateArray[BigInteger.ONE.intValue()].length() >= BigInteger.TWO.intValue())
                        stringBuilder.append(splittedDateArray[BigInteger.ONE.intValue()]);

                    stringBuilder.append(AlSudaisCommonConstants.INSTANCE.HYPHEN_STRING)
                            .append(splittedDateArray[BigInteger.ZERO.intValue()]);
                } else {
                    stringBuilder.append(splittedDateArray[BigInteger.ZERO.intValue()]);
                }
                value = stringBuilder.toString();
            }
            stringBuffer.append(value);
        }
        return stringBuffer.toString();
    }

    @Data
    public static class Expression<E extends Comparable<E>> {

        private String field;

        private String operator;

        private String join;

        private E value;

        private List<E> values;

        public Expression() {
            super();
        }

        public Expression(String field, String operator, String join, E value, List<E> values) {
            super();
            this.field = field;
            this.operator = operator;
            this.join = join;
            this.value = value;
            this.values = values;
        }
    }
}
