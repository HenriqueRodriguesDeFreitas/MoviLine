package com.vortex.EntregaRapida.controller;

import com.vortex.EntregaRapida.docs.ErroExamples;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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
            @ApiResponse(responseCode = "201",
                    description = "Estado cadastrado com sucesso!",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = EstadoResponseDto.class))),
            @ApiResponse(responseCode = "409",
                    description = "Já existe um estado com este nome",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(
                                    name = "Já existe um estado com este nome.",
                                    value = ErroExamples.ERRO_409
                            )}))
    })
    public ResponseEntity<EstadoResponseDto> cadastrarEstado(@RequestBody @Valid
                                                             EstadoRequestDto dto) {
        var estado = estadoService.cadastrarEstado(dto);
        return ResponseEntity.created(URI.create("/estado/" + estado.id())).body(estado);
    }

    @GetMapping
    @Operation(summary = "Buscar todos os estados", description = "Busca o id e nome do estado na base de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Dados dos estados retornada com sucesso",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = EstadoResponseDto.class),
                            examples = {@ExampleObject(
                                    name = "Estados retornados com sucesso!", value = """
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

    @GetMapping("/{id}")
    @Operation(summary = "Busca um estado de acordo com seu id.", description = "Retorna um estado específico de acordo com seu id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Busca retornada com sucesso, estado encontrado!",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = EstadoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Não existe estado com o id passado.",
                    content = @Content(mediaType = TYPE_JSON, schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Não existe estado com o Id passado.",
                                    value = ErroExamples.ERRO_404
                            )))
    })
    public ResponseEntity<EstadoResponseDto> buscarEstadoPorId(
            @PathVariable("id") UUID id) {
        return ResponseEntity.ok(estadoService.buscarEstadoPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o nome de uma estado.", description = "Busca um estado pelo id, caso encontrado, atualiza o nome.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o estado atualizado com sucesso.",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = EstadoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Não existe estado com o id passado.",
                    content = @Content(mediaType = TYPE_JSON, schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Não existe estado com o Id passado.",
                                    value = ErroExamples.ERRO_404
                            ))),
            @ApiResponse(responseCode = "409",
                    description = "Já existe um estado com este nome",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(
                                    name = "Já existe estado com o nome passado.",
                                    value = ErroExamples.ERRO_409
                            )}))
    })
    public ResponseEntity<EstadoResponseDto> atualizarEstado(@PathParam("id") UUID id,
                                                             @RequestBody String nome) {
        return ResponseEntity.ok(estadoService.atualizarEstado(id, nome));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Busca estados por nome", description = "Retorna todos os estados que possuam os caracteres passados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Dados dos estados retornada com sucesso",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = EstadoResponseDto.class),
                            examples = {@ExampleObject(
                                    name = "Estados retornados com sucesso!", value = """
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
    public ResponseEntity<List<EstadoResponseDto>> buscarPorNome(
            @PathVariable("nome") @NotBlank String nome) {
        return ResponseEntity.ok(estadoService.buscarEstadoPorNome(nome));
    }
}
