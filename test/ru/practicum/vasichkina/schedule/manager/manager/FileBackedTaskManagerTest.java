package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.*;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FileBackedTaskManagerTest {

    private static final String PATH_TO_FILE = "./src";
    private static FileBackedTaskManager fileManager;
    private static File file;
    List<Task> taskList = new ArrayList<>();
    List<Epic> epicList = new ArrayList<>();
    List<SubTask> subTasksList = new ArrayList<>();

    @BeforeAll
    public static void beforeAll() throws IOException {
        file = new File(PATH_TO_FILE, "backup.csv");
        fileManager = new FileBackedTaskManager(file);
        Files.createFile(Paths.get(PATH_TO_FILE, "backup.csv"));
    }

    @AfterAll
    public static void afterAll() {
        file.delete();
    }

    @Test
    void shouldCreateAndLoadEmptyFile() {
        boolean fileCreate = file.isFile();
        assertTrue(fileCreate, "Не создает файл");

        FileBackedTaskManager fileLoad = FileBackedTaskManager.loadFromFile();
        taskList = fileLoad.getTasks();
        epicList = fileLoad.getEpic();
        subTasksList = fileLoad.getSubTasks();
        assertEquals(taskList.size(), 0, "Возвращает не пустой файл");
        assertEquals(epicList.size(), 0, "Возвращает не пустой файл");
        assertEquals(subTasksList.size(), 0, "Возвращает не пустой файл");
    }

    @Test
    void shouldFileSaveAndLoadTasks() throws IOException {
        Task task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        fileManager.createTasks(task);

        Epic epic = new Epic("Имя эпика", "Описание эпика");
        fileManager.createEpic(epic);

        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.IN_PROGRESS,
                epic.getId());
        fileManager.createSubtask(subTask);

        List<String> saveTask = Files.readAllLines(file.toPath());
        assertEquals(4, saveTask.size(), "Сохраняет неверное количество задач");

        Task testTask = CSVFormatter.fromString(saveTask.get(1));
        assertEquals(testTask, task, "Сохраняет неверную задачу");

        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile();
        taskList = loadFile.getTasks();
        epicList = loadFile.getEpic();
        subTasksList = loadFile.getSubTasks();

        assertEquals(1, taskList.size(), "Возвращает неверное количество задач");
        assertEquals(1, epicList.size(), "Возвращает неверное количество эпиков");
        assertEquals(1, subTasksList.size(), "Возвращает неверное количество подзадач");

        Task loadTask = taskList.getFirst();
        assertEquals(loadTask, task, "Возвращает неверную задачу");

        Epic loadEpic = epicList.getFirst();
        assertEquals(loadEpic, epic, "Возвращает неверный эпик");

        SubTask loadSunTask = subTasksList.getFirst();
        assertEquals(loadSunTask, subTask, "Возвращает неверную подзадачу");
    }
}
