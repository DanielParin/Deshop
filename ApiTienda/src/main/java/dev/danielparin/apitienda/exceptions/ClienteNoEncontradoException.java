package dev.danielparin.apitienda.exceptions;

public class ClienteNoEncontradoException extends RecursoNoEncontradoException {
    public ClienteNoEncontradoException(Long id) {
        super("Cliente con ID " + id + " no encontrado");
    }
}
