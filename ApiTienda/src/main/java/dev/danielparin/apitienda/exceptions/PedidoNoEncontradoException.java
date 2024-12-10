package dev.danielparin.apitienda.exceptions;

public class PedidoNoEncontradoException extends RecursoNoEncontradoException {
    public PedidoNoEncontradoException(Long id) {
        super("Pedido con ID " + id + " no encontrado");
    }
}
