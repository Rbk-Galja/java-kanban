package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    //получение всех задач, подзадач и эпиков

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<Epic> getEpic();

    //получение задач/подзадач/эпиков по id
    Task getTaskById(Integer taskId);

    Epic getEpicById(Integer epicId);

    SubTask getSubTaskById(Integer subTasksId);

    // получение списка подзадач заданного эпика
    ArrayList<SubTask> getSubTaskFromEpic(Integer epicId);

    //создание задач/подзадач/эпиков
    Task createTasks(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubtask(SubTask subTask);

    //обновление подзадач/задач/эпиков
    Task updateTasks(Task task);

    void updateSubTasks(SubTask subTask);

    void updateEpic(Epic epic);

    // удаление задач/подзадач/эпиков по id
    boolean deleteTask(Integer taskId);

    boolean deleteSubTasks(Integer id);

    boolean deleteEpic(Integer id);

    // удаление всех задач/подзадач/эпиков
    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpics();

    List<Task> getHistory();

}
