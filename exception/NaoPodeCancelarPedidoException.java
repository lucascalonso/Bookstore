package com.lucascorreia.exception;

public class NaoPodeCancelarPedidoException extends RuntimeException {
    public NaoPodeCancelarPedidoException(String message) {
        super(message);
    }
}
