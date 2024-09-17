package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected static int nextId = 0;
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();

    //получение всех задач, подзадач и эпиков
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getEpic() {
        return new ArrayList<>(epics.values());
    }

    //получение задач/подзадач/эпиков по id
    @Override
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(Integer subTasksId) {
        SubTask subTask = subTasks.get(subTasksId);
        historyManager.addTask(subTask);
        return subTask;
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
            List<Integer> idSubTask = epic.getSubTaskId();
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
    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic.getId());
        return epic;
    }

    @Override
    public SubTask createSubtask(SubTask subTask) {
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
    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        epic.setSubTaskId(savedEpic.getSubTaskId());
        epic.setStatus(savedEpic.getStatus());
        epics.put(epic.getId(), epic);
    }

    // удаление задач/подзадач/эпиков по id
    @Override
    public boolean deleteTask(Integer taskId) {
        historyManager.remove(taskId);
        return tasks.remove(taskId) != null;
    }

    @Override
    public boolean deleteSubTasks(Integer id) {
        SubTask subTask = subTasks.get(id);
        boolean deleteSubTask = subTasks.remove(id) != null;
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTaskId().remove(epic.getId());
        updateEpicStatus(epic.getId());
        historyManager.remove(id);
        return deleteSubTask;
    }

    @Override
    public boolean deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        boolean deleteEpic = epics.remove(id) != null;
        for (Integer subTaskId : epic.getSubTaskId()) {
            subTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        historyManager.remove(id);
        return deleteEpic;
    }

    // удаление всех задач/подзадач/эпиков
    @Override
    public void deleteAllTasks() {
        for (Integer i : tasks.keySet()) {
            historyManager.remove(i);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer i : subTasks.keySet()) {
            historyManager.remove(i);
        }
        for (Integer epicId : epics.keySet()) {
            updateEpicStatus(epicId);
        }
        subTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer i : epics.keySet()) {
            historyManager.remove(i);
        }
        epics.clear();
        subTasks.clear();
    }

    public List<Task> getHistory() {
        return historyManager.getTasksHistory();
    }

    // статус эпика
    private void updateEpicStatus(Integer epicId) {
        Epic epicStatus = epics.get(epicId);
        if (epicId == null || !epics.containsKey(epicId)) {
            return;
        }
        ArrayList<SubTask> subTaskThisEpic = new ArrayList<>();
        List<Integer> idSubTask = epicStatus.getSubTaskId();
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

    private int getNextId() {
        return ++nextId;
    }

}
