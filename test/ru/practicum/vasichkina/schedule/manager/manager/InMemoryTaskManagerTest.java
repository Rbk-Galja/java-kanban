package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;


    @BeforeEach
    public void beforeEach() {
        taskManager = Manager.getDefault();
    }

    @Test
    void shouldCreateTask() {
        Task task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        Task newTask = taskManager.createTasks(task);
        assertNotNull(newTask, "Задача не найдена");

        Task getTask = taskManager.getTaskById(newTask.getId());
        assertEquals(newTask, getTask, "Возвращает неверную задачу");

        Task task3 = new Task("Имя второй задачи", "Описание второй задачи", TasksStatus.NEW);
        Task newTask2 = taskManager.createTasks(task3);

        final ArrayList<Task> savedTask = taskManager.getTasks();

        assertEquals(2, savedTask.size(), "Неверное количество задач");
        assertNotNull(savedTask, "Не получаем список задач");
        assertEquals(task, savedTask.getFirst(), "Возвращает неверную задачу");
    }

    @Test
    void shouldCreateEpic() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        Epic newEpic = taskManager.creatEpic(epic);
        assertNotNull(newEpic, "Эпик не найден");

        Epic getEpic = taskManager.getEpicById(newEpic.getId());
        assertEquals(newEpic, getEpic, "Возвращает неверный эпик");

        Epic epic2 = new Epic("Второй эпик", "Описание второго эпика");
        Epic newEpic2 = taskManager.creatEpic(epic2);

        final ArrayList<Epic> savedEpic = taskManager.getEpic();

        assertNotNull(savedEpic, "Не получаем список эпиков");
        assertEquals(2, savedEpic.size(), "Неверное количество эпиков");
        assertEquals(newEpic, savedEpic.getFirst(), "Возвращает неверный эпик");
    }

    @Test
    void shouldCreateSubTask() {
        Epic epic2 = new Epic("Имя эпика", "Описание эпика");
        Epic newEpic2 = taskManager.creatEpic(epic2);
        assertNotNull(newEpic2, "Эпик не найден");
        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                newEpic2.getId());
        SubTask newSubTask = taskManager.creatSubtask(subTask);
        assertNotNull(subTask, "Подзадача не найдена");

        SubTask getSubTask = taskManager.getSubTaskById(newSubTask.getId());
        assertEquals(newSubTask, getSubTask, "Возвращет неверную подзадачу");


        final ArrayList<SubTask> savedSubTask = taskManager.getSubTasks();

        assertNotNull(savedSubTask, "Не получаем список подзадач");
        assertEquals(1, savedSubTask.size(), "Неверное количество подзадач");
        assertEquals(newSubTask, savedSubTask.getFirst(), "Возвращает неверную подзадачу");
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        taskManager.createTasks(task);
        assertNotNull(task, "Подзадача не создана");

        taskManager.deleteTask(task.getId());
        ArrayList<Task> deleteTask = taskManager.getTasks();
        assertEquals(0, deleteTask.size(), "Задача не удалена");
    }

    @Test
    void shouldDeleteEpic() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        taskManager.creatEpic(epic);
        assertNotNull(epic, "Эпик не создан");

        taskManager.deleteEpic(epic.getId());
        ArrayList<Epic> deleteEpic = taskManager.getEpic();
        assertEquals(0, deleteEpic.size(), "Эпик не удалился");
    }

    @Test
    void shouldDeleteSubTask() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        taskManager.creatEpic(epic);
        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                epic.getId());
        assertNotNull(subTask, "Подзадача не создана");
        taskManager.deleteSubTasks(subTask.getId());

        ArrayList<SubTask> subTaskList = taskManager.getSubTaskFromEpic(epic.getId());
        assertEquals(0, subTaskList.size(), "Подзадача не удаляется из списка");
    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task("Имя задачи", "Описание подзадачи", TasksStatus.NEW);
        Task newTask = taskManager.createTasks(task);
        assertNotNull(task, "Задача не создана");

        Task task2 = new Task(newTask.getId(), "Новое имя", "Новое описание", TasksStatus.IN_PROGRESS);
        Task task2Update = taskManager.updateTasks(task2);

        int id = newTask.getId();
        int newId = task2Update.getId();
        assertEquals(id, newId, "Id задач не совпадают");
        assertNotEquals(newTask, task2Update, "Поля задачи не обновились");

        ArrayList<Task> tasksList = taskManager.getTasks();
        assertEquals(task2Update, tasksList.getFirst(), "Возвращает неверную задачу");
        assertEquals(1, tasksList.size(), "Не удаляет старую подзадачу при обновлении");
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        Epic newEpic = taskManager.creatEpic(epic);
        assertNotNull(newEpic, "Эпик не создан");

        Epic epic2 = new Epic(epic.getId(), "Новое имя", "Новое описание");
        taskManager.updateEpic(epic2);

        int id = epic.getId();
        int newId = epic2.getId();
        assertEquals(id, newId, "Id эпиков не совпадают");
        assertNotEquals(epic, epic2, "Поля эпика не обновились");

        ArrayList<Epic> epicList = taskManager.getEpic();
        assertEquals(epic, epicList.getFirst(), "Возвращает неверный эпик");
        assertEquals(1, epicList.size(), "Не удаляет старый эпик при обновлении");
    }

    @Test
    void shouldUpdateSubTask() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        taskManager.creatEpic(epic);
        assertNotNull(epic, "Эпик не создан");

        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                epic.getId());
        taskManager.creatSubtask(subTask);
        assertNotNull(subTask, "Подзадача не создана");

        SubTask newSubTask = new SubTask(subTask.getId(), "Новое имя", "Новое описание",
                TasksStatus.IN_PROGRESS, subTask.getEpicId());
        taskManager.updateSubTasks(newSubTask);

        int id = subTask.getId();
        int newId = newSubTask.getId();
        assertEquals(subTask.getId(), newSubTask.getId(), "Id подзадач не совпадают");
        assertNotEquals(subTask, newSubTask, "Поля подзадачи не обновились");

        ArrayList<SubTask> subTasksList = taskManager.getSubTaskFromEpic(epic.getId());
        assertEquals(newSubTask, subTasksList.getFirst(), "Подзадача не обновляется");
        assertEquals(1, subTasksList.size(), "Старая подзадача не удаляется при обновлении");
    }

    @Test
    void shouldUpdateEpicStatus() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        taskManager.creatEpic(epic);

        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                epic.getId());
        taskManager.creatSubtask(subTask);
        assertEquals(epic.getStatus(), subTask.getStatus(), "Статус эпика и подзадачи не совпадает");

        SubTask subTask1 = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.IN_PROGRESS,
                epic.getId());
        taskManager.creatSubtask(subTask1);
        assertEquals(epic.getStatus(), subTask1.getStatus(), "Статус эпика и подзадачи не совпадает");
    }

    @Test
    void shouldTaskBeEqualsIfEqualsId() {
        Task task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        Task savedTask = taskManager.createTasks(task);
        int id = savedTask.getId();

        Task task2 = new Task("Имя новой задачи", "Описание новой задачи", TasksStatus.IN_PROGRESS);
        taskManager.createTasks(task2);
        task2.setId(id);
        assertEquals(2, taskManager.getTasks().size(), "Неверное количество задач в списке");
        assertEquals(id, task2.getId(), "Id задач не равны");
    }

    @Test
    void shouldEpicBeEqualsIfEqualsId() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        Epic epicCreated = taskManager.creatEpic(epic);
        int id = epicCreated.getId();

        Epic epic2 = new Epic("Имя нового эпика", "Описание нового эпика");
        taskManager.creatEpic(epic2);
        epic2.setId(id);
        assertEquals(2, taskManager.getEpic().size(), "Неверное количество эпиков в списке");
        assertEquals(id, epic2.getId(), "Id эпиков не равны");
    }

    @Test
    void shouldSubTaskBeEqualsIfEqualsId() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        Epic epicCreated = taskManager.creatEpic(epic);

        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW,
                epicCreated.getId());
        SubTask subTaskCreated = taskManager.creatSubtask(subTask);
        int id = subTaskCreated.getId();

        SubTask subTask2 = new SubTask("Имя второй подзадачи", "Описание второй подзадачи",
                TasksStatus.IN_PROGRESS, epicCreated.getId());
        taskManager.creatSubtask(subTask2);
        subTask2.setId(id);
        assertEquals(2, taskManager.getSubTasks().size(), "Неверное количество подзадач в списке");
        assertEquals(id, subTask2.getId(), "Id подзадач не равны");
    }

    @Test
    void shouldNotAddEpicToHimself() {
        Epic epic = new Epic("Имя эпика", "Описание эпика");
        taskManager.creatEpic(epic);
        int id = epic.getId();
        assertNotNull(epic, "Эпик создан");
        SubTask subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW, id);
        subTask.setId(id);
        SubTask subTaskCreated = taskManager.creatSubtask(subTask);

        ArrayList<SubTask> getSubTask = taskManager.getSubTaskFromEpic(id);

        assertEquals(0, getSubTask.size(), "Эпик может добавить самого себя как подзадачу");
        assertNull(subTaskCreated, "Подзадача с id эпика создаётся");
    }


}