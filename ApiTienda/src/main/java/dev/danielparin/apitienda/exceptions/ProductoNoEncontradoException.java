package dev.danielparin.apitienda.exceptions;

public class ProductoNoEncontradoException extends RecursoNoEncontradoException {
    public ProductoNoEncontradoException(Long id) {
        super("Producto con ID " + id + " no encontrado");
    }
}
