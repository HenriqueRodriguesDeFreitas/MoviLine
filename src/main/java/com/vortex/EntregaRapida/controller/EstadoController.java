package com.vortex.EntregaRapida.controller;

import com.vortex.EntregaRapida.dto.request.EstadoRequestDto;
import com.vortex.EntregaRapida.dto.response.ErroResponseDto;
import com.vortex.EntregaRapida.dto.response.EstadoResponseDto;
import com.vortex.EntregaRapida.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("estado")
public class EstadoController {

    private final EstadoService estadoService;
    private static final String TYPE_JSON = "application/json";

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo estado", description = "Cria o cadastro de um novo estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Estado cadastrado com sucesso",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = EstadoResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "Conflito: Estado já cadastrado com o nome digitado",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(
                                    name = "Já existe um estado cadastrado com o nome passado", value = """
                                    {
                                    "data_hora" : "2025-09-01T14:45:04",
                                    "httpsValue" : "409",
                                    "erro" : "Erro: conflito de entidade",
                                    "descricao" : "Estado já cadastrado com o nome: Pará"
                                    }
                                    """)}))
    })
    public ResponseEntity<EstadoResponseDto> cadastrarEstado(@RequestBody @Valid
                                                             EstadoRequestDto dto) {
        return ResponseEntity.ok(estadoService.cadastrarEstado(dto));
    }

    @GetMapping
    @Operation(summary = "Buscar todos os estados", description = "Busca o id e nome do estado na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Dados dos estados retornada com sucesso",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = EstadoResponseDto.class),
                            examples = {@ExampleObject(
                                    name = "Estados retornados com sucesso.", value = """
                                    [{
                                    "id" : "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                    "nome" : "Pará"
                                    },
                                    {
                                    "id" : "4fa85f64-5717-4569-b3fc-2c963f66afa2",
                                    "nome" : "Paraná"
                                    }]
                                    """)}))
    })
    public ResponseEntity<List<EstadoResponseDto>> buscarTodosEstados() {
        return ResponseEntity.ok(estadoService.buscarTodosEstados());
    }
}
