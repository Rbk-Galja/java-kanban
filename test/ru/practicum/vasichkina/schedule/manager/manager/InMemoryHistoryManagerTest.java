package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static TaskManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = Manager.getDefault();
    }


    @Test
    void shouldSavePrevTaskInHistory() {
        Task task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        Task task1Created = historyManager.createTasks(task);
        historyManager.getTaskById(task1Created.getId());
        assertNotNull(task1Created, "Задача не найдена");

        Task task2 = new Task(task1Created.getId(), "Обновленное имя", "Обновленное описание",
                TasksStatus.IN_PROGRESS);
        Task task2Update = historyManager.updateTasks(task2);
        assertNotNull(task2Update, "Задача не найдена");

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Длина списка в истории не совпадает" );
        assertEquals(task1Created, history.getFirst(), "Старая версия задачи не сохраняется в истории");
    }

}