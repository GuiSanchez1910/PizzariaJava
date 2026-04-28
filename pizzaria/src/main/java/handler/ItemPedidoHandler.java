    package handler;

    import com.sun.net.httpserver.*;

    import models.ItemPedido;
    import service.ItemPedidoService;

    import com.fasterxml.jackson.databind.ObjectMapper;

    import java.io.*;

    public class ItemPedidoHandler implements HttpHandler {

        private ObjectMapper mapper = new ObjectMapper();
        private ItemPedidoService service = new ItemPedidoService();

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String metodo = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            try {

                if (metodo.equals("GET") && path.matches("/itens-pedido/[^/]+")) {
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
                String json = mapper.writeValueAsString(service.listarItensPedido());
                enviar(exchange, 200, json);

            } catch (Exception e) {
                enviar(exchange, 500, "{\"erro\":\"Erro ao listar itens de pedido\"}");
            }
        }

        private void criar(HttpExchange exchange) throws IOException {

        try {
            var node = mapper.readTree(exchange.getRequestBody());

            ItemPedido ip = mapper.treeToValue(node, ItemPedido.class);

            String pedidoId = node.get("pedido") != null
                    ? node.get("pedido").get("id").asText()
                    : null;

            if (pedidoId == null || pedidoId.isEmpty()) {
                enviar(exchange, 400, "{\"erro\":\"Pedido é obrigatório\"}");
                return;
            }

            ItemPedido criado = service.criarItemPedido(ip, pedidoId);

            enviar(exchange, 201, mapper.writeValueAsString(criado));

        } catch (Exception e) {
            enviar(exchange, 400, "{\"erro\": \"" + e.getMessage() + "\"}");
        }
    }

        private void buscarPorId(HttpExchange exchange) throws IOException {

            try {
                String id = exchange.getRequestURI().getPath().split("/")[2];

                ItemPedido ip = service.buscarPorId(id);

                enviar(exchange, 200, mapper.writeValueAsString(ip));

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
                ItemPedido ip = mapper.readValue(exchange.getRequestBody(), ItemPedido.class);

                ItemPedido atualizado = service.atualizar(ip);

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

