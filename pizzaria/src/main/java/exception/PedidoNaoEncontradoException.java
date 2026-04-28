package exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException() {
        super("Pedido não encontrado.");
    }

    public PedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
