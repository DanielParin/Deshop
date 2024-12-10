package dev.danielparin.backoffice_tienda.repositories;

import dev.danielparin.backoffice_tienda.models.Cliente;
import dev.danielparin.backoffice_tienda.services.ApiService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Arrays;


@AllArgsConstructor
public class ClienteRepository {

    private final ApiService apiService;

    public List<Cliente> findAll() throws Exception {
        return Arrays.asList(apiService.get("clientes/all",Cliente[].class));
    }

    public List<Cliente> buscarClientesPorNombre(String nombre) throws Exception {
        return Arrays.asList(apiService.get("clientes?nombre=" + nombre, Cliente[].class));
    }


    public Cliente buscarClientePorId(Long id) throws Exception {
        return apiService.get("clientes/" + id, Cliente.class);
    }

    public Cliente buscarClientePorDni(String dni) throws Exception {
        return apiService.get("clientes/dni?dni=" + dni, Cliente.class);
    }

    public Cliente crearCliente(Cliente cliente) throws Exception {
        return apiService.post("clientes", cliente, Cliente.class);
    }

    public Cliente actualizarCliente(Long id, Cliente cliente) throws Exception {
        return apiService.put("clientes/" + id, cliente, Cliente.class);
    }

    public void eliminarCliente(Long id) throws Exception {
        apiService.delete("clientes/" + id);
    }
}

