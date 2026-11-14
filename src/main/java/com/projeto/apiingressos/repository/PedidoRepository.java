package com.projeto.apiingressos.repository;

import com.projeto.apiingressos.entity.Pedido;
import com.projeto.apiingressos.entity.PedidoStatus;
import com.projeto.apiingressos.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    long countByUsuarioAndStatus(Usuario usuario, PedidoStatus status);

    long countByStatus(PedidoStatus status);
}
