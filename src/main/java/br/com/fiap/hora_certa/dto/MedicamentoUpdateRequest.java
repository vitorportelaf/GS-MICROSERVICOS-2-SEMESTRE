package br.com.fiap.hora_certa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

@Schema(description = "Dados para atualizar um medicamento (o idoso não muda)")
public class MedicamentoUpdateRequest {

    @Schema(example = "Losartana")
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @Schema(example = "50 mg - 1 comprimido")
    @NotBlank(message = "A dosagem é obrigatória.")
    @Size(max = 60, message = "A dosagem deve ter no máximo 60 caracteres.")
    private String dosagem;

    @Schema(type = "string", pattern = "HH:mm", example = "08:00")
    @NotNull(message = "O horário é obrigatório.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horario;

    @Schema(description = "Falar o lembrete em voz alta (padrão true)", example = "true")
    private Boolean lembretePorVoz;

    @Schema(description = "Lembrete ligado", example = "true")
    @NotNull(message = "Informe se o lembrete está ativo.")
    private Boolean ativo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Boolean getLembretePorVoz() {
        return lembretePorVoz;
    }

    public void setLembretePorVoz(Boolean lembretePorVoz) {
        this.lembretePorVoz = lembretePorVoz;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
