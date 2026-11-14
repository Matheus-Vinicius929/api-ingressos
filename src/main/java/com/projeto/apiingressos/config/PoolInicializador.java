package com.projeto.apiingressos.config;

import com.projeto.apiingressos.entity.PoolControle;
import com.projeto.apiingressos.repository.PoolControleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PoolInicializador implements CommandLineRunner {

    private final PoolControleRepository poolRepository;

    public PoolInicializador(PoolControleRepository poolRepository) {
        this.poolRepository = poolRepository;
    }

    @Override
    public void run(String... args) {

        if (!poolRepository.existsById(1L)) {
            PoolControle pool = new PoolControle(1L, 100);
            poolRepository.save(pool);

            System.out.println("Estoque inicializado com 100 ingressos!");
        }
    }
}
