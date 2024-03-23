package com.bisa.clientes.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Cliente {
    private UUID clienteId;
    private UUID personaId;
    private String email;
    private String telefono;
    private String ocupacion;
    private String estado;
    private Persona persona;
}
