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
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<CidadeResponseDto> buscarTodasCidadeDeUmEstado(UUID idEstado) {
        List<Cidade> cidades = cidadeRepository.findByEstadoId(idEstado);
        return cidades.stream()
                .map(cidadeMapper::toResponse).toList();
    }

    public List<CidadeResponseDto> buscarCidadesPorNome(CidadePorNomeRequestDto dto) {
        var estado = retornaEstadoComIdPassado(dto.estadoId());

        List<Cidade> cidades = cidadeRepository
                .findByEstadoIdAndNomeIgnoreCase(estado.getId(), dto.cidadeNome());

        return cidades.stream()
                .map(cidadeMapper::toResponse).toList();
    }

    public CidadeResponseDto buscarCidadePorId(UUID cidadeId) {
        Cidade cidade = retornaCidadeComIdPassado(cidadeId);
        return cidadeMapper.toResponse(cidade);
    }

    public void deletarCidade(CidadePorIdRequestDto dto) {
        var estado = retornaEstadoComIdPassado(dto.estadoId());
        var cidade = retornaCidadeComIdPassado(dto.cidadeId());

       if(!cidadeEstadoValidation
                .validaCidadePertenceAoEstado(cidade.getId(), estado.getId())){
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
