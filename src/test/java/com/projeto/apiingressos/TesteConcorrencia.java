package com.projeto.apiingressos;

import com.projeto.apiingressos.entity.Pedido;
import com.projeto.apiingressos.entity.Usuario;
import com.projeto.apiingressos.repository.PedidoRepository;
import com.projeto.apiingressos.repository.UsuarioRepository;
import com.projeto.apiingressos.service.PedidoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TesteConcorrencia {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    void duasRequisicoesNaoPodemReservarMesmoPedido() throws Exception {

        String username = "concorrencia-" + UUID.randomUUID();

        Usuario usuario = usuarioRepository.save(
                new Usuario(username, "senha")
        );

        Pedido pedido = pedidoRepository.save(
                new Pedido(7L, usuario)
        );

        CountDownLatch inicio = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            Future<String> primeira = executor.submit(() -> {
                inicio.await();
                try {
                    pedidoService.reservar(pedido.getId(), username);
                    return "SUCESSO";
                } catch (IllegalArgumentException e) {
                    return e.getMessage();
                }
            });

            Future<String> segunda = executor.submit(() -> {
                inicio.await();
                try {
                    pedidoService.reservar(pedido.getId(), username);
                    return "SUCESSO";
                } catch (IllegalArgumentException e) {
                    return e.getMessage();
                }
            });

            inicio.countDown();

            String resultado1 = primeira.get();
            String resultado2 = segunda.get();

            assertEquals(
                    1,
                    java.util.List.of(resultado1, resultado2)
                            .stream()
                            .filter("SUCESSO"::equals)
                            .count()
            );

            assertEquals(
                    1,
                    java.util.List.of(resultado1, resultado2)
                            .stream()
                            .filter("ESTADO_INVALIDO"::equals)
                            .count()
            );

        } finally {
            executor.shutdownNow();
            pedidoRepository.deleteById(pedido.getId());
            usuarioRepository.delete(usuario);
        }
    }
}