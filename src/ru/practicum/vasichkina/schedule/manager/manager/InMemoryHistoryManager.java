package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_LENGTH = 10;
    private final List<Task> taskHistory = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        if (taskHistory.size() > HISTORY_LENGTH) {
            taskHistory.removeFirst();
        }
        taskHistory.add(task);
    }

    @Override
    public List<Task> getTasksHistory() {
        return taskHistory;
    }

}
