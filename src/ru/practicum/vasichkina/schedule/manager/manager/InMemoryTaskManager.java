package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int nextId;
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    //получение всех задач, подзадач и эпиков
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getEpic() {
        return new ArrayList<>(epics.values());
    }

    //получение задач/подзадач/эпиков по id
    @Override
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        historyManager.addTask(task);
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        historyManager.addTask(epic);
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(Integer subTasksId) {
        SubTask subTask = subTasks.get(subTasksId);
        historyManager.addTask(subTask);
        return subTasks.get(subTasksId);
    }

    // получение списка подзадач заданного эпика
    @Override
    public ArrayList<SubTask> getSubTaskFromEpic(Integer epicId) {
        ArrayList<SubTask> subTaskThisEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);
        historyManager.addTask(epic);
        if (epic == null) {
            return subTaskThisEpic;
        } else {
            ArrayList<Integer> idSubTask = epic.getSubTaskId();
            for (Integer id : idSubTask) {
                if (subTasks.containsKey(id)) {
                    subTaskThisEpic.add(subTasks.get(id));
                    historyManager.addTask(subTasks.get(id));
                }
            }
            return subTaskThisEpic;
        }
    }

    //создание задач/подзадач/эпиков
    @Override
    public Task createTasks(Task task) {
            task.setId(getNextId());
            tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic creatEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
        return epic;
    }

    @Override
    public SubTask creatSubtask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null || epic.getId().equals(subTask.getId())) {
            return null;
        }
        subTask.setId(getNextId());
        subTasks.put(subTask.getId(), subTask);
        epic.getSubTaskId().add(subTask.getId());
        updateEpicStatus(epic.getId());
        return subTask;
    }

    //обновление подзадач/задач/эпиков
    @Override
    public Task updateTasks(Task task) {
        Integer tasksId = task.getId();
        if (tasksId == null || !tasks.containsKey(tasksId)) {
            return null;
        }
        tasks.put(tasksId, task);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public void updateSubTasks(SubTask subTask) {
        Integer subTaskId = subTask.getId();
        Integer epicId = subTask.getEpicId();
        SubTask savedSubTask = subTasks.get(subTaskId);
        if (savedSubTask == null || epicId.equals(subTask.getId())) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subTasks.put(subTaskId, subTask);
        updateEpicStatus(epicId);
        historyManager.addTask(subTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null || !epics.containsKey(epic.getId())) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        historyManager.addTask(epic);
    }

    // статус эпика
    private void updateEpicStatus(Integer epicId) {
        Epic epicStatus = epics.get(epicId);
        if (epicId == null || !epics.containsKey(epicId)) {
            return;
        }
        ArrayList<SubTask> subTaskThisEpic = new ArrayList<>();
        ArrayList<Integer> idSubTask = epicStatus.getSubTaskId();
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
    @Override
    public boolean deleteTask(Integer taskId) {
        return tasks.remove(taskId) != null;
    }

    @Override
    public Integer deleteSubTasks(Integer id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask == null) {
            return null;
        }
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTaskId().remove(epic.getId());
        updateEpicStatus(epic.getId());
        return id;
    }

    @Override
    public Integer deleteEpic(Integer id) {
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return null;
        }
        for (Integer subTaskId : epic.getSubTaskId()) {
            subTasks.remove(subTaskId);
        }
        return id;
    }

    // удаление всех задач/подзадач/эпиков
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Integer epicId : epics.keySet()) {
            updateEpicStatus(epicId);
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public List<Task> getHistory() {
        return historyManager.getTasksHistory();
    }

    private int getNextId() {
        return ++nextId;
    }
}
