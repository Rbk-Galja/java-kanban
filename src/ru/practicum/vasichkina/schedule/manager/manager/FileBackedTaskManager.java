package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final static String PATH_TO_FILE = "E:\\IDEA\\java-kanban\\src";
    private static File file = new File(PATH_TO_FILE, "backup.csv");

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

        System.out.println("Создаём задачи");

        Task task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        fileManager.createTasks(task);

        Epic epic = new Epic("Имя эпика", "Описание эпика");
        fileManager.createEpic(epic);

        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                epic.getId());
        fileManager.createSubtask(subTask);

        SubTask subTask1 = new SubTask("Имя подзадачи2", "Описание подзадачи 2", TasksStatus.NEW,
                epic.getId());
        fileManager.createSubtask(subTask1);

        List<String> tasks;
        try {
            tasks = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(tasks);
        System.out.println();

        System.out.println("Обновляем подзадачу");

       SubTask subTask2 = new SubTask(subTask.getId(),"Имя новой подзадачи", "Описание новой подзадачи",
                TasksStatus.IN_PROGRESS, subTask.getEpicId());
        fileManager.updateSubTasks(subTask2);

        try {
            tasks = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(tasks);
        System.out.println();

        System.out.println("Вызываем файл");
        loadFromFile();
        FileBackedTaskManager newFileManager = loadFromFile();

        List<Task> task1 = newFileManager.getTasks();
        List<Epic> epic1 = newFileManager.getEpic();
        List<SubTask> subTaskList = newFileManager.getSubTasks();
        System.out.println(task1 + "\n" + epic1 + "\n" + subTaskList);

    }

    public static FileBackedTaskManager loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            int maxID;
            List<Integer> idTask = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                Task task = CSVFormatter.fromString(lines.get(i));
                idTask.add(task.getId());
                if (task.getTaskType().equals(TaskType.EPIC)) {
                    fileBackedTaskManager.addEpic((Epic) task);
                } else if (task.getTaskType().equals(TaskType.SUBTASK)) {
                    fileBackedTaskManager.addSubTask((SubTask) task);
                } else if (task.getTaskType().equals(TaskType.TASK)) {
                    fileBackedTaskManager.addTask(task);
                }
            }
            if (!idTask.isEmpty()) {
                maxID = Collections.max(idTask);
                nextId = maxID;
            }
        } catch (IOException e) {
            throw ManagerSaveException.loadException(e);
        }
        return fileBackedTaskManager;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            boolean deleteFile = file.delete();
            if (deleteFile) {
                Files.createFile(Paths.get(PATH_TO_FILE, "backup.csv"));
            }
            bw.write(CSVFormatter.getHeader());
            bw.newLine();
            for (Task task : getTasks()) {
                bw.write(CSVFormatter.toString(task) + "\n");
            }
            for (Epic epic : getEpic()) {
                bw.write(CSVFormatter.toString(epic) + "\n");
            }
            for (SubTask subTask : getSubTasks()) {
                bw.write(CSVFormatter.toString(subTask) + "\n");
            }
        } catch (IOException e) {
            throw ManagerSaveException.saveException(e);
        }
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTaskId().add(subTask.getId());
    }

    @Override
    public Task createTasks(Task task) {
        Task createdTask = super.createTasks(task);
        save();
        return createdTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createEpic = super.createEpic(epic);
        save();
        return createEpic;
    }

    @Override
    public SubTask createSubtask(SubTask subTask) {
        SubTask createSubTask = super.createSubtask(subTask);
        save();
        return createSubTask;
    }

    @Override
    public Task updateTasks(Task task) {
        Task updateTask = super.updateTasks(task);
        save();
        return updateTask;
    }

    @Override
    public void updateSubTasks(SubTask subTask) {
        super.updateSubTasks(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public boolean deleteTask(Integer taskId) {
        boolean deleteTask = deleteTask(taskId);
        save();
        return deleteTask;
    }

    @Override
    public boolean deleteSubTasks(Integer id) {
        boolean deleteSubTask = deleteSubTasks(id);
        save();
        return deleteSubTask;
    }

    @Override
    public boolean deleteEpic(Integer id) {
        boolean deleteEpic = deleteEpic(id);
        save();
        return deleteEpic;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

}
