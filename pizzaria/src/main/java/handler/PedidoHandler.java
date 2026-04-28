package handler;

import com.sun.net.httpserver.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Pedido;
import service.PedidoService;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PedidoHandler implements HttpHandler {

    private ObjectMapper mapper = new ObjectMapper();
    private PedidoService service = new PedidoService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            
            if (metodo.equals("GET") && path.matches("/pedidos/[^/]+")) {
                buscarPorId(exchange);
            } else if (metodo.equals("GET")) {
                listar(exchange);
            } else if (metodo.equals("POST")) {
                criar(exchange);
            } else if (metodo.equals("DELETE") && path.matches("/pedidos/[^/]+")) {
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
            String json = mapper.writeValueAsString(service.listarPedidos());
            enviar(exchange, 200, json);
        } catch (Exception e) {
            enviar(exchange, 500, "{\"erro\":\"Erro ao listar pedidos\"}");
        }
    }

    private void criar(HttpExchange exchange) throws IOException {
        
        try {
            Pedido p = mapper.readValue(exchange.getRequestBody(), Pedido.class);
            Pedido criado = service.criarPedido(p);
            enviar(exchange, 201, mapper.writeValueAsString(criado));
        } catch (Exception e) {
            enviar(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private void buscarPorId(HttpExchange exchange) throws IOException {

        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            Pedido p = service.buscarPorId(id);
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
            Pedido p = mapper.readValue(exchange.getRequestBody(), Pedido.class);
            Pedido atualizado = service.atualizar(p);
            enviar(exchange, 200, mapper.writeValueAsString(atualizado));
        } catch (Exception e) {
            enviar(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

    private void enviar(HttpExchange exchange, int statusCode, String resposta) throws IOException {
        byte[] bytes = resposta.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}