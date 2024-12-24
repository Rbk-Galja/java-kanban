package ru.practicum.vasichkina.schedule.manager.HTTP.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.practicum.vasichkina.schedule.manager.HTTP.HttpTaskServer;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;

import java.io.IOException;

public class PrioritizedHttpHandler extends BaseHttpHandler {

    public PrioritizedHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка запроса /prioritized от клиента");
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            try {
                String response = HttpTaskServer.getGson().toJson(taskManager.getPrioritizedList());
                sendText(exchange, response, 200);
            } catch (Exception e) {
                sendError500(exchange);
            }
        } else {
            System.out.println("Ждем метом GET, а получили " + requestMethod);
        }
        exchange.close();
    }
}
