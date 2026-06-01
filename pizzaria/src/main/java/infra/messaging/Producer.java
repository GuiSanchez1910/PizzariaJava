package infra.messaging;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import infra.config.RabbitMQConnection;
import models.Cliente;
import models.Endereco;
import models.ItemPedido;
import models.Pedido;
import models.Pizza;

public class Producer {
    private static final String QUEUE_NAME = "fila-pedidos";

    public static void main(String[] args) throws Exception {

        Cliente cliente = new Cliente(
                UUID.randomUUID().toString(),
                "João Silva",
                "(11) 99999-9999"
        );

        Endereco endereco = new Endereco();
        endereco.setCep("01001-000");
        endereco.setLogradouro("Praça da Sé");
        endereco.setBairro("Sé");
        endereco.setLocalidade("São Paulo");
        endereco.setUf("SP");

        Pizza pizzaCalabresa = new Pizza(
                UUID.randomUUID().toString(),
                "Calabresa",
                "Família",
                45.00
        );

        Pizza pizzaMussarela = new Pizza(
                UUID.randomUUID().toString(),
                "Mussarela",
                "Broto",
                40.00
        );

        ItemPedido item1 = new ItemPedido(
                UUID.randomUUID().toString(),
                pizzaCalabresa,
                2,
                pizzaCalabresa.getPrecoBase() * 2
        );

        ItemPedido item2 = new ItemPedido(
                UUID.randomUUID().toString(),
                pizzaMussarela,
                1,
                pizzaMussarela.getPrecoBase()
        );

        List<ItemPedido> itens = Arrays.asList(item1, item2);

        Pedido pedido1 = new Pedido(
                UUID.randomUUID().toString(),
                cliente,
                endereco,
                itens,
                LocalDateTime.now().toString(),
                "PENDENTE",
                endereco.getCep(),
                "123"
        );

        Cliente cliente2 = new Cliente(
                UUID.randomUUID().toString(),
                "Maria Oliveira",
                "(11) 98888-8888"
        );

        ItemPedido item3 = new ItemPedido(
                UUID.randomUUID().toString(),
                pizzaMussarela,
                3,
                pizzaMussarela.getPrecoBase() * 3
        );

        List<ItemPedido> itensPedido2 = Arrays.asList(item3);

        Pedido pedido2 = new Pedido(
                UUID.randomUUID().toString(),
                cliente2,
                endereco,
                itensPedido2,
                LocalDateTime.now().toString(),
                "PENDENTE",
                endereco.getCep(),
                "456"
        );

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        Connection connection = RabbitMQConnection.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(
                QUEUE_NAME,
                true,
                false,
                false,
                null);

        for (Pedido pedido : pedidos) {

            String mensagem = pedido.toString();

            channel.basicPublish(
                    "",
                    QUEUE_NAME,
                    null,
                    mensagem.getBytes());

            System.out.println("Pedido enviado:");
            System.out.println(mensagem);
        }

        channel.close();
        connection.close();
    }
}