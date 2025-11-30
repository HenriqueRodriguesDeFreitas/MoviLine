package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.CidadeRequestDto;
import com.vortex.EntregaRapida.dto.response.CidadeResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.CidadeMapper;
import com.vortex.EntregaRapida.model.Cidade;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.CidadeRepository;
import com.vortex.EntregaRapida.repository.EstadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CidadeService {

    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;
    private final CidadeMapper cidadeMapper;

    public CidadeService(CidadeRepository cidadeRepository,
                         EstadoRepository estadoRepository,
                         CidadeMapper cidadeMapper) {
        this.cidadeRepository = cidadeRepository;
        this.estadoRepository = estadoRepository;
        this.cidadeMapper = cidadeMapper;
    }

    @Transactional
    public CidadeResponseDto cadastrarCidade(CidadeRequestDto dto) {
        Estado estadoEncontrado = buscaEstadoPorId(dto.idEstado());

        List<Cidade> cidadesCadastradas = cidadeRepository.buscarCidadesPorEstado(estadoEncontrado.getId());

        if (verificarEstadoJaPossuiCidade(dto.nome(), cidadesCadastradas)) {
            throw new ConflitoDeEntidadeException("Este estado já possui uma cidade com esse nome.");
        }

        Cidade novaCidade = new Cidade(dto.nome(), estadoEncontrado);
        return cidadeMapper.toResponse(cidadeRepository.save(novaCidade));
    }

    public List<CidadeResponseDto> buscarTodasCidadeDeUmEstado(UUID idEstado){
        List<Cidade> cidades = cidadeRepository.buscarCidadesPorEstado(idEstado);
        return cidades.stream()
                .map(cidadeMapper::toResponse).toList();
    }


    private boolean verificarEstadoJaPossuiCidade(String nomeNovaCidade, List<Cidade> cidades) {
        return cidades.stream()
                .anyMatch(c -> c.getNome().equalsIgnoreCase(nomeNovaCidade));
    }
}
