package exception;

public class ItemPedidoNaoEncontradoException extends RuntimeException {

    public ItemPedidoNaoEncontradoException() {
        super("Item de pedido não encontrado.");
    }

    public ItemPedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
