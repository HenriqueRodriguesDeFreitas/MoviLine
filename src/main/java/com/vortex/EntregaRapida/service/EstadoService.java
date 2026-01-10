package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.EstadoRequestDto;
import com.vortex.EntregaRapida.dto.response.EstadoResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.EstadoMapper;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.EstadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;

    public EstadoService(EstadoRepository estadoRepository,
                         EstadoMapper estadoMapper) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
    }

    @Transactional
    public EstadoResponseDto cadastrarEstado(EstadoRequestDto dto) {
        estadoRepository.findByNomeIgnoreCase(dto.nome())
                .ifPresent(e -> {
                    throw new ConflitoDeEntidadeException("Já existe estado com o nome passado.");
                });

        var estado = estadoMapper.toEntity(dto);

        estado = estadoRepository.save(estado);

        return estadoMapper.toResponse(estado);
    }

    @Transactional
    public EstadoResponseDto atualizarEstado(UUID idEstado, String novoNome) {
        Estado estado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhum estado encontrado com o id passado."));

        estadoRepository.findByNomeIgnoreCase(novoNome)
                .ifPresent(e -> {
                    if (!e.getNome().equalsIgnoreCase(estado.getNome()))
                        throw new ConflitoDeEntidadeException("Já existe estado com o nome passado.");
                });

        estado.setNome(novoNome);
        return estadoMapper.toResponse(estadoRepository.save(estado));
    }

    public Page<EstadoResponseDto> buscarEstadoPorNome(String nome, Pageable pageable) {
        return estadoRepository.findByNomeIgnoreCaseContaining(nome, pageable)
                .map(estadoMapper::toResponse);
    }

    public Page<EstadoResponseDto> buscarTodosEstados(Pageable pageable) {
       return estadoRepository.findAll(pageable)
                .map(estadoMapper::toResponse);
    }

    public EstadoResponseDto buscarEstadoPorId(UUID idEstado) {
        Estado estado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhum estado encontrado com o id: " + idEstado));
        return estadoMapper.toResponse(estado);
    }
}
