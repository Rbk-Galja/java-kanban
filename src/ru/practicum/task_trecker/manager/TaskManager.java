package ru.practicum.task_trecker.manager;

import ru.practicum.task_trecker.task.Epic;
import ru.practicum.task_trecker.task.SubTask;
import ru.practicum.task_trecker.task.Task;
import ru.practicum.task_trecker.task.TasksStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int nextId;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    //получение всех задач, подзадач и эпиков
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpic() {
        return new ArrayList<>(epics.values());
    }

    //получение задач/подзадач/эпиков по id
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return null;
        }
        return task;
    }

    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        return epic;
    }

    public SubTask getSubTaskById(Integer subTasksId) {
        SubTask subTask = subTasks.get(subTasksId);
        if (subTask == null) {
            return null;
        }
        return subTask;
    }

    // получение списка подзадач заданного эпика
    public ArrayList<SubTask> getSubTaskFromEpic(Integer epicId) {
        if (epics.get(epicId) == null) {
            return null;
        } else {
            return epics.get(epicId).getAddToEpic();
        }
    }

    //печать списков задач/эпиков/подзадач
    public void getAllTasks() {
        for (Integer key : tasks.keySet()) {
            Task task = tasks.get(key);
            System.out.println(task);
        }
    }

    public void getAllEpics() {
        for (Integer key : epics.keySet()) {
            Epic epic = epics.get(key);
            System.out.println(epic);
        }
    }

    public void getAllSubTask() {
        for (Integer key : subTasks.keySet()) {
            SubTask subTask = subTasks.get(key);
            System.out.println(subTask);
        }
    }

    //создание задач/подзадач/эпиков
    public Task createTasks(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic creatEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public SubTask creatSubtask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return null;
        }
        subTask.setId(getNextId());
        subTasks.put(subTask.getId(), subTask);
        epic.getAddToEpic().add(subTask);
        epic.setStatus(checkEpicStatus(epic));
        return subTask;
    }

    //обновление подзадач/задач/эпиков
    public Task updateTasks(Task task) {
        Integer tasksId = task.getId();
        if (tasksId == null || !tasks.containsKey(tasksId)) {
            return null;
        }
        tasks.put(tasksId, task);
        return task;
    }

    public SubTask updateSubtasks(SubTask subTask, SubTask subTask1) {
        Integer subTaskId = subTask1.getId();
        if (subTaskId == null || !subTasks.containsKey(subTaskId)) {
            return null;
        }
        subTasks.put(subTaskId, subTask1);
        Epic epic = epics.get(subTask.getEpicId());
        epic.setStatus(checkEpicStatus(epic));
        ArrayList<SubTask> updateSubTasks = epic.getAddToEpic();
        updateSubTasks.remove(subTask);
        updateSubTasks.add(subTask1);
        epic.setAddToEpic(updateSubTasks);
        return subTask1;
    }

    public Epic updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epicId == null || !epics.containsKey(epicId)) {
            return null;
        }
        epic.setAddToEpic(epics.get(epic.getId()).getAddToEpic());
        epic.setStatus(checkEpicStatus((epic)));
        epics.put(epicId, epic);
        return epic;
    }

    // статус эпика
    public TasksStatus checkEpicStatus(Epic epic) {
        Integer epicId = epic.getId();
        if ((epicId == null) || !epics.containsKey(epicId)) {
            return null;
        }
        ArrayList<SubTask> epicSubTasks = epic.getAddToEpic();
        if (epicSubTasks.isEmpty()) {
            return TasksStatus.NEW;
        } else {
            int subTaskDone = 0;
            int subTaskNew = 0;

            for (SubTask subTask : epicSubTasks) {
                if (subTask.getStatus() == TasksStatus.DONE) {
                    subTaskDone += 1;
                }
                if (subTask.getStatus() == TasksStatus.NEW) {
                    subTaskNew += 1;
                }
            }

            if (epicSubTasks.size() == subTaskDone) {
                return TasksStatus.DONE;
            } else if (epicSubTasks.size() == subTaskNew) {
                return TasksStatus.NEW;
            } else {
                return TasksStatus.IN_PROGRESS;
            }
        }
    }

    // удаление задач/подзадач/эпиков по id
    public boolean deleteTask(Integer taskId) {
        return tasks.remove(taskId) != null;
    }

    public boolean deleteSubTasks(SubTask subTask) {
        Integer subTaskId = subTask.getId();
        Epic epic = epics.get(subTask.getEpicId());
        ArrayList<SubTask> deleteSubTask = epic.getAddToEpic();
        deleteSubTask.remove(subTask);
        epic.setAddToEpic(deleteSubTask);
        epic.setStatus(checkEpicStatus(epic));
        return subTasks.remove(subTaskId) != null;
    }

    public boolean deleteEpic(Epic epic) {
        Integer epicId = epic.getId();
        ArrayList<SubTask> deleteSubTask = getSubTaskFromEpic(epicId);
        for (SubTask subTask : deleteSubTask) {
            for (SubTask subTask1 : subTasks.values()) {
                if (subTask1.equals(subTask)) {
                    Integer subTaskId = subTask1.getId();
                    subTasks.remove(subTaskId);
                }
            }
        }
        return epics.remove(epicId) != null;
    }

    // удаление всех задач/подзадач/эпиков
    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    private int getNextId() {
        return nextId++;
    }


}
