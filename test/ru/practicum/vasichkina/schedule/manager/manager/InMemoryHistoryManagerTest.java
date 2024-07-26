package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.BeforeAll;
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
    private static Task task;
    private static Epic epic;
    private static SubTask subTask;

    @BeforeAll
    static void beforeAll() {
        task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        epic = new Epic("Имя эпика", "Описание эпика");
        subTask = create();
    }

    @BeforeEach
    public void beforeEach() {
        historyManager = Manager.getDefault();
    }

    @Test
    void shouldSaveTaskInHistory() {
        historyManager.createTasks(task);
        assertNotNull(task, "Задача не найдена");
        historyManager.getTaskById(task.getId());

        history = historyManager.getHistory();
        assertEquals(1, history.size(), "Не сохраняет задачу в историю");
        Task task1 = history.getFirst();
        assertEquals(task, task1, "Возвращает неверную задачу из истории");

        historyManager.deleteTask(task.getId());
        newHistory = historyManager.getHistory();
        assertTrue(newHistory.isEmpty(), "Не удаляет задачу из истории");
    }

    @Test
    void shouldSaveEpicInHistory() {
        historyManager.createEpic(epic);
        assertNotNull(epic, "Эпик не найден");
        historyManager.getEpicById(epic.getId());

        history = historyManager.getHistory();
        assertEquals(1, history.size(), "Не сохраняет задачу в историю");

        historyManager.deleteEpic(epic.getId());
        newHistory = historyManager.getHistory();
        assertTrue(newHistory.isEmpty(), "Не удаляет эпик из истории");
    }

    @Test
    void shouldSaveSubTaskInHistory() {
        historyManager.createEpic(epic);
        assertNotNull(epic, "Эпик не найден");

        historyManager.createSubtask(subTask);
        assertNotNull(subTask, "Подзадача не найдена");
        historyManager.getSubTaskById(subTask.getId());

        history = historyManager.getHistory();
        assertEquals(1, history.size(), "Неверное количество подзадач");
        assertEquals(history.getFirst(), subTask, "Не сохраняет подзадачу в историю");

        historyManager.deleteSubTasks(subTask.getId());
        newHistory = historyManager.getHistory();
        assertTrue(newHistory.isEmpty(), "Не удаляет подзадачу из истории");
    }

    @Test
    void shouldMoveViewedTaskTheTail() {
        historyManager.createEpic(epic);
        assertNotNull(epic, "Эпик не найден");
        historyManager.getEpicById(epic.getId());

        historyManager.createSubtask(subTask);
        assertNotNull(subTask, "Подзадача не найдена");
        List<SubTask> subTasks = historyManager.getSubTasks();
        assertEquals(subTasks.size(), 1, "Не сохраняет подзадачу");
        historyManager.getSubTaskFromEpic(epic.getId());

        historyManager.createTasks(task);
        assertNotNull(task, "Задача не найдена");
        historyManager.getTaskById(task.getId());

        history = historyManager.getHistory();
        assertEquals(history.size(), 3, "Не сохраняет все задачи в историю");
        assertEquals(history.get(2), task, "Не добавляет в хвост последнюю вызванную задачу");

        historyManager.getEpicById(epic.getId());
        newHistory = historyManager.getHistory();
        assertEquals(newHistory.size(), 3, "Не удаляет предыдущий вызов задачи в истории");
        assertEquals(newHistory.get(2), epic, "Не сохраняет в хвост последнюю вызванную задачу");
    }

    private static SubTask create() {
        historyManager = Manager.getDefault();
        Epic epic1 = new Epic("И", "О");
        historyManager.createEpic(epic1);
        return new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW, epic1.getId());
    }

}