package br.com.fiap.hora_certa.exception;

/** Recurso inexistente: vira 404 Not Found. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com id " + id + " não encontrado.");
    }
}
