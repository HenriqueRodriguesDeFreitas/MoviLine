package com.vortex.EntregaRapida.repository;

import com.vortex.EntregaRapida.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstadoRepository extends JpaRepository<Estado, UUID> {
    @Query("SELECT new Estado(e.id, e.nome) FROM Estado e WHERE LOWER(e.nome) = LOWER(:nome)")
    Optional<Estado> findByNomeIgnoreCase(String nome);

    @Query("SELECT new Estado(e.id, e.nome) FROM Estado e WHERE e.id = :id")
    Optional<Estado> buscarEstadoSimplesPorId(UUID id);

    @Query("SELECT new Estado(e.id, e.nome) FROM Estado e")
    List<Estado> buscarTodosEstadosSimples();

    @Query("""
            SELECT new Estado(e.id, e.nome) 
            FROM Estado e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            """)
    List<Estado> buscarEstadoPorNomeContaining(String nome);
}
