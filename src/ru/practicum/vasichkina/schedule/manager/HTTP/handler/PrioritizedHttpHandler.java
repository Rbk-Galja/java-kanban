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
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            try {
                String resp = HttpTaskServer.getGson().toJson(taskManager.getPrioritizedList());
                sendText(exchange, resp, 200);
            } catch (Exception e) {
                sendError500(exchange);
            }
        } else {
            System.out.println("Ждали метод GET, а получили " + method);
        }
        exchange.close();
    }
}
