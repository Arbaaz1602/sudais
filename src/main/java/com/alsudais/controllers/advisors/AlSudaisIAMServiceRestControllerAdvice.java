package com.alsudais.controllers.advisors;

import com.alsudais.beans.response.ServiceResponseBean;
import com.alsudais.commons.AlSudaisCommonConstants;
import com.alsudais.configs.AlSudaisLocaleResolverConfig;
import com.alsudais.exceptions.AlSudaisCustomException;
import com.alsudais.exceptions.AlSudaisDataNotFoundException;
import com.alsudais.exceptions.AlSudaisAlreadyExistException;
import com.alsudais.exceptions.AlSudaisNotActiveException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AlSudaisIAMServiceRestControllerAdvice {

    @Autowired
    private AlSudaisLocaleResolverConfig alSudaisLocaleResolverConfig;

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AlSudaisCustomException.class)
    public ServiceResponseBean handleAlSudaisCustomException(AlSudaisCustomException alSudaisCustomException) {
        return ServiceResponseBean.builder()
                .message(this.alSudaisLocaleResolverConfig.toLocale(alSudaisCustomException.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AlSudaisAlreadyExistException.class)
    public ServiceResponseBean handleAlSudaisModuleAlreadyPresentException(AlSudaisAlreadyExistException alSudaisAlreadyExistException) {
        return ServiceResponseBean.builder()
                .message(this.alSudaisLocaleResolverConfig.toLocale(alSudaisAlreadyExistException.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AlSudaisDataNotFoundException.class)
    public ServiceResponseBean handleAlSudaisDataNotFoundException(AlSudaisDataNotFoundException alSudaisDataNotFoundException) {
        return ServiceResponseBean.builder()
                .message(this.alSudaisLocaleResolverConfig.toLocale(alSudaisDataNotFoundException.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AlSudaisNotActiveException.class)
    public ServiceResponseBean handleAlSudaisNotActiveException(AlSudaisNotActiveException alSudaisNotActiveException) {
        return ServiceResponseBean.builder()
                .message(this.alSudaisLocaleResolverConfig.toLocale(alSudaisNotActiveException.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ServiceResponseBean handleWebExchangeBindException(WebExchangeBindException webExchangeBindException) {
        Set<String> errorSet = webExchangeBindException.getBindingResult().getFieldErrors().stream().map(data -> data.getDefaultMessage()).collect(Collectors.toSet());
        errorSet = errorSet.parallelStream().map(errorCode -> {
            try {
                if (errorCode.contains(AlSudaisCommonConstants.INSTANCE.SPACE_STRING)) {
                    Object[] errorCodeArray = errorCode.split(AlSudaisCommonConstants.INSTANCE.SPACE_STRING);
                    String errorMessage = errorCodeArray[BigInteger.ZERO.bitCount()].toString();
                    System.arraycopy(errorCodeArray, BigInteger.ONE.intValue(), errorCodeArray, BigInteger.ZERO.intValue(), errorCodeArray.length - BigInteger.ONE.intValue());
                    return String.format(errorMessage, errorCodeArray);
                }
                return this.alSudaisLocaleResolverConfig.toLocale(errorCode);
            } catch (Exception e) {
                return this.alSudaisLocaleResolverConfig.toLocale(errorCode);
            }
        }).collect(Collectors.toSet());
        return ServiceResponseBean.builder()
                .errors(errorSet)
                .message(errorSet.stream().findFirst().isPresent() ? errorSet.stream().findFirst().get() : null)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ServiceResponseBean handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        Set<String> errorSet = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream().map(data -> data.getDefaultMessage()).collect(Collectors.toSet());
        errorSet = errorSet.parallelStream().map(errorCode -> {
            try {
                if (errorCode.contains(AlSudaisCommonConstants.INSTANCE.SPACE_STRING)) {
                    Object[] errorCodeArray = errorCode.split(AlSudaisCommonConstants.INSTANCE.SPACE_STRING);
                    String errorMessage = errorCodeArray[BigInteger.ZERO.bitCount()].toString();
                    System.arraycopy(errorCodeArray, BigInteger.ONE.intValue(), errorCodeArray, BigInteger.ZERO.intValue(), errorCodeArray.length - BigInteger.ONE.intValue());
                    return String.format(errorMessage, errorCodeArray);
                }
                return this.alSudaisLocaleResolverConfig.toLocale(errorCode);
            } catch (Exception e) {
                return this.alSudaisLocaleResolverConfig.toLocale(errorCode);
            }
        }).collect(Collectors.toSet());
        return ServiceResponseBean.builder()
                .errors(errorSet)
                .message(errorSet.stream().findFirst().isPresent() ? errorSet.stream().findFirst().get() : null)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ServiceResponseBean handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
        Set<String> errorSet = constraintViolationException.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        errorSet = errorSet.parallelStream().map(errorCode -> {
            try {
                if (errorCode.contains(AlSudaisCommonConstants.INSTANCE.SPACE_STRING)) {
                    Object[] errorCodeArray = errorCode.split(AlSudaisCommonConstants.INSTANCE.SPACE_STRING);
                    String errorMessage = errorCodeArray[BigInteger.ZERO.bitCount()].toString();
                    System.arraycopy(errorCodeArray, BigInteger.ONE.intValue(), errorCodeArray, BigInteger.ZERO.intValue(), errorCodeArray.length - BigInteger.ONE.intValue());
                    return String.format(errorMessage, errorCodeArray);
                }
                return this.alSudaisLocaleResolverConfig.toLocale(errorCode);
            } catch (Exception e) {
                return this.alSudaisLocaleResolverConfig.toLocale(errorCode);
            }
        }).collect(Collectors.toSet());
        return ServiceResponseBean.builder()
                .errors(errorSet)
                .message(errorSet.stream().findFirst().isPresent() ? errorSet.stream().findFirst().get() : null)
                .build();
    }
}
