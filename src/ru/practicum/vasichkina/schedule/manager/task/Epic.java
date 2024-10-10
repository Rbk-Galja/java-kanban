package ru.practicum.vasichkina.schedule.manager.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected LocalDateTime endTimeEpic;
    protected TaskType taskType = TaskType.EPIC;
    private List<Integer> subTaskId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
    }

    public Epic(Integer id, String name, String description, TasksStatus status, Duration durationTask, LocalDateTime startTime) {
        super(id, name, description, status, durationTask, startTime);
    }

    public List<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(List<Integer> addToEpic) {
        this.subTaskId = addToEpic;
    }

    public LocalDateTime getEndTimeEpic() {
        return endTimeEpic;
    }

    public void setEndTimeEpic(LocalDateTime endTimeEpic) {
        this.endTimeEpic = endTimeEpic;
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
        Epic epic = (Epic) o;
        return Objects.equals(endTimeEpic, epic.endTimeEpic) && taskType == epic.taskType && Objects.equals(subTaskId, epic.subTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), endTimeEpic, taskType, subTaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "endTimeEpic=" + endTimeEpic +
                "} " + super.toString();
    }
}
