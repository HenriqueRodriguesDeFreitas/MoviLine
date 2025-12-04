package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.CidadePorIdRequestDto;
import com.vortex.EntregaRapida.dto.request.CidadePorNomeRequestDto;
import com.vortex.EntregaRapida.dto.request.CidadeRequestDto;
import com.vortex.EntregaRapida.dto.response.CidadeResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.CidadeMapper;
import com.vortex.EntregaRapida.model.Cidade;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.CidadeRepository;
import com.vortex.EntregaRapida.repository.EstadoRepository;
import com.vortex.EntregaRapida.service.validation.CidadeValidator;
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

        List<Cidade> cidadesCadastradas = retornaCidadesDeUmEstado(estadoEncontrado);

        if (CidadeValidator.estadoPossuiCidade(dto.nome(), cidadesCadastradas)) {
            throw new ConflitoDeEntidadeException("Este estado já possui uma cidade com esse nome.");
        }

        Cidade novaCidade = new Cidade(dto.nome(), estadoEncontrado);
        return cidadeMapper.toResponse(cidadeRepository.save(novaCidade));
    }

    @Transactional
    public CidadeResponseDto atualizarCidade(UUID cidadeId, CidadeRequestDto dto) {
        Estado estadoEncontrado = retornaEstadoComIdPassado(dto.idEstado());

        Cidade cidadeEncontrada = retornaCidadeComIdPassado(cidadeId);

        List<Cidade> cidadesNoEstado = retornaCidadesDeUmEstado(estadoEncontrado);

        if (CidadeValidator.estadoPossuiCidade(dto.nome(), cidadesNoEstado, cidadeEncontrada)) {
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

    public List<CidadeResponseDto> buscarCidadesPorNome(CidadePorNomeRequestDto dto) {
        var estado = retornaEstadoComIdPassado(dto.estadoId());

        List<Cidade> cidades = cidadeRepository
                .buscarCidadePorEstadoENome(estado.getId(), dto.cidadeNome());

        return cidades.stream()
                .map(cidadeMapper::toResponse).toList();
    }

    public CidadeResponseDto buscarCidadePorId(UUID cidadeId) {
        Cidade cidade = retornaCidadeComIdPassado(cidadeId);
        return cidadeMapper.toResponse(cidade);
    }

    public void deletarCidade(UUID cidadeId) {
        var cidade = retornaCidadeComIdPassado(cidadeId);
        cidadeRepository.delete(cidade);
    }

    private Estado retornaEstadoComIdPassado(UUID estadoId) {
        return estadoRepository.buscarEstadoSimplesPorId(estadoId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhum estado encontrado com o id passado."));
    }

    private Cidade retornaCidadeComIdPassado(UUID cidadeId) {
        return cidadeRepository.buscarCidadeSimplesPorId(cidadeId)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhuma cidade encontrada com o Id passado."));
    }

    private List<Cidade> retornaCidadesDeUmEstado(Estado estadoEncontrado) {
        return cidadeRepository.buscarCidadesPorEstado(estadoEncontrado.getId());
    }
}
