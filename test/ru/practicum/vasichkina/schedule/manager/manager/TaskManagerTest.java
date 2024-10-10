package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Epic epic;
    protected SubTask subTask;
    protected Task task;

    protected abstract T createTaskManager();

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = createTaskManager();
        epic = new Epic("Имя эпика", "Описание эпика");
        taskManager.createEpic(epic);

        subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.createSubtask(subTask);

        task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 12, 0));
        taskManager.createTasks(task);
    }



    @Test
    @DisplayName("Создание и получение задачи")
    abstract void shouldCreateTask() throws IOException;

    @Test
    @DisplayName("Создание и получение эпика")
    abstract void shouldCreateEpic() throws IOException;

    @Test
    @DisplayName("Создание и получение подзадачи")
    abstract void shouldCreateSubTask() throws IOException;

    @Test
    @DisplayName("Удаление задачи")
    abstract void shouldDeleteTask();

    @Test
    @DisplayName("Удаление эпика")
    abstract void shouldDeleteEpic();

    @Test
    @DisplayName("Удаление подзадачи")
    abstract void shouldDeleteSubTask();

}




