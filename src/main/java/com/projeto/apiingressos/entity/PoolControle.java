package com.projeto.apiingressos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pool_controle")
public class PoolControle {

    @Id
    private Long id;

    @Column(nullable = false)
    private Integer total;

    public PoolControle() {
    }

    public PoolControle(Long id, Integer total) {
        this.id = id;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
