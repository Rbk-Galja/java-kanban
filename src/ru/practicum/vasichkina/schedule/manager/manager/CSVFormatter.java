package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.*;


public class CSVFormatter {

    private CSVFormatter() {

    }

    public static String toString(Task task) {
        StringBuilder sb = new StringBuilder()
                .append(task.getId()).append(",")
                .append(task.getTaskType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription());
        if (task instanceof SubTask) {
            sb.append(",").append(task.getEpicId());
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
        if (type.equals(TaskType.TASK)) {
            return new Task(id, name, description, status);
        } else if (type.equals(TaskType.SUBTASK)) {
            int epicId = Integer.parseInt(str[5]);
            return new SubTask(id, name, description, status, epicId);
        } else {
            return new Epic(id, name, description, status);
        }
    }

    protected static String getHeader() {
        return "id,type,name,status,description,epic";
    }
}
