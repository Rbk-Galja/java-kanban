package ru.practicum.vasichkina.schedule.manager.HTTP.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.vasichkina.schedule.manager.HTTP.Endpoint;
import ru.practicum.vasichkina.schedule.manager.HTTP.HttpTaskServer;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHttpHandler extends BaseHttpHandler {

    public EpicHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка запроса /epics от клиента");
        try (exchange) {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    handleGetMethod(exchange, path);
                    break;
                case "POST":
                    handlePostMethod(exchange);
                    break;
                case "DELETE":
                    handleDeleteMethod(exchange);
                    break;
                default:
                    System.out.println("Ждем метод GET, POST или DELETE, а получили" + requestMethod);
                    throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void handleGetMethod(HttpExchange exchange, String path) throws IOException {
        Endpoint endpoint = Endpoint.getEndpoint("GET", path);
        if (Endpoint.GET_EPICS.equals(endpoint)) {
            handleGetAllEpicsMethod(exchange);
        } else if (Endpoint.GET_EPICS_ID.equals(endpoint)) {
            handleGetEpicMethod(exchange);
        } else if (Endpoint.GET_EPICS_ID_SUBTASKS.equals(endpoint)) {
            handleGetSubtaskByEpic(exchange);
        }
    }

    public void handleGetAllEpicsMethod(HttpExchange exchange) throws IOException {
        List<Epic> epics = taskManager.getEpic();
        String response = HttpTaskServer.getGson().toJson(epics);
        sendText(exchange, response, 200);
    }

    public void handleGetEpicMethod(HttpExchange exchange) throws IOException {
        Integer id = parsePathId(exchange.getRequestURI().getPath());
        if (id <= 0) {
            sendError405(exchange);
            return;
        }
        try {
            Epic epic = taskManager.getEpicById(id).orElseThrow();
            String response = HttpTaskServer.getGson().toJson(epic);
            sendText(exchange, response, 200);
        } catch (RuntimeException e) {
            sendError404(exchange);
        } catch (Exception e) {
            sendError500(exchange);
        }
    }

    public void handleGetSubtaskByEpic(HttpExchange exchange) throws IOException {
        Integer id = parsePathId(exchange.getRequestURI().getPath());
        if (id <= 0) {
            sendError405(exchange);
            return;
        }
        List<SubTask> epicsSubTask = taskManager.getSubTaskFromEpic(id);
        String response = HttpTaskServer.getGson().toJson(epicsSubTask);
        sendText(exchange, response, 200);
    }

    public void handlePostMethod(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = HttpTaskServer.getGson().fromJson(body, Epic.class);
            taskManager.createEpic(epic);
            sendText(exchange, "Эпик успешно создан", 201);
        } catch (NullPointerException e) {
            sendError404(exchange);
        } catch (Exception e) {
            sendError500(exchange);
        }
    }

    public void handleDeleteMethod(HttpExchange exchange) throws IOException {
        Integer id = parsePathId(exchange.getRequestURI().getPath());
        if (id <= 0) {
            sendError405(exchange);
            return;
        }
        boolean isDelete = taskManager.deleteEpic(id);
        if (isDelete) {
            sendText(exchange, "Эпик с id = " + id + " удален", 200);
        } else {
            sendError404(exchange);
        }
    }
}
