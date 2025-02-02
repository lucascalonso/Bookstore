package com.lucascorreia.exception;

public class FaturaNaoEncontradaException extends RuntimeException {
    public FaturaNaoEncontradaException(String message) {
        super(message);
    }
}
