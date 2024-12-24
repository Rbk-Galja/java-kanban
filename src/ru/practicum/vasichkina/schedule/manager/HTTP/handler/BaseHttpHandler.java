package ru.practicum.vasichkina.schedule.manager.HTTP.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void sendText(HttpExchange e, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        e.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        e.sendResponseHeaders(statusCode, resp.length);
        e.getResponseBody().write(resp);
        e.close();
    }

    protected Integer parsePathId(String path) {
        String[] paths = path.split("/");
        if (paths.length > 2) {
            return Integer.parseInt(paths[2]);
        }
        return -1;
    }

    public void sendError405(HttpExchange e) throws IOException {
        sendText(e, "Неверный формат id", 405);
    }

    public void sendError404(HttpExchange e) throws IOException {
        sendText(e, "Задача с таким id не найдена", 404);
    }

    public void sendError500(HttpExchange e) throws IOException {
        sendText(e, "Ошибка со стороны сервера", 500);
    }

    public void sendError406(HttpExchange e) throws IOException {
        sendText(e, "Задача пересекается с другими задачами", 406);
    }

    public void sendError400(HttpExchange e) throws IOException {
        sendText(e, "Неверный формат задачи", 400);
    }

}
