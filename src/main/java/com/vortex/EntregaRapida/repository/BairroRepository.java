package com.vortex.EntregaRapida.repository;


import com.vortex.EntregaRapida.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BairroRepository extends JpaRepository<Bairro, UUID> {
    @Query("""
            SELECT new Bairro(b.id, b.nome) FROM Bairro b WHERE b.id = :bairroId
            """)
    Optional<Bairro> buscarBairroPorId(UUID bairroId);

    @Query("""
            SELECT new Bairro(b.id, b.nome) 
            FROM Bairro b 
            WHERE LOWER(b.nome)
            LIKE LOWER (CONCAT('%', :nome, '%'))
            """)
    List<Bairro> buscarBairroPorNome(String nome);

    @Query("""
           SELECT new Bairro(b.id, b.nome) 
           FROM Bairro b 
           WHERE b.cidade.id = :cidadeId 
           AND LOWER (b.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
           """)
    List<Bairro> buscarBairroPorCidadeENome(UUID cidadeId, String nome);
}
