package com.vortex.EntregaRapida.repository;


import com.vortex.EntregaRapida.model.Bairro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BairroRepository extends JpaRepository<Bairro, UUID> {
    List<Bairro> findByNomeContainingIgnoreCase(String nome);

    Page<Bairro> findByCidadeId(UUID cidadeId, Pageable pageable);

    Page<Bairro> findByNomeIgnoreCaseContainingAndCidadeId(String nome, UUID cidadeId, Pageable pageable);

    boolean existsByNomeIgnoreCaseAndCidadeId(String nome, UUID cidadeId);

    boolean existsByIdAndCidadeId(UUID bairroId, UUID cidadeId);

    Optional<Bairro> findByNomeIgnoreCaseAndCidadeId(String nomeBairro, UUID cidadeId);

}
