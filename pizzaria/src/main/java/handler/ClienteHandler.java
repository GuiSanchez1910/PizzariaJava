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
                enviar(exchange, 405, "{\"erro\":\"Método não permitido\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            enviar(exchange, 500, "{\"erro\":\"Erro interno no servidor\"}");
        }
    }

    private void listar(HttpExchange exchange) throws IOException {

        try {
            String json = mapper.writeValueAsString(service.listarClientes());
            enviar(exchange, 200, json);

        } catch (Exception e) {
            enviar(exchange, 500, "{\"erro\":\"Erro ao listar clientes\"}");
        }
    }

    private void criar(HttpExchange exchange) throws IOException {

        try {
            Cliente c = mapper.readValue(exchange.getRequestBody(), Cliente.class);

            Cliente criado = service.criarCliente(c);

            enviar(exchange, 201, mapper.writeValueAsString(criado));

        } catch (Exception e) {
            enviar(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private void buscarPorId(HttpExchange exchange) throws IOException {

        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            Cliente c = service.buscarPorId(id);

            enviar(exchange, 200, mapper.writeValueAsString(c));

        } catch (Exception e) {
            enviar(exchange, 404, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private void deletar(HttpExchange exchange) throws IOException {

        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            service.deletar(id);

            enviar(exchange, 200, "{\"mensagem\":\"Deletado\"}");

        } catch (Exception e) {
            enviar(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private void atualizar(HttpExchange exchange) throws IOException {

        try {
            Cliente c = mapper.readValue(exchange.getRequestBody(), Cliente.class);

            Cliente atualizado = service.atualizar(c);

            enviar(exchange, 200, mapper.writeValueAsString(atualizado));

        } catch (Exception e) {
            enviar(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private void enviar(HttpExchange exchange, int statusCode, String resposta) throws IOException {

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, resposta.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
}
