import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import handler.PizzaHandler;


public class ApiServer {

	public static void main(String[] args) throws IOException {
		
		HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
		
		server.createContext("/pizzas", new PizzaHandler());
		
		server.setExecutor(null);
        server.start();

        System.out.println("Servidor rodando na porta 8001...");

	}

}
