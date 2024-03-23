package com.bisa.clientes.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Persona {
    private UUID personaId;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Date   fechaNacimiento;
    private String direccion;
    private String carnetIdentidad;
}
