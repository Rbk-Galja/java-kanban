package ru.practicum.vasichkina.schedule.manager;

import ru.practicum.vasichkina.schedule.manager.manager.InMemoryTaskManager;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        testsTasks();

    }

    public static void testsTasks() {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Epic epic = new Epic("Имя эпика", "Описание эпика");
        inMemoryTaskManager.createEpic(epic);
        System.out.println(epic);

        SubTask subTask = new SubTask("Имя подзадачи 1", "Описание подзадачи 1", TasksStatus.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 10, 7, 11, 11), epic.getId());
        inMemoryTaskManager.createSubtask(subTask);

        SubTask subTask1 = new SubTask("Имя подзадачи 2", "Описание подзадачи 2", TasksStatus.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 10, 8, 11, 20), epic.getId());
        inMemoryTaskManager.createSubtask(subTask1);

        SubTask subTask2 = new SubTask("Имя подзадачи 3", "Описание подзадачи 3", TasksStatus.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 10, 9, 11, 11), epic.getId());
        inMemoryTaskManager.createSubtask(subTask2);

        Epic epic2 = new Epic("Имя эпика2", "Описание эпика2");
        inMemoryTaskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Имя подзадачи 1 эпика 2", "Описание подзадачи 1 эпика 2", TasksStatus.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 10, 10, 11, 10), epic2.getId());
        inMemoryTaskManager.createSubtask(subTask4);

        SubTask subTask5 = new SubTask("Имя подзадачи 2 эпика 2", "Описание подзадачи 2 эпика 2", TasksStatus.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 10, 11, 11, 20), epic2.getId());
        inMemoryTaskManager.createSubtask(subTask5);
        System.out.println(inMemoryTaskManager.getSubTaskFromEpic(epic2.getId()));

        System.out.println("Выводим список");
        List<Task> schedule = inMemoryTaskManager.getPrioritizedList();
        schedule.stream()
                .peek(task -> System.out.println(task + "/n"))
                .toList();
    }
}

