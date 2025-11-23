package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.EstadoRequestDto;
import com.vortex.EntregaRapida.dto.response.EstadoResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.exception.custom.ConflitoEntidadeInexistente;
import com.vortex.EntregaRapida.mapper.EstadoMapper;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.EstadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Estado estado = estadoRepository.buscarEstadoSimplesPorId(idEstado)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhum estado encontrado com o id passado."));

        estadoRepository.findByNomeIgnoreCase(novoNome)
                .ifPresent(e -> {
                    if (!e.getNome().equalsIgnoreCase(estado.getNome()))
                        throw new ConflitoDeEntidadeException("Já existe estado com o nome passado.");
                });

        estado.setNome(novoNome);
        return estadoMapper.toResponse(estadoRepository.save(estado));
    }

    public List<EstadoResponseDto> buscarTodosEstados() {
        List<Estado> estados = estadoRepository.buscarTodosEstadosSimples();

        return estados.stream()
                .map(estadoMapper::toResponse).toList();
    }

    public EstadoResponseDto buscarEstadoPorId(UUID idEstado) {
        Estado estado = estadoRepository.buscarEstadoSimplesPorId(idEstado)
                .orElseThrow(() -> new ConflitoEntidadeInexistente("Nenhum estado encontrado com o id: " + idEstado));
        return estadoMapper.toResponse(estado);
    }
}
