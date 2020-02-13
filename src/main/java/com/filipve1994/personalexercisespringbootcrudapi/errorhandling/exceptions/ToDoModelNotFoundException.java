package com.filipve1994.personalexercisespringbootcrudapi.errorhandling.exceptions;

public class ToDoModelNotFoundException extends RuntimeException {

    public ToDoModelNotFoundException() {
        super();
    }

    public ToDoModelNotFoundException(String message) {
        super(message);
    }

    public ToDoModelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToDoModelNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ToDoModelNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
