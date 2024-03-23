package com.bisa.clientes.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class Referencia {
    private UUID clienteId;
    private UUID personaId;
    private String motivo;
    private String deleted;
}
