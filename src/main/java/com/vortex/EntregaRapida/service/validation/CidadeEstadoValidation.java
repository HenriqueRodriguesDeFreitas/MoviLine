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
        return estadoRepository.findById(estadoId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhum estado encontrado com o id passado."));

    }

    public Cidade validaCidadePorId(UUID cidadeId) {
        return cidadeRepository.findById(cidadeId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhuma cidade encontrada com o Id passado."));
    }

    public List<Cidade> retornaCidadesDoEstado(Estado estado) {
        return cidadeRepository.findByEstadoId(estado.getId());
    }

    /*Usar para o cadastro de uma nova cidade
     * Verifica se estado já possui uma cidade com nome passado.*/
    public boolean estadoPossuiCidadeComNomePassado(String nomeNovaCidade, UUID estadoId) {
        return cidadeRepository.existsByNomeIgnoreCaseAndEstadoId(nomeNovaCidade, estadoId);
    }

    /*Usar para metodo de deletar
     * Verifica se o estado já possui uma cidade com o id passado*/
    public boolean estadoPossuiCidadeComIdPassado(UUID cidadeId, UUID estadoId) {
        return cidadeRepository.existsByIdAndEstadoId(cidadeId, estadoId);
    }

    public boolean existeOutraCidadeComMesmoNome(String nome, UUID estadoId, UUID cidadeIdAtual) {
        return cidadeRepository.existsByNomeIgnoreCaseAndEstadoIdAndIdNot(nome, estadoId, cidadeIdAtual);
    }


}
