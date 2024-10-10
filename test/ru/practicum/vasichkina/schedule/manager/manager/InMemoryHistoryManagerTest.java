package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты менеджера сохранения истории задач")
class InMemoryHistoryManagerTest extends TaskManagerTest {

    List<Task> history = new ArrayList<>();
    List<Task> newHistory = new ArrayList<>();

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    @DisplayName("Пустая история")
    void emptyHistory() {
        history = taskManager.getHistory();
        assertEquals(0, history.size(), "Не возвращает пустую историю");
    }

    @Test
    @DisplayName("Добавление дублей в историю")
    void shouldNotDuplicatesInHistory() {
        taskManager.getTaskById(task.getId());
        taskManager.getSubTaskById(subTask.getId());
        taskManager.getTaskById(task.getId());
        history = taskManager.getHistory();

        assertEquals(2, history.size(), "Добавляет дубли в историю");
    }

    @Test
    @DisplayName("Добавление последней вызванной задачи в хвост")
    void shouldMoveViewedTaskTheTail() {
        taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskFromEpic(epic.getId());
        taskManager.getTaskById(task.getId());

        history = taskManager.getHistory();
        assertEquals(history.get(2), task, "Не добавляет в хвост последнюю вызванную задачу");

        taskManager.getEpicById(epic.getId());
        newHistory = taskManager.getHistory();
        assertEquals(newHistory.size(), 3, "Не удаляет предыдущий вызов задачи в истории");
        assertEquals(newHistory.get(2), epic, "Не сохраняет в хвост последнюю вызванную задачу");
    }

    @Test
    @DisplayName("Удаление из головы истории")
    void shouldUpdateTailHistory() {
        taskManager.getTaskById(task.getId());
        taskManager.getSubTaskById(subTask.getId());
        taskManager.getEpicById(epic.getId());

        taskManager.deleteTask(task.getId());
        history = taskManager.getHistory();
        assertEquals(2, history.size(), "Не удаляет задачу из головы");
    }

    @Test
    @DisplayName("Удаление из середины истории")
    void shouldDeleteMiddleHistory() {
        taskManager.getTaskById(task.getId());
        taskManager.getSubTaskById(subTask.getId());
        taskManager.getEpicById(epic.getId());

        taskManager.deleteSubTasks(subTask.getId());
        history = taskManager.getHistory();
        assertEquals(2, history.size(), "Не удаляет задачу из середины");
    }

    @Test
    @DisplayName("Удаление из хвоста истории")
    void shouldDeleteTailHistory() {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskById(subTask.getId());

        taskManager.deleteSubTasks(subTask.getId());
        history = taskManager.getHistory();
        assertEquals(2, history.size(), "Не удаляет задачу из хвоста");
    }

    @Override
    @Test
    @DisplayName("Сохранение задачи в историю при создании")
    void shouldCreateTask() {
        assertNotNull(task, "Задача не найдена");
        taskManager.getTaskById(task.getId());

        history = taskManager.getHistory();
        assertEquals(1, history.size(), "Не сохраняет задачу в историю");
        Task task1 = history.getFirst();
        assertEquals(task, task1, "Возвращает неверную задачу из истории");
    }

    @Override
    @Test
    @DisplayName("Сохранение эпика в истории при создании")
    void shouldCreateEpic() {
        assertNotNull(epic, "Эпик не найден");
        taskManager.getEpicById(epic.getId());

        history = taskManager.getHistory();
        assertEquals(1, history.size(), "Не сохраняет эпик в историю");
    }

    @Override
    @Test
    @DisplayName("Сохранение подзадачи в истории")
    void shouldCreateSubTask() {
        assertNotNull(epic, "Эпик не найден");

        assertNotNull(subTask, "Подзадача не найдена");
        taskManager.getSubTaskById(subTask.getId());

        history = taskManager.getHistory();
        assertEquals(1, history.size(), "Неверное количество подзадач");
        assertEquals(history.getFirst(), subTask, "Не сохраняет подзадачу в историю");
    }

    @Override
    @Test
    @DisplayName("Удаление задачи из истории")
    void shouldDeleteTask() {
        taskManager.getTasks();
        taskManager.deleteTask(task.getId());
        newHistory = taskManager.getHistory();
        assertTrue(newHistory.isEmpty(), "Не удаляет задачу из истории");
    }

    @Override
    @Test
    @DisplayName("Удаление эпика из истории")
    void shouldDeleteEpic() {
        taskManager.deleteEpic(epic.getId());
        newHistory = taskManager.getHistory();
        assertTrue(newHistory.isEmpty(), "Не удаляет эпик из истории");
    }

    @Override
    @Test
    @DisplayName("Удаление подзадачи из истории")
    void shouldDeleteSubTask() {
        taskManager.deleteSubTasks(subTask.getId());
        newHistory = taskManager.getHistory();
        assertTrue(newHistory.isEmpty(), "Не удаляет подзадачу из истории");
    }

}