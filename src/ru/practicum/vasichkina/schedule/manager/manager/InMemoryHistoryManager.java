package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> taskHistory = new ArrayList<>();
    private static final int HISTORY_LENGTH = 10;

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        taskHistory.add(task);
        if(taskHistory.size() > HISTORY_LENGTH) {
            taskHistory.removeFirst();
        }
    }

    @Override
    public List<Task> getTasksHistory() {
        return taskHistory;
    }

}
