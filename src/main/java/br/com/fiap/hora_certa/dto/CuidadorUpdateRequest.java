package br.com.fiap.hora_certa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualizar um cuidador")
public class CuidadorUpdateRequest {

    @Schema(example = "Carlos Souza")
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres.")
    private String nome;

    @Schema(example = "carlos.souza@email.com")
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
    private String email;

    @Schema(example = "11987654321")
    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve ter 10 ou 11 dígitos, só números.")
    private String telefone;

    @Schema(example = "Filho")
    @Size(max = 40, message = "O parentesco deve ter no máximo 40 caracteres.")
    private String parentesco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}
