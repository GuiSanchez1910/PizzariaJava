package exception;

public class ValidacaoException extends RuntimeException {

    public ValidacaoException() {
        super("Erro na validação dos dados.");
    }

    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
