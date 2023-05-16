package com.alsudais.exceptions;

public class AlSudaisDataNotFoundException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    public AlSudaisDataNotFoundException(String message) {
        super(message);
    }
    public AlSudaisDataNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}