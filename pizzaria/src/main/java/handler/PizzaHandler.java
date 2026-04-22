package handler;

import com.sun.net.httpserver.*;

import models.Pizza;
import service.PizzaService;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.*;
import java.util.*;

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
                exchange.sendResponseHeaders(405, -1);
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            exchange.sendResponseHeaders(500, -1);
        }
    }

    private void listar(HttpExchange exchange) throws Exception {

        String json = mapper.writeValueAsString(service.listarPizzas());
        enviar(exchange, json);
    }

    private void criar(HttpExchange exchange) throws Exception {

        Pizza p = mapper.readValue(exchange.getRequestBody(), Pizza.class);

        Pizza criado = service.criarPizza(p);

        enviar(exchange, mapper.writeValueAsString(criado));
    }

    private void buscarPorId(HttpExchange exchange) throws Exception {

        String id = exchange.getRequestURI().getPath().split("/")[2];

        Pizza p = service.buscarPorId(id);

        enviar(exchange, mapper.writeValueAsString(p));
    }

    private void deletar(HttpExchange exchange) throws Exception {

        String id = exchange.getRequestURI().getPath().split("/")[2];

        service.deletar(id);

        enviar(exchange, "{\"mensagem\":\"Deletado\"}");
    }

    private void atualizar(HttpExchange exchange) throws Exception {

        Pizza p = mapper.readValue(exchange.getRequestBody(), Pizza.class);

        Pizza atualizado = service.atualizar(p);

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
