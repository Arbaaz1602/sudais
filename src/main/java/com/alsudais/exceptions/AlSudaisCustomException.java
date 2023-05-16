package com.alsudais.exceptions;

public class AlSudaisCustomException extends IllegalStateException{
    private static final long serialVersionUID = 1L;

    public AlSudaisCustomException(String msg) {
        super(msg);
    }

    public AlSudaisCustomException(String msg, Throwable t) {
        super(msg, t);
    }
}
