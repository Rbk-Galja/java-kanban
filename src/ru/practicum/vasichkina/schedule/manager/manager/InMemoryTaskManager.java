package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.exceptions.InvalidTaskException;
import ru.practicum.vasichkina.schedule.manager.task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected static int nextId = 0;
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> scheduleTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    //получение всех задач, подзадач и эпиков
    @Override
    public List<Task> getTasks() {
        return tasks.values().stream().toList();
    }

    @Override
    public List<SubTask> getSubTasks() {
        return subTasks.values().stream().toList();
    }

    @Override
    public List<Epic> getEpic() {
        return epics.values().stream().toList();
    }

    //получение задач/подзадач/эпиков по id
    @Override
    public Optional<Task> getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        historyManager.addTask(task);
        return Optional.of(task);
    }

    @Override
    public Optional<Epic> getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        historyManager.addTask(epic);
        return Optional.of(epic);
    }

    @Override
    public Optional<SubTask> getSubTaskById(Integer subTasksId) {
        SubTask subTask = subTasks.get(subTasksId);
        historyManager.addTask(subTask);
        return Optional.of(subTask);
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
        intersectionTaskTime(task);
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
        Epic epic = epics.get(subTask.getEpicIdSB());
        if (epic == null || epic.getId().equals(subTask.getId())) {
            return null;
        }
        subTask.setId(getNextId());
        intersectionTaskTime(subTask);
        subTasks.put(subTask.getId(), subTask);
        epic.getSubTaskId().add(subTask.getId());
        updateEpicStatus(epic.getId());
        updateEpicDurationTime(epic.getId(), subTask);
        return subTask;
    }

    //обновление подзадач/задач/эпиков
    @Override
    public Task updateTasks(Task task) {
        scheduleTask.remove(tasks.get(task.getId()));
        intersectionTaskTime(task);
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
        Integer epicId = subTask.getEpicIdSB();
        scheduleTask.remove(subTasks.get(subTaskId));
        intersectionTaskTime(subTask);
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
        updateEpicDurationTime(epicId, subTask);
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
        scheduleTask.remove(tasks.get(taskId));
        return tasks.remove(taskId) != null;
    }

    @Override
    public boolean deleteSubTasks(Integer id) {
        SubTask subTask = subTasks.get(id);
        boolean deleteSubTask = subTasks.remove(id) != null;
        Epic epic = epics.get(subTask.getEpicIdSB());
        epic.getSubTaskId().remove(epic.getId());
        updateEpicStatus(epic.getId());
        scheduleTask.remove(subTask);
        updateEpicDurationTime(epic.getId(), subTask);
        historyManager.remove(id);
        return deleteSubTask;
    }

    @Override
    public boolean deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        boolean deleteEpic = epics.remove(id) != null;
        for (Integer subTaskId : epic.getSubTaskId()) {
            scheduleTask.remove(subTasks.get(subTaskId));
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
            scheduleTask.remove(tasks.get(i));
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer i : subTasks.keySet()) {
            historyManager.remove(i);
            scheduleTask.remove(subTasks.get(i));
        }
        for (Epic epic : epics.values()) {
            epic.setStatus(TasksStatus.NEW);
            epic.setDuration(Duration.ofMinutes(0));
            epic.setStartTime(LocalDateTime.now());
        }
        subTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer i : epics.keySet()) {
            historyManager.remove(i);
            scheduleTask.remove(epics.get(i));
        }
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
            scheduleTask.remove(subTasks.get(id));
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

    private void updateEpicDurationTime(Integer epicId, SubTask subTask1) {
        Epic epic = epics.get(epicId);
        List<Integer> idSubTask = epic.getSubTaskId();
        if (idSubTask.isEmpty()) {
            return;
        }
        if (idSubTask.size() == 1) {
            epic.setStartTime(subTask1.getStartTime());
            epic.setDuration(subTask1.getDurationTask());
            epic.setEndTimeEpic(subTask1.getStartTime().plus(subTask1.getDurationTask()));
            return;
        }
        TreeSet<SubTask> subTaskThisEpic = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        subTasks.values().stream()
                .filter(subTask -> idSubTask.contains(subTask.getId()))
                .map(subTask -> subTaskThisEpic.add(subTask))
                .collect(Collectors.toSet());
        epic.setStartTime(subTaskThisEpic.getFirst().getStartTime());
        epic.setDuration(Duration.ofMinutes(0));
        for (SubTask s : subTaskThisEpic) {
            epic.setDuration(epic.getDurationTask().plus(s.getDurationTask()));
        }
        epic.setEndTimeEpic(subTaskThisEpic.getLast().getEndTime());
    }

    private boolean isValid(Task task, Task task1) {
        return task.getStartTime().isBefore(task1.getEndTime()) && task.getEndTime().isAfter(task1.getStartTime());
    }

    private void intersectionTaskTime(Task task1) {
        boolean isValidTask = scheduleTask.stream()
                .filter(task -> task.getStartTime() != null)
                .anyMatch(task -> isValid(task1, task));
        if (isValidTask) {
            throw new InvalidTaskException("На этот период времени уже запланирована задача");
        }
        scheduleTask.add(task1);
    }

    @Override
    public List<Task> getPrioritizedList() {
        return scheduleTask.stream().toList();
    }

    private int getNextId() {
        return ++nextId;
    }

}
