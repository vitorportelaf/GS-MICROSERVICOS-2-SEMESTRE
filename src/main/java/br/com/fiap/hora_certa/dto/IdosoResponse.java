package br.com.fiap.hora_certa.dto;

import java.time.LocalDate;

public class IdosoResponse {

    private Long id;

    private String nome;

    private LocalDate dataNascimento;

    private String telefone;

    private Integer toleranciaMinutos;

    private String observacoes;

    private Long cuidadorId;

    private String cuidadorNome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCuidadorNome() {
        return cuidadorNome;
    }

    public void setCuidadorNome(String cuidadorNome) {
        this.cuidadorNome = cuidadorNome;
    }
}
