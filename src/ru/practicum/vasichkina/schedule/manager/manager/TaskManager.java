package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;
import java.util.HashMap;

// спасибо за похвалу) очень воодушевляет

public class TaskManager {

    private int nextId;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

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
        return tasks.get(taskId);
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
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        } else {
            ArrayList<SubTask> subTaskThisEpic = new ArrayList<>();
            ArrayList<Integer> idSubTask = epic.getAddToEpic();
            for (Integer id : idSubTask) {
                if (subTasks.containsKey(id)) {
                    subTaskThisEpic.add(subTasks.get(id));
                }
            }
            return subTaskThisEpic;
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
        updateEpicStatus(epic.getId());
        return epic;
    }

    public SubTask creatSubtask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return null;
        }
        subTask.setId(getNextId());
        subTasks.put(subTask.getId(), subTask);
        epic.getAddToEpic().add(subTask.getId());
        updateEpicStatus(epic.getId());
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

    public void updateSubTasks(SubTask subTask) {
        Integer subTaskId = subTask.getId();
        Integer epicId = subTask.getEpicId();
        SubTask savedSubTask = subTasks.get(subTaskId);
        if (savedSubTask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subTasks.put(subTaskId, subTask);
        updateEpicStatus(epicId);
    }

    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null || !epics.containsKey(epic.getId())) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    // статус эпика
    private void updateEpicStatus(Integer epicId) {
        Epic epicStatus = epics.get(epicId);
        if (epicId == null || !epics.containsKey(epicId)) {
            return;
        }
        ArrayList<SubTask> subTaskThisEpic = new ArrayList<>();
        ArrayList<Integer> idSubTask = epicStatus.getAddToEpic();
        for (Integer id : idSubTask) {
            if (subTasks.containsKey(id)) {
                subTaskThisEpic.add(subTasks.get(id));
            }
        }
        if (subTaskThisEpic.isEmpty()) {
            epicStatus.setStatus(TasksStatus.NEW);
        } else {
            int subTaskDone = 0;
            int subTaskNew = 0;
            for (SubTask subTask : subTaskThisEpic) {
                if (subTask.getStatus() == TasksStatus.DONE) {
                    subTaskDone += 1;
                }
                if (subTask.getStatus() == TasksStatus.NEW) {
                    subTaskNew += 1;
                }
            }

            if (subTaskThisEpic.size() == subTaskDone) {
                epicStatus.setStatus(TasksStatus.DONE);
            } else if (subTaskThisEpic.size() == subTaskNew) {
                epicStatus.setStatus(TasksStatus.NEW);
            } else {
                epicStatus.setStatus(TasksStatus.IN_PROGRESS);
            }
        }
    }

    // удаление задач/подзадач/эпиков по id
    public boolean deleteTask(Integer taskId) {
        return tasks.remove(taskId) != null;
    }

    public Integer deleteSubTasks(Integer id) {
        SubTask subTask = subTasks.remove(id);
        ;
        if (subTask == null) {
            return null;
        }
        Epic epic = epics.get(subTask.getEpicId());
        epic.getAddToEpic().remove(epic.getId());
        updateEpicStatus(epic.getId());
        return id;
    }

    public Integer deleteEpic(Integer id) {
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return null;
        }
        for (Integer subTaskId : epic.getAddToEpic()) {
            subTasks.remove(subTaskId);
        }
        return id;
    }

    // удаление всех задач/подзадач/эпиков
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Integer epicId : epics.keySet()) {
            updateEpicStatus(epicId);
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    private int getNextId() {
        return ++nextId;
    }
}
