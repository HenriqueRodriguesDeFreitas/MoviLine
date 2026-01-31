package com.vortex.EntregaRapida.repository;

import com.vortex.EntregaRapida.model.Rua;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuaRepository extends JpaRepository<Rua, UUID> {

    boolean existsByIdAndBairroId(UUID ruaId, UUID bairroId);
    boolean existsByBairroId(UUID bairroId);
    boolean existsByNomeIgnoreCaseAndBairroId(String nome, UUID bairroId);
    Page<Rua> findByBairroId(UUID bairroId, Pageable pageable);
    Page<Rua> findByNomeIgnoreCaseContainingAndBairroId(String nomeRua, UUID bairroId, Pageable pageable);
}
