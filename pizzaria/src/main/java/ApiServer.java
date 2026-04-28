import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import handler.ClienteHandler;
import handler.PizzaHandler;
import handler.ItemPedidoHandler;
import handler.PedidoHandler;

public class ApiServer {

	public static void main(String[] args) throws IOException {

		HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);

		server.createContext("/pizzas", new PizzaHandler());
		server.createContext("/clientes", new ClienteHandler());
		server.createContext("/itens-pedido", new ItemPedidoHandler());
		server.createContext("/pedidos", new PedidoHandler());

		server.setExecutor(null);
		server.start();

		System.out.println("Servidor rodando na porta 8001...");

	}

}
