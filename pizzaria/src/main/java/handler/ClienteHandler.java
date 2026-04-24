package handler;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import models.Cliente;
import service.ClienteService;

public class ClienteHandler implements HttpHandler{

    private ObjectMapper mapper = new ObjectMapper();
    private ClienteService service = new ClienteService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {

            if (metodo.equals("GET") && path.matches("/clientes/[^/]+")) {
                buscarPorId(exchange);

            } else if (metodo.equals("GET")) {
                listar(exchange);

            } else if (metodo.equals("POST")) {
                criar(exchange);

            } else if (metodo.equals("DELETE")) {
                deletar(exchange);

            } else if (metodo.equals("PUT")) {
                atualizar(exchange);

            } else {
                exchange.sendResponseHeaders(405, -1);
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void listar(HttpExchange exchange) throws Exception {

        String json = mapper.writeValueAsString(service.listarClientes());
        enviar(exchange, json);
    }

    private void criar(HttpExchange exchange) throws Exception {

        Cliente c = mapper.readValue(exchange.getRequestBody(), Cliente.class);

        Cliente criado = service.criarCliente(c);

        enviar(exchange, mapper.writeValueAsString(criado));
    }

    private void buscarPorId(HttpExchange exchange) throws Exception {

        String id = exchange.getRequestURI().getPath().split("/")[2];

        Cliente c = service.buscarPorId(id);

        enviar(exchange, mapper.writeValueAsString(c));
    }

    private void deletar(HttpExchange exchange) throws Exception {

        String id = exchange.getRequestURI().getPath().split("/")[2];

        service.deletar(id);

        enviar(exchange, "{\"mensagem\":\"Deletado\"}");
    }

    private void atualizar(HttpExchange exchange) throws Exception {

        Cliente c = mapper.readValue(exchange.getRequestBody(), Cliente.class);

        Cliente atualizado = service.atualizar(c);

        enviar(exchange, mapper.writeValueAsString(atualizado));
    }

    private void enviar(HttpExchange exchange, String resposta) throws IOException {

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resposta.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
    
}
