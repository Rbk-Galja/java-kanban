package ru.practicum.vasichkina.schedule.manager.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    protected Integer idEpicSB;
   // protected TaskType taskType = TaskType.SUBTASK;

    public SubTask(Integer id, String name, String description, TasksStatus status,
                   Duration durationTask, LocalDateTime startTime, Integer epicId) {
        super(id, name, description, status, durationTask, startTime);
        this.idEpicSB = epicId;
    }

    public SubTask(String name, String description, TasksStatus status, Integer epicId) {
        super(name, description, status);
        this.durationTask = Duration.ofMinutes(15);
        this.startTime = LocalDateTime.now();
        this.idEpicSB = epicId;
    }

    public SubTask(String name, String description, TasksStatus status, Duration durationTask,
                   LocalDateTime startTime, Integer epicId) {
        super(name, description, status, durationTask, startTime);
        this.idEpicSB = epicId;
    }

    @Override
    public Integer getIdEpicSB() {
        return idEpicSB;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "idEpicSB=" + idEpicSB +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(idEpicSB, subTask.idEpicSB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpicSB);
    }
}
