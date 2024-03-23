package com.bisa.clientes.controller;
import com.bisa.clientes.model.Cliente;
import com.bisa.clientes.model.Persona;
import com.bisa.clientes.model.Referencia;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "Clientes", description = "Gestión de clientes")
public class ClienteController {

    private List<Persona> personas = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private List<Referencia> referencias = new ArrayList<>();
    @PostMapping("/persona")
    @Operation(summary = "Crear una nueva persona")
    public ResponseEntity<Persona> crearPersona(@RequestBody Persona persona) {
        persona.setPersonaId(UUID.randomUUID());
        personas.add(persona);
        return new ResponseEntity<>(persona, HttpStatus.CREATED);
    }

    @GetMapping("/personas")
    @Operation(summary = "Obtener todas las personas")
    public ResponseEntity<List<Persona>> obtenerTodasLasPersonas() {
        return ResponseEntity.ok(personas);
    }

    @PostMapping("/cliente")
    @Operation(summary = "Crear un nuevo cliente")
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        String estado = "CREADO";

        Persona persona = buscarPersonaPorId(cliente.getPersonaId());

        if (persona == null) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no se encontró
        }

        cliente.setClienteId(UUID.randomUUID());
        cliente.setEstado(estado);
        cliente.setPersona(persona);
        clientes.add(cliente);
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    @PostMapping("/{clienteId}/referencias")
    @Operation(summary = "Agregar referencia a un cliente")
    public ResponseEntity<Referencia> agregarReferencia(@PathVariable UUID clienteId, @RequestBody Referencia referencia) {
        Cliente cliente = buscarClientePorId(clienteId);

        if (cliente == null) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no se encontró el cliente
        }

        Persona persona = buscarPersonaPorId(referencia.getPersonaId());

        if (persona == null) {
            return ResponseEntity.notFound().build();
        }
        referencias.add(referencia);

        //verifica cantidad de referencias por cliente y cambia de estado
        verificaCantidadReferencias(clienteId);

        return ResponseEntity.ok(referencia);
    }

    @DeleteMapping("/{clienteId}/referencias")
    @Operation(summary = "Eliminar referencia de un cliente")
    public ResponseEntity<Referencia> eliminarReferenciaDeCliente(@PathVariable UUID clienteId, @RequestBody Referencia referencia) {
        Cliente cliente = buscarClientePorId(clienteId);

        if (cliente == null) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no se encontró el cliente
        }

        // Buscamos la referencia
        for (Referencia refEliminar : referencias) {
            if (refEliminar.getClienteId().equals(cliente.getClienteId()) && refEliminar.getPersonaId().equals(referencia.getPersonaId())) {
                referencia.setMotivo(referencia.getMotivo());
                referencia.setDeleted("ELIMINADO");
            }
        }

        //verifica cantidad de referencias por cliente y cambia de estado
        verificaCantidadReferencias(clienteId);

        return ResponseEntity.ok(referencia);
    }

    @GetMapping("/clientes/{accesibilidad}")
    @Operation(summary = "Listar clientes con filtros")
    public ResponseEntity<List<Cliente>> listarClientesConFiltros(@PathVariable String accesibilidad) {
        List<Cliente> clientesFiltrados = new ArrayList<>();
        Integer cantReferencias = 0;
        Integer cantReferenciasClientes = 0;
        for (Cliente cliente : clientes) {
                cantReferencias = 0;
                cantReferenciasClientes = 0;
                for (Referencia referencia : referencias) {
                    if (referencia.getClienteId().equals(cliente.getClienteId()) && referencia.getDeleted() == null) {
                        cantReferencias++;
                        cantReferenciasClientes = (buscarClientePorId(referencia.getClienteId()))!= null ? cantReferenciasClientes+1 : cantReferenciasClientes;
                    }
                }

                switch (accesibilidad) {
                    case "NULA":
                        if (cantReferencias.equals(0) && cantReferenciasClientes.equals(0)) {
                            clientesFiltrados.add(cliente);
                        }
                        break;
                    case "MALA":
                        if (cantReferencias.equals(1) && cantReferenciasClientes.equals(0)) {
                            clientesFiltrados.add(cliente);
                        }
                        break;
                    case "REGULAR" :
                        if ((cantReferencias.equals(1) && cantReferenciasClientes.equals(1) || (cantReferencias >= 2 && cantReferenciasClientes.equals(0)))) {
                            clientesFiltrados.add(cliente);
                        }
                        break;
                    case "BUENA":
                        if ((cantReferencias>=2 && cantReferenciasClientes>=2 || (cantReferencias >= 3 && cantReferenciasClientes>=1))) {
                            clientesFiltrados.add(cliente);
                        }
                        break;
                    default:
                        break;
                }
        }

        return ResponseEntity.ok(clientesFiltrados);
    }

    private Void verificaCantidadReferencias(UUID clienteId) {
        Integer cantReferencias = 0;
        for (Cliente cliente : clientes) {
            if (cliente.getClienteId().equals(clienteId)) {
                for (Referencia referencia : referencias) {
                    if (referencia.getClienteId().equals(clienteId) && referencia.getDeleted() == null) {
                        cantReferencias++;
                    }
                }
                if(cantReferencias.equals(0) ){
                    cliente.setEstado("BLOQUEADO");
                }
                else{
                    cliente.setEstado("ACTIVO");
                }
                return null;
            }
        }
        return null;
    }
    private Cliente buscarClientePorId(UUID clienteId) {
        for (Cliente cliente : clientes) {
            if (cliente.getClienteId().equals(clienteId)) {
                return cliente;
            }
        }
        return null; // Devuelve null si no se encuentra ningún cliente con el ID dado
    }

    private Persona buscarPersonaPorId(UUID personaId) {
        for (Persona persona : personas) {
            if (persona.getPersonaId().equals(personaId)) {
                 return persona;
            }
        }
        return null;
    }

}
