package com.vortex.EntregaRapida.service;

import com.vortex.EntregaRapida.dto.request.EstadoRequestDto;
import com.vortex.EntregaRapida.dto.response.EstadoResponseDto;
import com.vortex.EntregaRapida.exception.custom.ConflitoDeEntidadeException;
import com.vortex.EntregaRapida.mapper.EstadoMapper;
import com.vortex.EntregaRapida.model.Estado;
import com.vortex.EntregaRapida.repository.EstadoRepository;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;
    private static final String JA_EXISTE_ENTIDADE_COM_NOME = "%s já cadastrado com o nome: %s";

    public EstadoService(EstadoRepository estadoRepository,
                         EstadoMapper estadoMapper) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
    }

    public EstadoResponseDto cadastrarEstado(EstadoRequestDto dto) {
        estadoRepository.findByNomeIgnoreCase(dto.nome())
                .ifPresent(e -> {
                    String mensagemErroFormatado = String.format(
                            JA_EXISTE_ENTIDADE_COM_NOME, "Estado", dto.nome());
                    throw new ConflitoDeEntidadeException(mensagemErroFormatado);
                });

        Estado estado = estadoMapper.toEntity(dto);

        estado = estadoRepository.save(estado);

        return estadoMapper.toResponse(estado);
    }
}
