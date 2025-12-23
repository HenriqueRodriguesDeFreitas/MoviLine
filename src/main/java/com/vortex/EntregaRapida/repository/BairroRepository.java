package com.vortex.EntregaRapida.repository;


import com.vortex.EntregaRapida.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BairroRepository extends JpaRepository<Bairro, UUID> {
    List<Bairro> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndCidadeId(String nome, UUID cidadeId);

    boolean existsByIdAndCidadeId(UUID bairroId, UUID cidadeId);
}
