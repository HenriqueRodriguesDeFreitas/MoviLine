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
import com.vortex.EntregaRapida.service.validation.CidadeEstadoValidator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CidadeService {

    private final CidadeRepository cidadeRepository;
    private final CidadeMapper cidadeMapper;
    private final CidadeEstadoValidator cidadeEstadoValidation;

    public CidadeService(CidadeRepository cidadeRepository,
                         CidadeMapper cidadeMapper,
                         CidadeEstadoValidator cidadeEstadoValidation) {
        this.cidadeRepository = cidadeRepository;
        this.cidadeMapper = cidadeMapper;
        this.cidadeEstadoValidation = cidadeEstadoValidation;
    }

    @Transactional
    public CidadeResponseDto cadastrarCidade(CidadeRequestDto dto) {
        Estado estadoEncontrado = retornaEstadoComIdPassado(dto.idEstado());

        if (cidadeEstadoValidation.estadoPossuiCidadeComNomePassado(dto.nome(), estadoEncontrado.getId())) {
            throw new ConflitoDeEntidadeException("Este estado já possui uma cidade com esse nome.");
        }

        Cidade novaCidade = new Cidade(dto.nome(), estadoEncontrado);
        return cidadeMapper.toResponse(cidadeRepository.save(novaCidade));
    }

    @Transactional
    public CidadeResponseDto atualizarCidade(UUID cidadeId, CidadeRequestDto dto) {
        Estado estadoEncontrado = retornaEstadoComIdPassado(dto.idEstado());
        Cidade cidadeEncontrada = retornaCidadeComIdPassado(cidadeId);

        boolean validacao = cidadeEstadoValidation.validaCidadePertenceAoEstado(cidadeEncontrada.getId(),
                estadoEncontrado.getId());

        if (!validacao) {
            throw new ConflitoEntidadeInexistente(
                    "O estado não possui a cidade informada."
            );
        }

        boolean nomeAlterado = !cidadeEncontrada.getNome()
                .equalsIgnoreCase(dto.nome());

        if (nomeAlterado) {
            if (cidadeEstadoValidation.existeOutraCidadeComMesmoNome(
                    dto.nome(), estadoEncontrado.getId(), cidadeEncontrada.getId())) {
                throw new ConflitoDeEntidadeException(
                        "Este estado já possui uma cidade com o nome passado."
                );
            }
        }
        cidadeEncontrada.setNome(dto.nome());
        cidadeEncontrada.setEstado(estadoEncontrado);

        return cidadeMapper.toResponse(cidadeRepository.save(cidadeEncontrada));
    }

    public Page<CidadeResponseDto> buscarTodasCidadeDeUmEstado(UUID idEstado, Pageable pageable) {
        return cidadeRepository.findByEstadoId(idEstado, pageable)
                .map(cidadeMapper::toResponse);
    }

    public Page<CidadeResponseDto> buscarCidadesPorNome(CidadePorNomeRequestDto dto, Pageable pageable) {
        var estado = retornaEstadoComIdPassado(dto.estadoId());

        return cidadeRepository
                .findByEstadoIdAndNomeIgnoreCaseContaining(estado.getId(), dto.cidadeNome(), pageable)
                .map(cidadeMapper::toResponse);
    }

    public CidadeResponseDto buscarCidadePorId(UUID cidadeId) {
        Cidade cidade = retornaCidadeComIdPassado(cidadeId);
        return cidadeMapper.toResponse(cidade);
    }

    public void deletarCidade(CidadePorIdRequestDto dto) {
        var estado = retornaEstadoComIdPassado(dto.estadoId());
        var cidade = retornaCidadeComIdPassado(dto.cidadeId());

        if (!cidadeEstadoValidation
                .validaCidadePertenceAoEstado(cidade.getId(), estado.getId())) {
            throw new ConflitoEntidadeInexistente("O estado não possui a cidade pesquisada.");
        }
        cidadeRepository.delete(cidade);

    }

    private Estado retornaEstadoComIdPassado(UUID estadoId) {
        return cidadeEstadoValidation.validaEstadoPorId(estadoId);
    }

    private Cidade retornaCidadeComIdPassado(UUID cidadeId) {
        return cidadeEstadoValidation.validaCidadePorId(cidadeId);
    }

}
