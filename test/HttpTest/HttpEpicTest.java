package HttpTest;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.manager.InMemoryTaskManager;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.*;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тесты POST, GET, DELETE для EPIC")
public class HttpEpicTest extends BaseHttpTaskManagerTest {

    @Override
    protected TaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Override
    @Test
    @DisplayName("Создание эпика")
    void shouldCreateTask() throws Exception {
        String jsonEpic = gson.toJson(epic);
        url = URI.create(uriSt + "/epics");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Ожидали код ответа 201");
    }

    @Test
    @DisplayName("Получение эпика по id")
    void shouldGetTaskById() throws Exception {
        taskManager.createEpic(epic);
        url = URI.create(uriSt + "/epics" + "/" + epic.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        assertEquals(200, response.statusCode(), "Ожидали код ответа 200");
        assertEquals(epic.getId(), jsonObject.get("id").getAsInt(), "Возвращает неверный id задачи");
        assertEquals(epic.getName(), jsonObject.get("name").getAsString(), "Возвращает неверное имя задачи");
    }

    @Test
    @DisplayName("Ошибка при попытке получить несуществующий эпик")
    void shouldReturnError404() throws Exception {
        url = URI.create(uriSt + "/epics/2");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Ожидали код ошибки 404");
    }

    @Override
    @Test
    @DisplayName("Получение всех эпиков")
    void shouldGetAllTasks() throws Exception {
        taskManager.createEpic(epic);
        Epic epic2 = new Epic("Имя эпика 2", "Описание эпика 2");
        taskManager.createEpic(epic2);
        url = URI.create(uriSt + "/epics");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидали код ответа 200");

        TypeToken<List<Epic>> listTypeToken = new TypeToken<>() {
        };
        JsonElement jsonElement = JsonParser.parseString(response.body());
        epics = gson.fromJson(jsonElement, listTypeToken.getType());
        assertEquals(epics.size(), 2, "Неверное количество задач");
    }

    @Override
    @Test
    @DisplayName("Удаление эпика")
    void shouldDeleteTask() throws Exception {
        taskManager.createEpic(epic);

        url = URI.create(uriSt + "/epics" + "/" + epic.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидали код 200");

        epics = taskManager.getEpic();
        assertEquals(0, epics.size(), "Не удаляет задачу");

        url = URI.create(uriSt + "/epics" + "/" + epic.getId());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response2.statusCode(), "Ожидали код 404");
    }

    @Test
    @DisplayName("Получение списка подзадач эпика")
    void shouldGetSubtaskFromEpic() throws Exception {
        taskManager.createEpic(epic);
        SubTask subtask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subtask);
        SubTask subtask2 = new SubTask("Имя подзадачи 2", "Описание подзадачи 2", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2025, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subtask2);

        url = URI.create(uriSt + "/epics" + "/" + epic.getId() + "/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        TypeToken<List<Epic>> listTypeToken = new TypeToken<>() {
        };
        JsonElement jsonElement = JsonParser.parseString(response.body());
        subTasks = gson.fromJson(jsonElement, listTypeToken.getType());
        assertEquals(subTasks.size(), 2, "Неверное количество задач");
    }
}
