package com.projeto.apiingressos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long showId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PedidoStatus status = PedidoStatus.NO_CARRINHO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Pedido() {
    }

    public Pedido(Long showId, Usuario usuario) {
        this.showId = showId;
        this.usuario = usuario;
        this.status = PedidoStatus.NO_CARRINHO;
    }

    public Long getId() {
        return id;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setStatus(PedidoStatus status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
