package com.vortex.EntregaRapida.controller;

import com.vortex.EntregaRapida.docs.ErroExamples;
import com.vortex.EntregaRapida.dto.request.CidadePorIdRequestDto;
import com.vortex.EntregaRapida.dto.request.CidadePorNomeRequestDto;
import com.vortex.EntregaRapida.dto.request.CidadeRequestDto;
import com.vortex.EntregaRapida.dto.response.CidadeResponseDto;
import com.vortex.EntregaRapida.dto.response.ErroResponseDto;
import com.vortex.EntregaRapida.dto.response.PageResponseDto;
import com.vortex.EntregaRapida.mapper.PageResponseMapper;
import com.vortex.EntregaRapida.service.CidadeService;
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

@Tag(name = "Cidade", description = "Operações relacionadas ao cadastro e manutenções de cidades")
@RestController
@RequestMapping("cidade")
public class CidadeController {

    private final CidadeService cidadeService;
    private static final String TYPE_JSON = "application/json";
    private final PageResponseMapper pageResponseMapper;

    public CidadeController(CidadeService cidadeService, PageResponseMapper pageResponseMapper) {
        this.cidadeService = cidadeService;
        this.pageResponseMapper = pageResponseMapper;
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova cidade", description = "Cadastra uma nova cidade em um estado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Estado cadastrado com sucesso!",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = CidadeResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Não existe estado com o id passado.",
                    content = @Content(mediaType = TYPE_JSON, schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Não existe estado com o id passado", value = ErroExamples.ERRO_404
                            ))),
            @ApiResponse(responseCode = "409",
                    description = "O estado já possui uma cidade com o mesmo nome.",
                    content = @Content(mediaType = TYPE_JSON, schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Estado já possui cidade com o mesmo nome",
                                    value = ErroExamples.ERRO_409
                            )))
    })

    public ResponseEntity<CidadeResponseDto> cadastrarCidade(@RequestBody @Valid
                                                             CidadeRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cidadeService.cadastrarCidade(dto));
    }

    @PutMapping("/{cidadeId}")
    @Operation(summary = "Atualiza dados de uma cidade", description = "Atualiza o nome e o estado a qual a cidade pertence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado atualizado com sucesso!"
                    , content = @Content(mediaType = TYPE_JSON, schema = @Schema(implementation = CidadeResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Não existe recurso com o Id passado.",
                    content = @Content(mediaType = TYPE_JSON, schema = @Schema(implementation = ErroResponseDto.class),
                            examples = {@ExampleObject(
                                    name = "Estado não encontrado com o Id passado.",
                                    value = ErroExamples.ERRO_404
                            ),
                                    @ExampleObject(
                                            name = "Cidade não encontrada com o id passado.",
                                            value = ErroExamples.ERRO_404
                                    )}
                    ))
    })
    public ResponseEntity<CidadeResponseDto> atualizarCidade(
            @PathVariable("cidadeId") UUID cidadeId,
            @RequestBody @Valid CidadeRequestDto dto) {
        return ResponseEntity.ok(cidadeService.atualizarCidade(cidadeId, dto));
    }

    @GetMapping("/estado/{idEstado}")
    @Operation(summary = "Buscar todos as cidades de um estado.", description = "Busca as cidades que pertencem a um estado específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Cidades de um estado especfíco retornadas com sucesso.",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = CidadeResponseDto.class),
                            examples = {@ExampleObject(
                                    name = "Cidades retornadas com sucesso!", value = """
                                    [{
                                    "id" : "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                    "nome" : "Breves"
                                    },
                                    {
                                    "id" : "4fa85f64-5717-4569-b3fc-2c963f66afa2",
                                    "nome" : "Belém"
                                    }]
                                    """)}))
    })
    public ResponseEntity<PageResponseDto<CidadeResponseDto>> buscarCidadesDeUmEstado(
            @PathVariable("idEstado") UUID idEstado,
            @ParameterObject Pageable pageable) {
        var page = cidadeService.buscarTodasCidadeDeUmEstado(idEstado, pageable);
        return ResponseEntity.ok(pageResponseMapper.toPageResponse(page));
    }

    @GetMapping("/nome")
    @Operation(summary = "Busca cidade por nome",
            description = "Busca cidade por nome, retorna todos que tenha os caracteres passados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Busca retornada com sucesso!",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = CidadeResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Cidades retornadas com sucesso!",
                                    value = """
                                            [{
                                            "id" : "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                            "nome" : "Breves"
                                            },
                                            {
                                            "id" : "4fa85f64-5717-4569-b3fc-2c963f66afa2",
                                            "nome" : "Belém"
                                            }]
                                            """
                            )))
    })
    public ResponseEntity<PageResponseDto<CidadeResponseDto>> buscarCidadesPorNome(
            @RequestParam UUID estadoid,
            @RequestParam String cidadeNome,
            @ParameterObject Pageable pageable) {
        var dto = new CidadePorNomeRequestDto(estadoid, cidadeNome);
        var page = cidadeService.buscarCidadesPorNome(dto, pageable);
        return ResponseEntity.ok(pageResponseMapper.toPageResponse(page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma cidade por Id", description = "Retorna a cidade que possui o Id passado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca retornada com sucesso!",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = CidadeResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Busca retornada com sucesso!",
                                    value = """
                                            {
                                            "id" : "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                            "nome" : "Breves"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "Nenhuma cidade encontrada com o Id passado.",
                    content = @Content(mediaType = TYPE_JSON,
                            schema = @Schema(implementation = ErroResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Nenhuma cidade encontrada com o id passado",
                                    value = ErroExamples.ERRO_404
                            )))
    })
    public ResponseEntity<CidadeResponseDto> buscarCidadePorId(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(cidadeService.buscarCidadePorId(id));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarCidade(@RequestBody @Valid CidadePorIdRequestDto dto) {
        cidadeService.deletarCidade(dto);
        return ResponseEntity.noContent().build();
    }
}
