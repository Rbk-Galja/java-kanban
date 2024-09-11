package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    public void beforeEach() throws IOException {
        file = new File(PATH_TO_FILE, "backup.csv");
        fileManager = new FileBackedTaskManager(file);
        Files.createFile(Paths.get(PATH_TO_FILE, "backup.csv"));
    }

    @AfterEach
    public void afterEach() {
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
        assertEquals(saveTask.size(), 4, "Сохраняет неверное количество задач");

        Task testTask = CSVFormatter.fromString(saveTask.get(1));
        assertEquals(testTask, task, "Сохраняет неверную задачу");

        FileBackedTaskManager loadFile = FileBackedTaskManager.loadFromFile();
        taskList = loadFile.getTasks();
        epicList = loadFile.getEpic();
        subTasksList = loadFile.getSubTasks();

        assertEquals(taskList.size(), 1, "Возвращает неверное количество задач");
        assertEquals(epicList.size(), 1, "Возвращает неверное количество эпиков");
        assertEquals(subTasksList.size(), 1, "Возвращает неверное количество подзадач");

        Task loadTask = taskList.getFirst();
        assertEquals(loadTask, task, "Возвращает неверную задачу");

        Epic loadEpic = epicList.getFirst();
        assertEquals(loadEpic, epic, "Возвращает неверный эпик");

        SubTask loadSunTask = subTasksList.getFirst();
        assertEquals(loadSunTask, subTask, "Возвращает неверную подзадачу");
    }
}
