package ru.practicum.vasichkina.schedule.manager.HTTP.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.vasichkina.schedule.manager.HTTP.Endpoint;
import ru.practicum.vasichkina.schedule.manager.HTTP.HttpTaskServer;
import ru.practicum.vasichkina.schedule.manager.exceptions.InvalidTaskException;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHttpHandler extends BaseHttpHandler {

    public TaskHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка запроса /tasks от клиента");
        try (exchange) {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET":
                    handleGetMethod(exchange, path);
                    break;
                case "POST":
                    handlePostMethod(exchange, path);
                    break;
                case "DELETE":
                    handleDeleteMethod(exchange);
                    break;
                default:
                    System.out.println("Ждем метод GET, POST или DELETE, а получили" + requestMethod);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void handleGetMethod(HttpExchange exchange, String path) throws IOException {

        Endpoint endpoint = Endpoint.getEndpoint("GET", path);
        Integer id = parsePathId(exchange.getRequestURI().getPath());
        if (Endpoint.GET_TASKS.equals(endpoint)) {
            List<Task> tasks = taskManager.getTasks();
            String response = HttpTaskServer.getGson().toJson(tasks);
            sendText(exchange, response, 200);
        } else if (Endpoint.GET_TASKS_ID.equals(endpoint)) {
            if (id <= 0) {
                sendError405(exchange);
                return;
            }
            try {
                Task task = taskManager.getTaskById(id).orElseThrow();
                String response = HttpTaskServer.getGson().toJson(task);
                sendText(exchange, response, 200);
            } catch (RuntimeException e) {
                sendError404(exchange);
            } catch (Exception e) {
                sendError500(exchange);
            }
        }
    }

    public void handlePostMethod(HttpExchange exchange, String path) throws IOException {
        Endpoint endpoint = Endpoint.getEndpoint("POST", path);
        if (Endpoint.POST_TASKS.equals(endpoint)) {
            handleAddMethod(exchange);
        } else if (Endpoint.POST_TASKS_ID.equals(endpoint)) {
            handleUpdateMethod(exchange);
        }
    }

    public void handleAddMethod(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Task task = HttpTaskServer.getGson().fromJson(body, Task.class);
            taskManager.createTasks(task);
            sendText(exchange, "Задача успешно создана", 201);
        } catch (InvalidTaskException e) {
            sendError406(exchange);
        } catch (NullPointerException e) {
            sendError404(exchange);
        } catch (Exception e) {
            sendError500(exchange);
        }
    }

    public void handleUpdateMethod(HttpExchange exchange) throws IOException {
        Integer id = parsePathId(exchange.getRequestURI().getPath());
        try {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes());
            Task task = HttpTaskServer.getGson().fromJson(body, Task.class);
            if (id <= 0) {
                sendError405(exchange);
                return;
            }
            if (!task.getId().equals(id)) {
                sendError400(exchange);
                return;
            }
            taskManager.updateTasks(task);
            sendText(exchange, "Задача с id = " + id + " успешно обновлена", 201);
        } catch (InvalidTaskException e) {
            sendError406(exchange);
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
        boolean isDelete = taskManager.deleteTask(id);
        if (isDelete) {
            sendText(exchange, "Задача с id = " + id + " удалена", 200);
        } else {
            sendError404(exchange);
        }
    }
}
