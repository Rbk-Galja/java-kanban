package ru.practicum.vasichkina.schedule.manager.manager;

public class Manager {
    private Manager() {

    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
