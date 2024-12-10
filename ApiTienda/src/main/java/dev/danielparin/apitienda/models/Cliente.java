package dev.danielparin.apitienda.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;



@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    private String dni;
    private String nombre;
    private String apellidos;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("fNacimiento")
    private Date fNacimiento;
    private String correo;
    private String telefono;
    private String direccion;
}
