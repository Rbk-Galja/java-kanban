package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.*;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест менеджера создания файла бэкапа")
public class FileBackedTaskManagerTest extends TaskManagerTest {

    private static final String PATH_TO_FILE = "./resources";
    private static File file;
    List<Task> taskList = new ArrayList<>();
    List<Epic> epicList = new ArrayList<>();
    List<SubTask> subTasksList = new ArrayList<>();
    List<String> save = new ArrayList<>();

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Override
    @BeforeEach
    public void setUp() throws IOException {
        file = new File(PATH_TO_FILE, "backup.csv");
        Files.createFile(file.toPath());
        taskManager = new FileBackedTaskManager(file);
        epic = new Epic("Имя эпика", "Описание эпика");
        taskManager.createEpic(epic);

        subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subTask);

        task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 12, 0));
        taskManager.createTasks(task);
    }

    @AfterEach
    public void afterEach() {
        file.delete();
    }

    @Test
    @DisplayName("Создание и загрузка пустого файла")
    void shouldCreateAndLoadEmptyFile() {
        boolean fileCreate = file.isFile();
        assertTrue(fileCreate, "Не создает файл");

        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpics();

        FileBackedTaskManager fileLoad = FileBackedTaskManager.loadFromFile();

        taskList = fileLoad.getTasks();
        epicList = fileLoad.getEpic();
        subTasksList = fileLoad.getSubTasks();
        assertEquals(taskList.size(), 0, "Возвращает не пустой файл");
        assertEquals(epicList.size(), 0, "Возвращает не пустой файл");
        assertEquals(subTasksList.size(), 0, "Возвращает не пустой файл");
    }

    @Override
    @Test
    @DisplayName("Сохранение и выгрузка задачи из файла")
    void shouldCreateTask() throws IOException {
        taskManager.deleteAllEpics();
        save = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, save.size(), "Сохраняет неверное количество задач");

        Task testTask = CSVFormatter.fromString(save.get(1));
        assertEquals(testTask, task, "Сохраняет неверную задачу");

        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile();
        taskList = loadFile.getTasks();
        assertEquals(1, taskList.size(), "Возвращает неверное количество задач");
    }

    @Override
    @Test
    @DisplayName("Сохранение и выгрузка эпика из файла")
    void shouldCreateEpic() throws IOException {
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllTasks();
        save = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, save.size(), "Сохраняет неверное количество эпиков'");

        Epic testEpic = (Epic) CSVFormatter.fromString(save.get(1));
        assertEquals(testEpic.getId(), epic.getId(), "Сохраняет неверный эпик");

        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile();
        epicList = loadFile.getEpic();
        assertEquals(1, epicList.size(), "Возвращает неверное количество эпиков");
    }

    @Override
    @Test
    @DisplayName("Сохранение и выгрузка подзадачи из файла")
    void shouldCreateSubTask() throws IOException {
        taskManager.deleteAllTasks();
        save = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        assertEquals(save.size(),3, "Сохраняет неверное количество подзадач'");

        SubTask testSubTask = (SubTask) CSVFormatter.fromString(save.get(2));
        assertEquals(testSubTask.getId(), subTask.getId(), "Сохраняет неверную подзадачу");

        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile();
        subTasksList = loadFile.getSubTasks();
        assertEquals(1, subTasksList.size(), "Возвращает неверное количество подзадач");
    }

    @Override
    @Test
    @DisplayName("Удаление задачи из файла")
    void shouldDeleteTask() {
        taskManager.deleteAllTasks();
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile();
        taskList = loadFile.getTasks();
        assertEquals(taskList.size(), 0, "Не удаляет задачу");
    }

    @Override
    @Test
    @DisplayName("Удаление эпика")
    void shouldDeleteEpic() {
        taskManager.deleteAllEpics();
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile();
        epicList = loadFile.getEpic();
        assertEquals(epicList.size(), 0, "Не удаляет эпик");
    }

    @Override
    @Test
    @DisplayName("Удаление подзадачи")
    void shouldDeleteSubTask() {
        taskManager.deleteAllSubTasks();
        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile();
        subTasksList = loadFile.getSubTasks();
        assertEquals(subTasksList.size(), 0, "Не удаляет подзадачу");
    }
}
