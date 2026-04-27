package exception;

public class PizzaNaoEncontradaException extends RuntimeException {

    public PizzaNaoEncontradaException() {
        super("Pizza não encontrada.");
    }

    public PizzaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}