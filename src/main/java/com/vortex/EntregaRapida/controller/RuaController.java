package com.vortex.EntregaRapida.controller;

import com.vortex.EntregaRapida.docs.ErroExamples;
import com.vortex.EntregaRapida.dto.request.RuaRequestDto;
import com.vortex.EntregaRapida.dto.request.RuasDeUmBairroRequestDto;
import com.vortex.EntregaRapida.dto.response.ErroResponseDto;
import com.vortex.EntregaRapida.dto.response.PageResponseDto;
import com.vortex.EntregaRapida.dto.response.RuaResponseDto;
import com.vortex.EntregaRapida.mapper.PageResponseMapper;
import com.vortex.EntregaRapida.service.RuaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Rua", description = "Operações relacionadas ao cadastro e manutenções de ruas.")
@RequestMapping("rua")
public class RuaController {

    private final RuaService ruaService;
    private static final String TYPE_JSON = "application/json";
    private static final String DESC_CODE_404 = "Erro de recurso não encontrado.";
    private static final String DESC_CODE_409 = "Erro de conflito entre entidades.";
    private static final String MSG_EstadoNaoPossuiCidadeComIdInformado = "Estado não possui cidade com id informado.";
    private static final String MSG_CidadeNaoPossuiBairroComIdInformado = "Cidade não possui bairro com id informado.";
    private final PageResponseMapper pageResponseMapper;

    public RuaController(RuaService ruaService, PageResponseMapper pageResponseMapper) {
        this.ruaService = ruaService;
        this.pageResponseMapper = pageResponseMapper;
    }

    @PostMapping
    @Operation(summary = "Cadastrar rua", description = "Método usado para cadastrar uma nova rua em um bairro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rua cadastrada com sucesso.",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = RuaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = DESC_CODE_404,
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {
                                    @ExampleObject(name = MSG_EstadoNaoPossuiCidadeComIdInformado,
                                            value = ErroExamples.ERRO_404),
                                    @ExampleObject(name = MSG_CidadeNaoPossuiBairroComIdInformado,
                                            value = ErroExamples.ERRO_404)
                            })),
            @ApiResponse(responseCode = "409", description = DESC_CODE_409,
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {
                                    @ExampleObject(name = "Bairro já possui rua com o mesmo nome.",
                                            value = ErroExamples.ERRO_409)
                            }))
    })
    public ResponseEntity<RuaResponseDto> cadastrarRua(@RequestBody @Valid RuaRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ruaService.cadastrarRua(request));
    }

    @PutMapping("/{ruaId}")
    @Operation(summary = "Atualizar rua", description = "Método utilizado para atualizar dados de uma rua")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rua atualizada com sucesso!",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = RuaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = DESC_CODE_404,
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(name = MSG_EstadoNaoPossuiCidadeComIdInformado,
                                    value = ErroExamples.ERRO_404),
                                    @ExampleObject(name = MSG_CidadeNaoPossuiBairroComIdInformado,
                                            value = ErroExamples.ERRO_404),
                                    @ExampleObject(name = "Bairro não possui rua com id informado.",
                                            value = ErroExamples.ERRO_404)})),
            @ApiResponse(responseCode = "409", description = DESC_CODE_409,
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(name = "Bairro já possui rua com mesmo nome.",
                                    value = ErroExamples.ERRO_409)}))
    })
    public ResponseEntity<RuaResponseDto> atualizarRua(@PathVariable("ruaId") UUID ruaId,
                                                       @RequestBody @Valid RuaRequestDto requestDto) {
        return ResponseEntity.ok(ruaService.atualizarRua(ruaId, requestDto));
    }

    @GetMapping
    @Operation(summary = "Buscar ruas", description = "Busca ruas de um bairro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca retornada com sucesso!",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = RuaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = DESC_CODE_404,
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(name = MSG_EstadoNaoPossuiCidadeComIdInformado,
                                    value = ErroExamples.ERRO_404),
                                    @ExampleObject(name = MSG_CidadeNaoPossuiBairroComIdInformado,
                                            value = ErroExamples.ERRO_404)}))

    })
    public ResponseEntity<PageResponseDto<RuaResponseDto>> buscaRuasDeBairro(@RequestBody @Valid RuasDeUmBairroRequestDto requestDto,
                                                                             @ParameterObject Pageable pageable) {
        var page = ruaService.buscarRuasDeUmBairro(requestDto, pageable);
        return ResponseEntity.ok(pageResponseMapper.toPageResponse(page));
    }

    @GetMapping("/buscaPorNome")
    @Operation(summary = "Busca ruas por nome", description = "Busca ruas de um bairro por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca retornada com sucesso!",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = RuaResponseDto.class))),
            @ApiResponse(responseCode = "404", description = DESC_CODE_404,
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(name = MSG_EstadoNaoPossuiCidadeComIdInformado,
                                    value = ErroExamples.ERRO_404),
                                    @ExampleObject(name = MSG_CidadeNaoPossuiBairroComIdInformado)}))
    })
    public ResponseEntity<PageResponseDto<RuaResponseDto>> buscaRuasPorNome(
            @RequestParam UUID estadoId,
            @RequestParam UUID cidadeId,
            @RequestParam UUID bairroId,
            @RequestParam String nome, @ParameterObject Pageable pageable) {
        var page = ruaService.buscarRuasPorNome(estadoId, cidadeId, bairroId, nome, pageable);
        return ResponseEntity.ok(pageResponseMapper.toPageResponse(page));
    }
}
