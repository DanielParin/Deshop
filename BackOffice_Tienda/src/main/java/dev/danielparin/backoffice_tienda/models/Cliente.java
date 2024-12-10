package dev.danielparin.backoffice_tienda.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
public class Cliente {

    private Long idCliente;
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fNacimiento;
    private String correo;
    private String telefono;
    private String direccion;

    public Cliente(String dni, String nombre, String apellidos, LocalDate fNacimiento, String correo, String telefono, String direccion) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fNacimiento = fNacimiento;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
    }
}
