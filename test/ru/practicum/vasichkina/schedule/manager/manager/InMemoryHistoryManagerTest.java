package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager historyManager;
    List<Task> history = new ArrayList<>();
    List<Task> newHistory = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        historyManager = Manager.getDefault();
    }

    @Test
    void shouldSaveTaskInHistory() {
        Task task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        historyManager.createTasks(task);
        assertNotNull(task, "Задача не найдена");
        historyManager.getTaskById(task.getId());

        history = historyManager.getHistory();
        assertEquals(1, history.size(), "Не сохраняет задачу в историю");
        Task task3 = history.getFirst();

        Task task2 = new Task(task.getId(), "Обновленное имя", "Обновленное описание",
                TasksStatus.IN_PROGRESS);
        historyManager.updateTasks(task2);
        assertNotNull(task2, "Задача не найдена");

        newHistory = historyManager.getHistory();
        assertEquals(1, newHistory.size(), "Неверное количество задач в истории");
        Task task4 = newHistory.getFirst();
        assertEquals(task3.getId(), task4.getId(), "Id задач не совпадают");
        assertEquals(task3.getName(), task4.getName(), "Имя задачи обновляется в истории");
        assertEquals(task3.getDescription(), task4.getDescription(), "Описание задачи обновляется в истории");
        assertEquals(task3.getStatus(), task4.getStatus(), "Статус задач обновляется в истории");
    }

    @Test
    void shouldSaveEpicInHistory() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        historyManager.creatEpic(epic);
        assertNotNull(epic, "Эпик не найден");
        historyManager.getEpicById(epic.getId());

        history = historyManager.getHistory();
        assertEquals(1, history.size(), "Не сохраняет задачу в историю");
        Task task = history.getFirst();

        Epic epic2 = new Epic("Новое имя", "Новое описание");
        historyManager.updateEpic(epic2);
        assertNotNull(epic2, "Эпик не найден");

        newHistory = historyManager.getHistory();
        assertEquals(1., newHistory.size(), "Неверное количество задач в истории");
        Task task1 = newHistory.getFirst();
        assertEquals(task.getId(), task1.getId(), "Id эпиков не совпадают");
        assertEquals(task.getName(), task1.getName(), "Имя эпика обновляется в истории");
        assertEquals(task.getDescription(), task1.getDescription(), "Описание эпика обновляется в истории");
        assertEquals(task.getStatus(), task1.getStatus(), "Статус эпика обновляется в истории");
    }

    @Test
    void shouldSaveSubTaskInHistory() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        historyManager.creatEpic(epic);
        assertNotNull(epic, "Эпик не найден");

        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                epic.getId());
        historyManager.creatSubtask(subTask);
        assertNotNull(subTask, "Подзадача не найдена");
        historyManager.getSubTaskById(subTask.getId());

        history = historyManager.getHistory();
        assertEquals(1, history.size(), "Неверное количество подзадач");
        Task task = history.getFirst();

        SubTask subTask1 = new SubTask(subTask.getId(),"Имя новое", "Описание новое",
                TasksStatus.IN_PROGRESS, subTask.getEpicId());
        historyManager.updateSubTasks(subTask1);
        assertNotNull(subTask1, "Подзадача не найдена");

        newHistory = historyManager.getHistory();
        Task task1 = newHistory.getFirst();
        assertEquals(task.getId(), task1.getId(), "Id подздач не совпадают");
        assertEquals(task.getName(), task1.getName(), "Имя подзадачи обновляется в истории");
        assertEquals(task.getDescription(), task1.getDescription(), "Описание подзадачи обновляется в истории");
        assertEquals(task.getStatus(), task1.getStatus(), "Статус подзадачи обновляется в истории");
    }
}