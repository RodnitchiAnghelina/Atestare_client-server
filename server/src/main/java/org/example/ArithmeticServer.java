package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ArithmeticServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/add", new AddHandler());
        server.createContext("/subtract", new SubtractHandler());
        server.createContext("/multiply", new MultiplyHandler());
        server.createContext("/divide", new DivideHandler());
        server.setExecutor(null);
        server.start();
    }

    static class AddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            int first = Integer.parseInt(params.get("first"));
            int second = Integer.parseInt(params.get("second"));
            int result = first + second;
            sendResponse(exchange, 200, Integer.toString(result));
        }
    }

    static class SubtractHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            int first = Integer.parseInt(params.get("first"));
            int second = Integer.parseInt(params.get("second"));
            int result = first - second;
            sendResponse(exchange, 200, Integer.toString(result));
        }
    }

    static class MultiplyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            int first = Integer.parseInt(params.get("first"));
            int second = Integer.parseInt(params.get("second"));
            int result = first * second;
            sendResponse(exchange, 200, Integer.toString(result));
        }
    }

    static class DivideHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            int first, second, result;
            try {
                first = Integer.parseInt(params.get("first"));
                second = Integer.parseInt(params.get("second"));
                if (second != 0) {
                    result = first / second;
                    sendResponse(exchange, 200, Integer.toString(result));
                } else {
                    sendResponse(exchange, 400, "Bad Request: Division by zero");
                }
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "Bad Request: Invalid input");
            }
        }
    }

    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            params.put(keyValue[0], keyValue[1]);
        }
        return params;
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

