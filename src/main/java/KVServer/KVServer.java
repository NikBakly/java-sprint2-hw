package KVServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


public class KVServer {
    public static final int PORT = 8088;
    private String API_KEY;
    private HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() {
        API_KEY = generateApiKey();
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        triggerRegisterContext();
        triggerSaveContext();
        triggerLoadContext();
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_KEY: " + API_KEY);
        server.start();
    }

    public void stop() {
        System.out.println("Завершение работы сервера на порту " + PORT);
        System.out.println("Не работает в браузере http://localhost:" + PORT + "/");
        System.out.println("API_KEY: " + API_KEY);
        server.stop(0);
    }

    private void triggerRegisterContext() {
        server.createContext("/register", (h) -> {
            try (h) {
                System.out.println("\n/register");
                if ("GET".equals(h.getRequestMethod())) {
                    sendText(h, API_KEY);
                } else {
                    System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
                }
            }
        });
    }

    private void triggerSaveContext() {
        server.createContext("/save", (h) -> {
            try (h) {
                System.out.println("\n/save");
                if (!hasAuth(h)) {
                    System.out.println("Запрос не авторизован, нужен параметр в query API_KEY со значением апи-ключа");
                    h.sendResponseHeaders(403, 0);
                    return;
                }
                if ("POST".equals(h.getRequestMethod())) {
                    String key = h.getRequestURI().getPath().substring("/save/".length());
                    if (key.isEmpty()) {
                        System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                        h.sendResponseHeaders(400, 0);
                        return;
                    }
                    String value = readText(h);
                    if (value.isEmpty()) {
                        System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                        h.sendResponseHeaders(400, 0);
                        return;
                    }
                    data.put(key, value);
                    System.out.println("Значение для ключа " + key + " успешно обновлено!");
                    h.sendResponseHeaders(200, 0);
                } else {
                    System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
                }
            }
        });
    }

    private void triggerLoadContext() {
        server.createContext("/load", (h) -> {
            if (!hasAuth(h)) {
                System.out.println("Запрос не авторизован, нужен параметр в query API_KEY со значением апи-ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("GET".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/load/".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                if (!data.containsKey(key)) {
                    System.out.println("Ключ " + key + " не существует в базе");
                    h.sendResponseHeaders(404, 0);
                    return;
                }
                sendText(h, data.get(key));
                System.out.printf("Значение для ключа %s успешно обработано!\n", key);
                h.sendResponseHeaders(200, 0);

            } else {
                System.out.println("/load ждёт GET-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        });
    }

    private String generateApiKey() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_KEY=" + API_KEY) || rawQuery.contains("API_KEY=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), "UTF-8");
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        //byte[] resp = jackson.writeValueAsBytes(obj);
        byte[] resp = text.getBytes("UTF-8");
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
