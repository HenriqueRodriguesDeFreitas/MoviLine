package com.vortex.EntregaRapida.service.validation;

import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.model.Cidade;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.CidadeRepository;
import com.vortex.EntregaRapida.repository.EstadoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CidadeEstadoValidation {

    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;

    public CidadeEstadoValidation(CidadeRepository cidadeRepository,
                                  EstadoRepository estadoRepository) {
        this.cidadeRepository = cidadeRepository;
        this.estadoRepository = estadoRepository;
    }

    public Estado validaEstadoPorId(UUID estadoId) {
        return estadoRepository.buscarEstadoSimplesPorId(estadoId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhum estado encontrado com o id passado."));

    }

    public Cidade validaCidadePorId(UUID cidadeId) {
        return cidadeRepository.buscarCidadeSimplesPorId(cidadeId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhuma cidade encontrada com o Id passado."));
    }

    public List<Cidade> validarRetornaCidadeDoEstado(Estado estado) {
        return cidadeRepository.buscarCidadesPorEstado(estado.getId());
    }


}
