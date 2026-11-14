package com.projeto.apiingressos.controller;

import com.projeto.apiingressos.dto.PedidoRequest;
import com.projeto.apiingressos.entity.Pedido;
import com.projeto.apiingressos.service.PedidoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/pedidos")
    public ResponseEntity<?> criarPedido(
            @Valid @RequestBody PedidoRequest request,
            Principal principal) {

        Pedido pedido = pedidoService.criarPedido(
                request.getShowId(),
                principal.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", pedido.getId(),
                        "status", pedido.getStatus().name()
                ));
    }

    @PostMapping("/pedidos/{id}/reservar")
    public ResponseEntity<?> reservar(
            @PathVariable Long id,
            Principal principal) {

        try {
            Pedido pedido = pedidoService.reservar(
                    id,
                    principal.getName()
            );

            return ResponseEntity.ok()
                    .header("X-Lote", "7F3A")
                    .body(Map.of(
                            "id", pedido.getId(),
                            "status", pedido.getStatus().name()
                    ));

        } catch (IllegalArgumentException e) {
            return tratarErro(e.getMessage());

        } catch (IllegalStateException e) {
            return tratarErro(e.getMessage());
        }
    }

    @PostMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<?> cancelar(
            @PathVariable Long id,
            Principal principal) {

        try {
            Pedido pedido = pedidoService.cancelar(
                    id,
                    principal.getName()
            );

            return ResponseEntity.ok(Map.of(
                    "id", pedido.getId(),
                    "status", pedido.getStatus().name()
            ));

        } catch (IllegalArgumentException e) {
            return tratarErro(e.getMessage());

        } catch (IllegalStateException e) {
            return tratarErro(e.getMessage());
        }
    }

    @GetMapping("/pool")
    public ResponseEntity<?> consultarPool() {
        return ResponseEntity.ok(pedidoService.consultarPool());
    }

    private ResponseEntity<?> tratarErro(String erro) {

        if ("PEDIDO_NAO_ENCONTRADO".equals(erro)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", erro));
        }

        if ("POOL_CHEIO".equals(erro) ||
                "COTA_PESSOAL".equals(erro) ||
                "ESTADO_INVALIDO".equals(erro)) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", erro));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", erro));
    }
}