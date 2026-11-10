package br.com.fiap.hora_certa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/** Corpo padronizado das respostas de erro (sem detalhes internos). */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        List<CampoInvalido> campos) {

    public record CampoInvalido(String campo, String mensagem) {
    }
}
