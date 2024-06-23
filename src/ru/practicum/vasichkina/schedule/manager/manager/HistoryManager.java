package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Task;

import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    List<Task> getTasksHistory();


}
