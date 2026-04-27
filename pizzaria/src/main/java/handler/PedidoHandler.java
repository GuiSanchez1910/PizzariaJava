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
                exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            byte[] response = "{\"erro\":\"Erro interno no servidor\"}".getBytes();
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(500, response.length);
            exchange.getResponseBody().write(response);
            exchange.getResponseBody().close();
        }
    }

    private void listar(HttpExchange exchange) throws Exception {
        
        String json = mapper.writeValueAsString(service.listarPedidos());
        enviar(exchange, json, 200);
    }

    private void criar(HttpExchange exchange) throws Exception {
        Pedido p = mapper.readValue(exchange.getRequestBody(), Pedido.class);
        Pedido criado = service.criarPedido(p);
        enviar(exchange, mapper.writeValueAsString(criado), 201);
    }

    private void buscarPorId(HttpExchange exchange) throws Exception {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        Pedido p = service.buscarPorId(id);

        if (p != null) {
            enviar(exchange, mapper.writeValueAsString(p), 200);
        } else {
            enviar(exchange, "{\"erro\":\"Pedido não encontrado\"}", 404);
        }
    }

    private void deletar(HttpExchange exchange) throws Exception {
        String id = exchange.getRequestURI().getPath().split("/")[2];
        service.deletar(id);
        enviar(exchange, "{\"mensagem\":\"Deletado\"}", 200);
    }

    private void atualizar(HttpExchange exchange) throws Exception {
        Pedido p = mapper.readValue(exchange.getRequestBody(), Pedido.class);
        Pedido atualizado = service.atualizar(p);
        enviar(exchange, mapper.writeValueAsString(atualizado), 200);
    }

    private void enviar(HttpExchange exchange, String resposta, int statusCode) throws IOException {
        byte[] bytes = resposta.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

}
