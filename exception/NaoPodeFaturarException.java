package com.lucascorreia.exception;

public class NaoPodeFaturarException extends RuntimeException {
    public NaoPodeFaturarException(String message) {
        super(message);
    }
}
