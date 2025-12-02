package com.vortex.EntregaRapida.repository;


import com.vortex.EntregaRapida.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BairroRepository extends JpaRepository<Bairro, UUID> {
    @Query("""
            SELECT new Bairro(b.id, b.nome) FROM Bairro b WHERE b.id = :bairroId
            """)
    Optional<Bairro> buscarBairroPorId(UUID bairroId);
}
