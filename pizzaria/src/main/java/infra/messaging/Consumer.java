package infra.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import infra.config.RabbitMQConnection;

public class Consumer {
    private static final String QUEUE_NAME = "fila-pedidos";

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQConnection.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(
                QUEUE_NAME,
                true,
                false,
                false,
                null);

        System.out.println("Aguardando mensagens...");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String mensagem = new String(
                    delivery.getBody(),
                    "UTF-8");
            System.out.println(
                    "Mensagem recebida: "
                            + mensagem);
        };

        channel.basicConsume(
                QUEUE_NAME,
                true,
                callback,
                consumerTag -> {
                });
    }

}
