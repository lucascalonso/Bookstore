package com.lucascorreia.exception;

public class NaoPodeCancelarFaturaException extends RuntimeException {
    public NaoPodeCancelarFaturaException(String message) {
        super(message);
    }
}
