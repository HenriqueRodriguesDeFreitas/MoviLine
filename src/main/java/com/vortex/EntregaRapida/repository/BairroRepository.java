package com.vortex.EntregaRapida.repository;


import com.vortex.EntregaRapida.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BairroRepository extends JpaRepository<Bairro, UUID> {

}
