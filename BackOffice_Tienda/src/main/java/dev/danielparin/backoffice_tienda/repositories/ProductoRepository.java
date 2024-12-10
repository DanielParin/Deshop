package dev.danielparin.backoffice_tienda.repositories;

import dev.danielparin.backoffice_tienda.models.Producto;
import dev.danielparin.backoffice_tienda.services.ApiService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Arrays;

@AllArgsConstructor
public class ProductoRepository {

    private final ApiService apiService;

    public List<Producto> findAll() throws Exception {
        return Arrays.asList(apiService.get("productos/all",Producto[].class));
    }
    public List<Producto> buscarProductosPorNombre(String nombre) throws Exception {
        return Arrays.asList(apiService.get("productos?nombre=" + nombre, Producto[].class));
    }

    public Producto buscarProductoPorId(Long id) throws Exception {
        return apiService.get("productos/" + id, Producto.class);
    }

    public Producto crearProducto(Producto producto) throws Exception {
        return apiService.post("productos", producto, Producto.class);
    }

    public Producto actualizarProducto(Long id, Producto producto) throws Exception {
        return apiService.put("productos/" + id, producto, Producto.class);
    }

    public void eliminarProducto(Long id) throws Exception {
        apiService.delete("productos/" + id);
    }
}
