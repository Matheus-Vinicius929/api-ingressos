package com.projeto.apiingressos.service;

import com.projeto.apiingressos.entity.Pedido;
import com.projeto.apiingressos.entity.PedidoStatus;
import com.projeto.apiingressos.entity.PoolControle;
import com.projeto.apiingressos.entity.Usuario;

import com.projeto.apiingressos.repository.PedidoRepository;
import com.projeto.apiingressos.repository.PoolControleRepository;
import com.projeto.apiingressos.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PoolControleRepository poolRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            UsuarioRepository usuarioRepository,
            PoolControleRepository poolRepository) {

        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.poolRepository = poolRepository;
    }

    @Transactional
    public Pedido criarPedido(Long showId, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("USUARIO_NAO_ENCONTRADO"));

        Pedido pedido = new Pedido(showId, usuario);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido reservar(Long pedidoId, String username) {

        PoolControle pool = poolRepository.buscarComBloqueio(1L)
                .orElseThrow(() -> new IllegalStateException("POOL_NAO_ENCONTRADO"));

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("PEDIDO_NAO_ENCONTRADO"));

        if (!pedido.getUsuario().getUsername().equals(username)) {
            throw new IllegalArgumentException("PEDIDO_NAO_ENCONTRADO");
        }

        if (pedido.getStatus() != PedidoStatus.NO_CARRINHO) {
            throw new IllegalArgumentException("ESTADO_INVALIDO");
        }

        long ocupadas = pedidoRepository.countByStatus(PedidoStatus.RESERVADO);

        if (ocupadas >= pool.getTotal()) {
            throw new IllegalStateException("POOL_CHEIO");
        }

        long reservasUsuario = pedidoRepository.countByUsuarioAndStatus(
                pedido.getUsuario(),
                PedidoStatus.RESERVADO
        );

        if (reservasUsuario >= 2) {
            throw new IllegalStateException("COTA_PESSOAL");
        }

        pedido.setStatus(PedidoStatus.RESERVADO);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cancelar(Long pedidoId, String username) {

        poolRepository.buscarComBloqueio(1L)
                .orElseThrow(() -> new IllegalStateException("POOL_NAO_ENCONTRADO"));

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("PEDIDO_NAO_ENCONTRADO"));

        if (!pedido.getUsuario().getUsername().equals(username)) {
            throw new IllegalArgumentException("PEDIDO_NAO_ENCONTRADO");
        }

        if (pedido.getStatus() != PedidoStatus.RESERVADO) {
            throw new IllegalArgumentException("ESTADO_INVALIDO");
        }

        pedido.setStatus(PedidoStatus.CANCELADO);

        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> consultarPool() {

        long ocupadas = pedidoRepository.countByStatus(PedidoStatus.RESERVADO);

        long total = poolRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("POOL_NAO_ENCONTRADO"))
                .getTotal();

        return Map.of(
                "total", total,
                "ocupadas", ocupadas,
                "livres", total - ocupadas
        );
    }
}
