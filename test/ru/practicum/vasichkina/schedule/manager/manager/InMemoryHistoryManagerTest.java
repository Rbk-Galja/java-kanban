package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты менеджера сохранения истории задач")
class InMemoryHistoryManagerTest {

    private static TaskManager historyManager;
    List<Task> history = new ArrayList<>();
    List<Task> newHistory = new ArrayList<>();
    private static Task task;
    private static Epic epic;
    private static SubTask subTask;

    @BeforeEach
    public void beforeEach() {
        historyManager = Manager.getDefault();
        epic = new Epic("Имя эпика", "Описание эпика");
        historyManager.createEpic(epic);

        subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW, epic.getId());
        historyManager.createSubtask(subTask);

        task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        historyManager.createTasks(task);
    }

    @Test
    @DisplayName("Сохранение задачи в историю при создании")
    void shouldSaveTaskInHistory() {
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
    @DisplayName("Сохранение эпика в истории при создании")
    void shouldSaveEpicInHistory() {
        assertNotNull(epic, "Эпик не найден");
        historyManager.getEpicById(epic.getId());

        history = historyManager.getHistory();
        assertEquals(1, history.size(), "Не сохраняет эпик в историю");

        historyManager.deleteEpic(epic.getId());
        newHistory = historyManager.getHistory();
        assertTrue(newHistory.isEmpty(), "Не удаляет эпик из истории");
    }

    @Test
    @DisplayName("Сохранение подзадачи в истории")
    void shouldSaveSubTaskInHistory() {
        assertNotNull(epic, "Эпик не найден");

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
    @DisplayName("Добавление последней вызванной задачи в хвост")
    void shouldMoveViewedTaskTheTail() {
        assertNotNull(epic, "Эпик не найден");
        historyManager.getEpicById(epic.getId());

        assertNotNull(subTask, "Подзадача не найдена");
        List<SubTask> subTasks = historyManager.getSubTasks();
        assertEquals(subTasks.size(), 1, "Не сохраняет подзадачу");
        historyManager.getSubTaskFromEpic(epic.getId());

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

}