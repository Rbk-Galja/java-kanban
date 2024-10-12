package ru.practicum.vasichkina.schedule.manager.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private Integer epicId;
    protected TaskType taskType = TaskType.SUBTASK;

    public SubTask(Integer id, String name, String description, TasksStatus status,
                   Duration durationTask, LocalDateTime startTime, Integer epicId) {
        super(id, name, description, status, durationTask, startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TasksStatus status, Integer epicId) {
        super(name, description, status);
        this.durationTask = Duration.ofMinutes(15);
        this.startTime = LocalDateTime.now();
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TasksStatus status, Duration durationTask,
                   LocalDateTime startTime, Integer epicId) {
        super(name, description, status, durationTask, startTime);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicId, subTask.epicId) && taskType == subTask.taskType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId, taskType);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                "} " + super.toString();
    }
}
