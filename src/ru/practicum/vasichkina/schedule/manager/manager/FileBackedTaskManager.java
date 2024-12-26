package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.exceptions.ManagerSaveException;
import ru.practicum.vasichkina.schedule.manager.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final String[] path = {"src", "ru", "practicum", "vasichkina", "schedule", "manager"};
    private static final String PATH_TO_DIR = String.join(File.separator, path);
    private static final File dir = new File(PATH_TO_DIR, "resources");
    private static File file = new File(PATH_TO_DIR + File.separator + "resources", "backup.csv");

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {

        Files.createDirectories(dir.toPath());
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

        System.out.println("Создаём задачи");

        Task task = new Task("d", "f", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 11, 7, 12, 10));
        fileManager.createTasks(task);

        Epic epic = new Epic("Имя эпика", "Описание эпика");
        fileManager.createEpic(epic);

        SubTask subTask = new SubTask("Имя подзадачи 1", "Описание подзадачи 1", TasksStatus.NEW, epic.getId());
        fileManager.createSubtask(subTask);

        SubTask subTask1 = new SubTask("Имя подзадачи 2", "Описание подзадачи 2", TasksStatus.IN_PROGRESS,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 8, 11, 20), epic.getId());
        fileManager.createSubtask(subTask1);

        SubTask subTask2 = new SubTask("Имя подзадачи 3", "Описание подзадачи 3", TasksStatus.IN_PROGRESS,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 9, 11, 11), epic.getId());
        fileManager.createSubtask(subTask2);

        Epic epic2 = new Epic("Имя эпика2", "Описание эпика2");
        fileManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Имя подзадачи 1 эпика 2", "Описание подзадачи 1 эпика 2", TasksStatus.IN_PROGRESS,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 11, 10), epic2.getId());
        fileManager.createSubtask(subTask4);

        SubTask subTask5 = new SubTask("Имя подзадачи 2 эпика 2", "Описание подзадачи 2 эпика 2", TasksStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 11, 11, 20), epic2.getId());
        fileManager.createSubtask(subTask5);

        List<String> tasks;
        tasks = Files.readAllLines(file.toPath());
        System.out.println(tasks);
        System.out.println();

        System.out.println("Вызываем файл");
        loadFromFile();
        FileBackedTaskManager newFileManager = loadFromFile();

        List<Task> task1 = newFileManager.getTasks();
        List<Epic> epic1 = newFileManager.getEpic();
        List<SubTask> subTaskList = newFileManager.getSubTasks();
        System.out.println(task1 + "\n" + epic1 + "\n" + subTaskList);

        System.out.println();

        file.delete();
        dir.delete();
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
                if (task instanceof Epic) {
                    fileBackedTaskManager.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    fileBackedTaskManager.addSubTask((SubTask) task);
                } else {
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

    protected void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
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
        Epic epic = epics.get(subTask.getEpicIdSB());
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
