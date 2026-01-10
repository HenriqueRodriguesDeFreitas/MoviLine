package com.vortex.EntregaRapida.repository;

import com.vortex.EntregaRapida.model.Cidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CidadeRepository extends JpaRepository<Cidade, UUID> {
    Page<Cidade> findByEstadoId(UUID estadoId, Pageable pageable);

    List<Cidade> findByEstadoId(UUID estadoId);

    List<Cidade> findByNomeContainingIgnoreCase(String nome);

    Page<Cidade> findByEstadoIdAndNomeIgnoreCase(UUID estadoId, String nomeCidade, Pageable pageable);

    boolean existsByIdAndEstadoId(UUID cidadeId, UUID estadoId);

    boolean existsByNomeIgnoreCaseAndEstadoId(String nome, UUID estadoId);

    boolean existsByNomeIgnoreCaseAndEstadoIdAndIdNot(String nome, UUID id, UUID id1);
}
