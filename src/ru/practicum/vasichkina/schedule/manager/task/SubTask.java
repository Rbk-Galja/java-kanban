package ru.practicum.vasichkina.schedule.manager.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    protected Integer epicIdSB;
    //protected TaskType taskType = TaskType.SUBTASK;

    public SubTask(Integer id, String name, String description, TasksStatus status,
                   Duration durationTask, LocalDateTime startTime, Integer epicIdSB) {
        super(id, name, description, status, durationTask, startTime);
        this.epicIdSB = epicIdSB;
    }

    public SubTask(String name, String description, TasksStatus status, Integer epicIdSB) {
        super(name, description, status);
        this.durationTask = Duration.ofMinutes(15);
        this.startTime = LocalDateTime.now();
        this.epicIdSB = epicIdSB;
    }

    public SubTask(String name, String description, TasksStatus status, Duration durationTask,
                   LocalDateTime startTime, Integer epicIdSB) {
        super(name, description, status, durationTask, startTime);
        this.epicIdSB = epicIdSB;
    }

    @Override
    public Integer getEpicIdSB() {
        return epicIdSB;
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "epicIdSB=" + epicIdSB +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epicIdSB, subTask.epicIdSB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicIdSB);
    }
}
