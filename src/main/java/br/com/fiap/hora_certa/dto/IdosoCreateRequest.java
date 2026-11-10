package br.com.fiap.hora_certa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Dados para cadastrar um idoso")
public class IdosoCreateRequest {

    @Schema(example = "Maria Souza")
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres.")
    private String nome;

    @Schema(example = "1948-03-15")
    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate dataNascimento;

    @Schema(example = "11912345678")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve ter 10 ou 11 dígitos, só números.")
    private String telefone;

    @Schema(description = "Minutos sem confirmar a dose até avisar o cuidador (padrão 30)", example = "30")
    @Min(value = 5, message = "A tolerância mínima é de 5 minutos.")
    @Max(value = 240, message = "A tolerância máxima é de 240 minutos.")
    private Integer toleranciaMinutos;

    @Schema(example = "Hipertensa e diabética, mora sozinha.")
    @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres.")
    private String observacoes;

    @Schema(description = "Id do cuidador responsável", example = "1")
    @NotNull(message = "O cuidador é obrigatório.")
    private Long cuidadorId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getToleranciaMinutos() {
        return toleranciaMinutos;
    }

    public void setToleranciaMinutos(Integer toleranciaMinutos) {
        this.toleranciaMinutos = toleranciaMinutos;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getCuidadorId() {
        return cuidadorId;
    }

    public void setCuidadorId(Long cuidadorId) {
        this.cuidadorId = cuidadorId;
    }
}
