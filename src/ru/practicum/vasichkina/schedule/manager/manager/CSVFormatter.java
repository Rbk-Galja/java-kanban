package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.*;

import java.time.Duration;
import java.time.LocalDateTime;


public class CSVFormatter {

    private CSVFormatter() {

    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder()
                .append(task.getId()).append(",")
                .append(getTaskType(task)).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getStartTime()).append(",")
                .append(task.getDurationTask().toMinutes());
        if (getTaskType(task).equals(TaskType.SUBTASK)) {
            sb.append(",").append(task.getIdEpicSB());
        }
        return sb.toString();
    }

    public static Task fromString(String csvRom) {
        String[] str = csvRom.split(",");
        int id = Integer.parseInt(str[0]);
        TaskType type = TaskType.valueOf(str[1]);
        String name = str[2];
        TasksStatus status = TasksStatus.valueOf(str[3]);
        String description = str[4];
        LocalDateTime startTime = LocalDateTime.parse(str[5]);
        int duration = Integer.parseInt(str[6]);
        if (type.equals(TaskType.TASK)) {
            return new Task(id, name, description, status, Duration.ofMinutes(duration), startTime);
        } else if (type.equals(TaskType.SUBTASK)) {
            int epicId = Integer.parseInt(str[7]);
            return new SubTask(id, name, description, status, Duration.ofMinutes(duration), startTime, epicId);
        } else {
            return new Epic(id, name, description, status, Duration.ofMinutes(duration), startTime);
        }
    }

    protected static String getHeader() {
        return "id,type,name,status,description,duration,startTime,epic";
    }

    private static TaskType getTaskType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof SubTask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;
    }
}
