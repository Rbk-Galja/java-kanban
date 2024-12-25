package HttpTaskTest;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.HTTP.HttpTaskServer;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

abstract class BaseHttpTaskManagerTest {
    protected TaskManager taskManager;
    HttpTaskServer httpTaskServer;
    Gson gson;
    HttpClient client = HttpClient.newHttpClient();
    URI url;
    String uriSt = "http://localhost:8080";

    Epic epic = new Epic("Имя", "Описание");
    Task task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW, Duration.ofMinutes(15),
            LocalDateTime.of(2024, 10, 11, 12, 0));
    List<Epic> epics;
    List<SubTask> subTasks;
    List<Task> tasks;

    protected abstract TaskManager createTaskManager();

    @BeforeEach
    public void startServer() throws IOException {
        taskManager = createTaskManager();

        httpTaskServer = new HttpTaskServer(taskManager);
        gson = HttpTaskServer.getGson();
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        httpTaskServer.stop();
    }

    @Test
    @DisplayName("Создание задачи")
    abstract void shouldCreateTask() throws Exception;

    @Test
    @DisplayName("Получение всех задач")
    abstract void shouldGetAllTasks() throws Exception;

    @Test
    @DisplayName("Удаление задачи")
    abstract void shouldDeleteTask() throws Exception;

}

