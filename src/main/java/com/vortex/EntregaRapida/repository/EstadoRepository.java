package com.vortex.EntregaRapida.repository;

import com.vortex.EntregaRapida.model.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstadoRepository extends JpaRepository<Estado, UUID> {
    Optional<Estado> findByNomeIgnoreCase(String nome);

    Page<Estado> findByNomeIgnoreCaseContaining(String nome, Pageable pageable);
}
