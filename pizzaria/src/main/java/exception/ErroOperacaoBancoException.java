package exception;

public class ErroOperacaoBancoException extends RuntimeException {

    public ErroOperacaoBancoException() {
        super("Erro ao acessar o banco de dados.");
    }

    public ErroOperacaoBancoException(String mensagem) {
        super(mensagem);
    }

    public ErroOperacaoBancoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
