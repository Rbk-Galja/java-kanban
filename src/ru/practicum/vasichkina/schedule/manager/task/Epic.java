package ru.practicum.vasichkina.schedule.manager.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    protected TaskType taskType = TaskType.EPIC;
    private List<Integer> subTaskId = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
    }

    public Epic(Integer id, String name, String description, TasksStatus status) {
        super(id, name, description, status);
    }

    public List<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(List<Integer> addToEpic) {
        this.subTaskId = addToEpic;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic epic)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(subTaskId, epic.subTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskId);
    }

}
