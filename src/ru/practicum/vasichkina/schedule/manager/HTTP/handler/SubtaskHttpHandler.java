package ru.practicum.vasichkina.schedule.manager.HTTP.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.vasichkina.schedule.manager.HTTP.Endpoint;
import ru.practicum.vasichkina.schedule.manager.HTTP.HttpTaskServer;
import ru.practicum.vasichkina.schedule.manager.exceptions.InvalidTaskException;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SubtaskHttpHandler extends BaseHttpHandler {

    public SubtaskHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка запроса /subtasks от клиента");
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
        Integer id = parsePathId(exchange.getRequestURI().getPath());
        if (Endpoint.GET_SUBTASKS.equals(endpoint)) {
            List<SubTask> subTasks = taskManager.getSubTasks();
            String response = HttpTaskServer.getGson().toJson(subTasks);
            sendText(exchange, response, 200);
        } else if (Endpoint.GET_SUBTASKS_ID.equals(endpoint)) {
            if (id <= 0) {
                sendError405(exchange);
                return;
            }
            try {
                SubTask subTask = taskManager.getSubTaskById(id).orElseThrow();
                String response = HttpTaskServer.getGson().toJson(subTask);
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
        if (Endpoint.POST_SUBTASKS.equals(endpoint)) {
            handleAddMethod(exchange);
        } else if (Endpoint.POST_SUBTASKS_ID.equals(endpoint)) {
            handleUpdateMethod(exchange);
        }
    }

    public void handleAddMethod(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes());
            SubTask subTask = HttpTaskServer.getGson().fromJson(body, SubTask.class);
            taskManager.createSubtask(subTask);
            sendText(exchange, "Подзадача успешно создана", 201);
        } catch (InvalidTaskException e) {
            sendError406(exchange);
        } catch (RuntimeException e) {
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
            SubTask subTask = HttpTaskServer.getGson().fromJson(body, SubTask.class);
            if (id <= 0) {
                sendError405(exchange);
                return;
            }
            if (!subTask.getId().equals(id)) {
                sendError400(exchange);
                return;
            }
            taskManager.updateSubTasks(subTask);
            sendText(exchange, "Подзадача с id = " + id + " успешно обновлена", 201);
        } catch (InvalidTaskException e) {
            sendError406(exchange);
        } catch (RuntimeException e) {
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
        boolean isDelete = taskManager.deleteSubTasks(id);
        if (isDelete) {
            sendText(exchange, "Подзадача с id = " + id + " удалена", 200);
        } else {
            sendError404(exchange);
        }
    }
}


