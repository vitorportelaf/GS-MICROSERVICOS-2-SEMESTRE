package br.com.fiap.hora_certa.controller;

import br.com.fiap.hora_certa.dto.DoseCreateRequest;
import br.com.fiap.hora_certa.dto.DoseResponse;
import br.com.fiap.hora_certa.dto.DoseUpdateRequest;
import br.com.fiap.hora_certa.dto.ErroResponse;
import br.com.fiap.hora_certa.service.DoseService;
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
@RequestMapping("api/${api.version}/doses")
@Tag(name = "Doses", description = "Registro das doses: tomadas, pendentes e não confirmadas")
public class DoseController {

    private final DoseService service;

    public DoseController(DoseService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastrar dose")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dose cadastrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Medicamento informado não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Medicamento inativo ou confirmação inválida", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<DoseResponse> cadastrar(@Valid @RequestBody DoseCreateRequest dto) {
        DoseResponse criado = service.cadastrar(dto);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    @Operation(summary = "Listar doses")
    @ApiResponse(responseCode = "200", description = "Lista de doses")
    public ResponseEntity<List<DoseResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar dose pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dose encontrada"),
            @ApiResponse(responseCode = "404", description = "Dose não encontrada", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<DoseResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dose")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dose atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dose não encontrada", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com regra de negócio", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<DoseResponse> atualizar(@PathVariable Long id, @Valid @RequestBody DoseUpdateRequest dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir dose")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dose excluída"),
            @ApiResponse(responseCode = "404", description = "Dose não encontrada", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
