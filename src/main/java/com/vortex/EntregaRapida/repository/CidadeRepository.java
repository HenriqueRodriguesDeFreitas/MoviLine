package com.vortex.EntregaRapida.repository;

import com.vortex.EntregaRapida.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CidadeRepository extends JpaRepository<Cidade, UUID> {
    @Query("""
             SELECT new Cidade(c.id, c.nome) FROM Cidade c WHERE c.estado.id = :estadoId
            """)
    List<Cidade> buscarCidadesPorEstado(UUID estadoId);

    @Query("""
            SELECT new Cidade(c.id, c.nome) FROM Cidade c WHERE c.id = :cidadeId
            """)
    Optional<Cidade> buscarCidadeSimplesPorId(UUID cidadeId);

    @Query("""
            SELECT new Cidade(c.id, c.nome) 
            FROM Cidade c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            """)
    List<Cidade> buscarCidadesPorNomeContaining(String nome);
}
