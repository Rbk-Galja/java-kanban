package HttpTest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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

@DisplayName("Тесты POST, GET, DELETE для TASK")
public class HttpTaskTest extends BaseHttpTaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Override
    @Test
    @DisplayName("Создание задачи")
    void shouldCreateTask() throws Exception {
        String taskJson = gson.toJson(task);
        url = URI.create(uriSt + "/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Ожидали код ответа 201");
    }

    @Test
    @DisplayName("Ошибка при создании задачи с пересечением по времени")
    void shouldReturnError406() throws Exception {
        taskManager.createTasks(task);
        Task task2 = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 12, 0));
        String taskJson = gson.toJson(task2);
        url = URI.create(uriSt + "/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode(), "Ожидали код ответа 406");
    }

    @Test
    @DisplayName("Получение задачи по id")
    void shouldGetTaskById() throws Exception {
        taskManager.createTasks(task);
        url = URI.create(uriSt + "/tasks" + "/" + task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        assertEquals(200, response.statusCode(), "Ожидали код ответа 200");
        assertEquals(task.getId(), jsonObject.get("id").getAsInt(), "Возвращает неверный id задачи");
        assertEquals(task.getName(), jsonObject.get("name").getAsString(), "Возвращает неверное имя задачи");
    }

    @Test
    @DisplayName("Ошибка при попытке получить несуществующую задачу")
    void shouldReturnError404() throws Exception {
        url = URI.create(uriSt + "/tasks/2");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Ожидали код ошибки 404");
    }

    @Override
    @Test
    @DisplayName("Получение всех задач")
    void shouldGetAllTasks() throws Exception {
        taskManager.createTasks(task);
        Task task2 = new Task("Имя задачи 2", "Описание задачи 2", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 11, 23, 13, 0));
        taskManager.createTasks(task2);
        url = URI.create(uriSt + "/tasks");

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
    }

    @Test
    @DisplayName("Обновление задачи")
    void shouldUpdateTask() throws Exception {
        taskManager.createTasks(task);
        Task updateTask = new Task(task.getId(), "Обновленное имя задачи", "Обновленное описание задачи",
                TasksStatus.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 23, 12, 0));

        String taskJson = gson.toJson(updateTask);
        url = URI.create(uriSt + "/tasks" + "/" + task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Ожидали код 201");

        Task expectedTask = taskManager.getTaskById(task.getId()).orElseThrow();

        assertEquals(expectedTask.getName(), "Обновленное имя задачи", "Не обновляет имя задачи");
        assertEquals(expectedTask.getStartTime(), LocalDateTime.of(2024, 10, 23, 12, 0),
                "Не обновляет время начала задачи");
    }

    @Override
    @Test
    @DisplayName("Удаление задачи")
    void shouldDeleteTask() throws Exception {
        taskManager.createTasks(task);

        url = URI.create(uriSt + "/tasks" + "/" + task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидали код 200");

        tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "Не удаляет задачу");

        url = URI.create(uriSt + "/tasks" + "/" + task.getId());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response2.statusCode(), "Ожидали код 404");
    }

}


