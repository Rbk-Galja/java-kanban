package HttpTaskTest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.manager.InMemoryTaskManager;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpPrioritizedTest extends BaseHttpTaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    public void createURI() {
        url = URI.create(uriSt + "/prioritized");
    }

    @Override
    @Test
    @DisplayName("Добавление задачи в список")
    void shouldCreateTask() throws Exception {
        taskManager.createTasks(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидали код ответа 200");

        TypeToken<List<Task>> listTypeToken = new TypeToken<>() {
        };
        JsonElement jsonElement = JsonParser.parseString(response.body());
        tasks = gson.fromJson(jsonElement, listTypeToken.getType());
        assertEquals(tasks.size(), 1, "Неверное количество задач");
    }

    @Override
    @Test
    @DisplayName("Получение сортированного списка задач")
    void shouldGetAllTasks() throws Exception {
        taskManager.createTasks(task);
        Task task2 = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 10, 0));
        taskManager.createTasks(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидали код ответа 200");

        TypeToken<List<Task>> listTypeToken = new TypeToken<>() {
        };
        JsonElement jsonElement = JsonParser.parseString(response.body());
        tasks = gson.fromJson(jsonElement, listTypeToken.getType());
        assertEquals(tasks.size(), 2, "Неверное количество задач");
        assertEquals(task2.getId(), tasks.getFirst().getId(), "Задачи не расставлены по приоритету");
    }

    @Override
    @Test
    @DisplayName("Удаление задачи")
    void shouldDeleteTask() throws Exception {
        taskManager.createTasks(task);
        Task task2 = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 10, 0));
        taskManager.createTasks(task2);
        taskManager.deleteTask(task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидали код ответа 200");

        TypeToken<List<Task>> listTypeToken = new TypeToken<>() {
        };
        JsonElement jsonElement = JsonParser.parseString(response.body());
        tasks = gson.fromJson(jsonElement, listTypeToken.getType());
        assertEquals(tasks.size(), 1, "Неверное количество задач");
    }

}