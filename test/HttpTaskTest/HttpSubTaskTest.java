package HttpTaskTest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.manager.InMemoryTaskManager;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тесты POST, GET, DELETE для SUBTASK")
public class HttpSubTaskTest extends BaseHttpTaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    public void createEpic() {
        taskManager.createEpic(epic);
    }

    @Override
    @Test
    @DisplayName("Создание подзадачи")
    void shouldCreateTask() throws Exception {
        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        String taskJson = gson.toJson(subTask);
        url = URI.create(uriSt + "/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Ожидали код ответа 201");
    }

    @Test
    @DisplayName("Ошибка при создании подзадачи с пересечением по времени")
    void shouldReturnError406() throws Exception {
        SubTask subTask2 = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subTask2);
        SubTask subTask3 = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        String taskJson = gson.toJson(subTask3);
        url = URI.create(uriSt + "/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode(), "Ожидали код ответа 406");
    }

    @Test
    @DisplayName("Получение подзадачи по id")
    void shouldGetTaskById() throws Exception {
        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subTask);
        url = URI.create(uriSt + "/subtasks" + "/" + subTask.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        assertEquals(200, response.statusCode(), "Ожидали код ответа 200");
        assertEquals(subTask.getId(), jsonObject.get("id").getAsInt(), "Возвращает неверный id задачи");
        assertEquals(subTask.getName(), jsonObject.get("name").getAsString(), "Возвращает неверное имя задачи");
    }

    @Test
    @DisplayName("Ошибка при попытке получить несуществующую подзадачу")
    void shouldReturnError404() throws Exception {
        url = URI.create(uriSt + "/subtasks/3");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Ожидали код ошибки 404");
    }

    @Override
    @Test
    @DisplayName("Получение всех подзадач")
    void shouldGetAllTasks() throws Exception {
        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subTask);
        SubTask subTask2 = new SubTask("Имя подзадачи 2", "Описание подзадачи 2", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2025, 10, 9, 12, 0), epic.getId());
        taskManager.createSubtask(subTask2);
        url = URI.create(uriSt + "/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидали код ответа 200");

        TypeToken<List<SubTask>> listTypeToken = new TypeToken<>() {
        };
        JsonElement jsonElement = JsonParser.parseString(response.body());
        subTasks = gson.fromJson(jsonElement, listTypeToken.getType());
        assertEquals(subTasks.size(), 2, "Неверное количество задач");
    }

    @Test
    @DisplayName("Обновление подзадачи")
    void shouldUpdateTask() throws Exception {
        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subTask);
        SubTask updateSubTask = new SubTask(subTask.getId(), "Обновленное имя задачи", "Обновленное описание задачи",
                TasksStatus.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2025, 10, 23, 12, 0), epic.getId());

        String taskJson = gson.toJson(updateSubTask);
        url = URI.create(uriSt + "/subtasks" + "/" + subTask.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Ожидали код 201");

        SubTask expectedSubTask = taskManager.getSubTaskById(updateSubTask.getId()).orElseThrow();

        assertEquals(expectedSubTask.getName(), "Обновленное имя задачи", "Не обновляет имя задачи");
        assertEquals(expectedSubTask.getStartTime(), LocalDateTime.of(2025, 10, 23, 12, 0),
                "Не обновляет время начала задачи");
    }

    @Override
    @Test
    @DisplayName("Удаление подзадачи")
    void shouldDeleteTask() throws Exception {
        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subTask);

        url = URI.create(uriSt + "/subtasks" + "/" + subTask.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидали код 200");

        subTasks = taskManager.getSubTasks();
        assertEquals(0, subTasks.size(), "Не удаляет задачу");

        url = URI.create(uriSt + "/subtasks" + "/" + subTask.getId());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response2.statusCode(), "Ожидали код 404");
    }

}
