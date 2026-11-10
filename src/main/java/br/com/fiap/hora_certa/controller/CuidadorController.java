package br.com.fiap.hora_certa.controller;

import br.com.fiap.hora_certa.dto.CuidadorCreateRequest;
import br.com.fiap.hora_certa.dto.CuidadorResponse;
import br.com.fiap.hora_certa.dto.CuidadorUpdateRequest;
import br.com.fiap.hora_certa.dto.ErroResponse;
import br.com.fiap.hora_certa.service.CuidadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/${api.version}/cuidadores")
@Tag(name = "Cuidadores", description = "Familiares que recebem o alerta de dose não confirmada")
public class CuidadorController {

    private final CuidadorService service;

    public CuidadorController(CuidadorService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar cuidador")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuidador cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<CuidadorResponse> cadastrar(@Valid @RequestBody CuidadorCreateRequest dto) {
        CuidadorResponse criado = service.cadastrar(dto);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar cuidadores")
    @ApiResponse(responseCode = "200", description = "Lista de cuidadores")
    public ResponseEntity<List<CuidadorResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar cuidador pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuidador encontrado"),
            @ApiResponse(responseCode = "404", description = "Cuidador não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<CuidadorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cuidador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuidador atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cuidador não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<CuidadorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody CuidadorUpdateRequest dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cuidador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuidador excluído"),
            @ApiResponse(responseCode = "404", description = "Cuidador não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Cuidador possui idosos vinculados", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
