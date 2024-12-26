package ru.practicum.vasichkina.schedule.manager.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private Integer id;
    private String name;
    private String description;
    private TasksStatus status;
    //protected TaskType taskType = TaskType.TASK;
    protected Integer epicId;
    protected Duration durationTask = Duration.ofMinutes(15);

    protected LocalDateTime startTime;
   // protected LocalDateTime endTime;

    public Task(Integer id, String name, String description, TasksStatus status,
                Duration durationTask, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.durationTask = durationTask;
        this.startTime = startTime;
    }

    public Task(String name, String description, TasksStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.durationTask = Duration.ofMinutes(15);
        this.startTime = LocalDateTime.now();
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

   /* public Task(Integer id, String name, String description, TasksStatus status, TaskType taskType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = taskType;
    } */

    public Task(String name, String description, TasksStatus status, Duration durationTask, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.durationTask = durationTask;
        this.startTime = startTime;
    }

    public Task(Integer id, String name, String description, TasksStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public TasksStatus getStatus() {
        return status;
    }

    public void setStatus(TasksStatus status) {
        this.status = status;
    }

    public Integer getEpicIdSB() {
        return epicId;
    }

    public Duration getDurationTask() {
        return durationTask;
    }

    public void setDuration(Duration durationTask) {
        this.durationTask = durationTask;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(durationTask);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", durationTask=" + durationTask +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(epicId, task.epicId) && Objects.equals(durationTask, task.durationTask) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, epicId, durationTask, startTime);
    }
}
