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
        Estado estadoEncontrado = retornaEstadoComIdPassado(dto.idEstado());

        List<Cidade> cidadesCadastradas = cidadeRepository.buscarCidadesPorEstado(estadoEncontrado.getId());

        if (verificarEstadoJaPossuiCidade(dto.nome(), cidadesCadastradas)) {
            throw new ConflitoDeEntidadeException("Este estado já possui uma cidade com esse nome.");
        }

        Cidade novaCidade = new Cidade(dto.nome(), estadoEncontrado);
        return cidadeMapper.toResponse(cidadeRepository.save(novaCidade));
    }

    @Transactional
    public CidadeResponseDto atualizarCidade(UUID cidadeId, CidadeRequestDto dto) {
        Estado estadoEncontrado = retornaEstadoComIdPassado(dto.idEstado());

        Cidade cidadeEncontrada = retornaCidadeComIdPassado(cidadeId);

        List<Cidade> cidadesNoEstado = cidadeRepository.buscarCidadesPorEstado(estadoEncontrado.getId());

        if (verificarEstadoJaPossuiCidade(dto.nome(), cidadesNoEstado, cidadeEncontrada)) {
            throw new ConflitoDeEntidadeException("Este estado já possui uma cidade com o nome passado.");
        }

        cidadeEncontrada.setNome(dto.nome());
        cidadeEncontrada.setEstado(estadoEncontrado);

        return cidadeMapper.toResponse(cidadeRepository.save(cidadeEncontrada));
    }

    public List<CidadeResponseDto> buscarTodasCidadeDeUmEstado(UUID idEstado) {
        List<Cidade> cidades = cidadeRepository.buscarCidadesPorEstado(idEstado);
        return cidades.stream()
                .map(cidadeMapper::toResponse).toList();
    }

    public List<CidadeResponseDto> buscarCidadesPorNome(String nome) {
        List<Cidade> cidades = cidadeRepository.buscarCidadesPorNomeContaining(nome);
        return cidades.stream()
                .map(cidadeMapper::toResponse).toList();
    }

    //Usado no serviço para cadastrar cidade
    private boolean verificarEstadoJaPossuiCidade(String nomeNovaCidade, List<Cidade> cidades) {
        return cidades.stream()
                .anyMatch(c -> c.getNome().equalsIgnoreCase(nomeNovaCidade));
    }

    //Usado no serviço para atualizar cidade
    private boolean verificarEstadoJaPossuiCidade(String novoNome, List<Cidade> cidades, Cidade cidadeEncontrada) {
        return cidades.stream()
                .anyMatch(c -> !c.getId().equals(cidadeEncontrada.getId())
                        && c.getNome().equalsIgnoreCase(novoNome));
    }

    private Cidade retornaCidadeComIdPassado(UUID cidadeId) {
        return cidadeRepository.buscarCidadeSimplesPorId(cidadeId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhuma cidade encontrada com o Id passado."));
    }
}
