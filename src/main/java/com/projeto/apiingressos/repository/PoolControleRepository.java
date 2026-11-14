package com.projeto.apiingressos.repository;

import com.projeto.apiingressos.entity.PoolControle;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PoolControleRepository extends JpaRepository<PoolControle, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PoolControle p WHERE p.id = :id")
    Optional<PoolControle> buscarComBloqueio(@Param("id") Long id);
}
