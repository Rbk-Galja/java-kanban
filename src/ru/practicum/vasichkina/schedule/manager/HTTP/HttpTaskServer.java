package ru.practicum.vasichkina.schedule.manager.HTTP;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.vasichkina.schedule.manager.HTTP.adapter.*;
import ru.practicum.vasichkina.schedule.manager.HTTP.handler.*;
import ru.practicum.vasichkina.schedule.manager.manager.Manager;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.httpServer.createContext("/tasks", new TaskHttpHandler(taskManager));
        this.httpServer.createContext("/subtasks", new SubtaskHttpHandler(taskManager));
        this.httpServer.createContext("/epics", new EpicHttpHandler(taskManager));
        this.httpServer.createContext("/history", new HistoryHttpHandler(taskManager));
        this.httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager));
    }

    public void start() {
        System.out.println("Сервер запущен на порту " + PORT);
        System.out.println("http://localhost:" + PORT);
        httpServer.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен на порту " + PORT);
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Manager.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(TasksStatus.class, new TaskStatusAdapter());
        return gsonBuilder.create();
    }
}