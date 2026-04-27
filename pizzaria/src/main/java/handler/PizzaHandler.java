package handler;

import com.sun.net.httpserver.*;

import models.Pizza;
import service.PizzaService;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;

public class PizzaHandler implements HttpHandler {

    private ObjectMapper mapper = new ObjectMapper();
    private PizzaService service = new PizzaService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {

            if (metodo.equals("GET") && path.matches("/pizzas/[^/]+")) {
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
            String json = mapper.writeValueAsString(service.listarPizzas());
            enviar(exchange, 200, json);

        } catch (Exception e) {
            enviar(exchange, 500, "{\"erro\":\"Erro ao listar pizzas\"}");
        }
    }

    private void criar(HttpExchange exchange) throws IOException {

        try {
            Pizza p = mapper.readValue(exchange.getRequestBody(), Pizza.class);

            Pizza criado = service.criarPizza(p);

            enviar(exchange, 201, mapper.writeValueAsString(criado));

        } catch (Exception e) {
            enviar(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private void buscarPorId(HttpExchange exchange) throws IOException {

        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            Pizza p = service.buscarPorId(id);

            enviar(exchange, 200, mapper.writeValueAsString(p));

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
            Pizza p = mapper.readValue(exchange.getRequestBody(), Pizza.class);

            Pizza atualizado = service.atualizar(p);

            enviar(exchange, 200, mapper.writeValueAsString(atualizado));

        } catch (Exception e) {
            enviar(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private void enviar(HttpExchange exchange, int status, String resposta) throws IOException {

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, resposta.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
    
}
