package ru.practicum.vasichkina.schedule.manager.task;

public class SubTask extends Task {

    private Integer epicId;
    protected TaskType taskType = TaskType.SUBTASK;

    public SubTask(Integer id, String name, String description, TasksStatus status, Integer epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TasksStatus status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
