package io.github.neudopb.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException() {
        super("Pedido não encontrado!");
    }
}
