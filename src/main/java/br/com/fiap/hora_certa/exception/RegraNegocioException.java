package br.com.fiap.hora_certa.exception;

/** Operação que conflita com uma regra de negócio: vira 409 Conflict. */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
