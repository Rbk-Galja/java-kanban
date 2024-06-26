package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;
    private static Task task;
    private static Epic epic;
    private static SubTask subTask;
    private static List<Task> savedTask = new ArrayList<>();
    private static List<Epic> savedEpic = new ArrayList<>();
    private static List<SubTask> savedSubTask = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        epic = new Epic("Имя эпика", "Описание эпика");
        subTask = create();
    }

    @BeforeEach
    public void beforeEach() {
        taskManager = Manager.getDefault();
    }

    @Test
    void shouldCreateTask() {

        taskManager.createTasks(task);
        assertNotNull(task, "Задача не найдена");

        Task getTask = taskManager.getTaskById(task.getId());
        assertEquals(task, getTask, "Возвращает неверную задачу");

        Task task3 = new Task("Имя второй задачи", "Описание второй задачи", TasksStatus.NEW);
        taskManager.createTasks(task3);

        savedTask = taskManager.getTasks();

        assertEquals(2, savedTask.size(), "Неверное количество задач");
        assertNotNull(savedTask, "Не получаем список задач");
        assertEquals(task, savedTask.getFirst(), "Возвращает неверную задачу");
    }

    @Test
    void shouldCreateEpic() {
        taskManager.creatEpic(epic);
        assertNotNull(epic, "Эпик не найден");

        Epic getEpic = taskManager.getEpicById(epic.getId());
        assertEquals(epic, getEpic, "Возвращает неверный эпик");

        Epic epic2 = new Epic("Второй эпик", "Описание второго эпика");
        taskManager.creatEpic(epic2);

        savedEpic = taskManager.getEpic();

        assertNotNull(savedEpic, "Не получаем список эпиков");
        assertEquals(2, savedEpic.size(), "Неверное количество эпиков");
        assertEquals(epic, savedEpic.getFirst(), "Возвращает неверный эпик");
    }

    @Test
    void shouldCreateSubTask() {
        taskManager.creatEpic(epic);
        taskManager.creatSubtask(subTask);
        assertNotNull(subTask, "Подзадача не найдена");

        SubTask getSubTask = taskManager.getSubTaskById(subTask.getId());
        assertEquals(subTask, getSubTask, "Возвращет неверную подзадачу");

        savedSubTask = taskManager.getSubTasks();

        assertNotNull(savedSubTask, "Не получаем список подзадач");
        assertEquals(1, savedSubTask.size(), "Неверное количество подзадач");
        assertEquals(subTask, savedSubTask.getFirst(), "Возвращает неверную подзадачу");
    }

    @Test
    void shouldDeleteTask() {
        taskManager.createTasks(task);
        assertNotNull(task, "Подзадача не создана");

        taskManager.deleteTask(task.getId());
        savedTask = taskManager.getTasks();
        assertEquals(0, savedTask.size(), "Задача не удалена");
    }

    @Test
    void shouldDeleteEpic() {
        taskManager.creatEpic(epic);
        assertNotNull(epic, "Эпик не создан");

        taskManager.deleteEpic(epic.getId());
        savedEpic = taskManager.getEpic();
        assertEquals(0, savedEpic.size(), "Эпик не удалился");
    }

    @Test
    void shouldDeleteSubTask() {
        taskManager.creatEpic(epic);
        taskManager.updateSubTasks(subTask);
        assertNotNull(subTask, "Подзадача не создана");
        taskManager.deleteSubTasks(subTask.getId());

        savedSubTask = taskManager.getSubTaskFromEpic(epic.getId());
        assertEquals(0, savedSubTask.size(), "Подзадача не удаляется из списка");
    }

    @Test
    void shouldUpdateTask() {
        Task newTask = taskManager.createTasks(task);
        assertNotNull(task, "Задача не создана");

        Task task2 = new Task(newTask.getId(), "Новое имя", "Новое описание", TasksStatus.IN_PROGRESS);
        Task task2Update = taskManager.updateTasks(task2);

        int id = newTask.getId();
        int newId = task2Update.getId();
        assertEquals(id, newId, "Id задач не совпадают");
        assertNotEquals(newTask, task2Update, "Поля задачи не обновились");

        savedTask = taskManager.getTasks();
        assertEquals(task2Update, savedTask.getFirst(), "Возвращает неверную задачу");
        assertEquals(1, savedTask.size(), "Не удаляет старую подзадачу при обновлении");
    }

    @Test
    void shouldUpdateEpic() {
        taskManager.creatEpic(epic);
        assertNotNull(epic, "Эпик не создан");

        Epic epic2 = new Epic(epic.getId(), "Новое имя", "Новое описание");
        taskManager.updateEpic(epic2);

        int id = epic.getId();
        int newId = epic2.getId();
        assertEquals(id, newId, "Id эпиков не совпадают");
        assertNotEquals(epic, epic2, "Поля эпика не обновились");

        savedEpic = taskManager.getEpic();
        assertEquals(epic2, savedEpic.getFirst(), "Возвращает неверный эпик");
        assertEquals(1, savedEpic.size(), "Не удаляет старый эпик при обновлении");
    }

    @Test
    void shouldUpdateSubTask() {
        taskManager.creatEpic(epic);
        assertNotNull(epic, "Эпик не создан");

        taskManager.creatSubtask(subTask);
        assertNotNull(subTask, "Подзадача не создана");

        SubTask newSubTask = new SubTask(subTask.getId(), "Новое имя", "Новое описание",
                TasksStatus.IN_PROGRESS, subTask.getEpicId());
        taskManager.updateSubTasks(newSubTask);

        int id = subTask.getId();
        int newId = newSubTask.getId();
        assertEquals(id, newId, "Id подзадач не совпадают");
        assertNotEquals(subTask, newSubTask, "Поля подзадачи не обновились");

        savedSubTask = taskManager.getSubTaskFromEpic(epic.getId());
        assertEquals(newSubTask, savedSubTask.getFirst(), "Подзадача не обновляется");
        assertEquals(1, savedSubTask.size(), "Старая подзадача не удаляется при обновлении");
    }

    @Test
    void shouldUpdateEpicStatus() {
        taskManager.creatEpic(epic);
        taskManager.creatSubtask(subTask);
        assertEquals(epic.getStatus(), subTask.getStatus(), "Статус эпика и подзадачи не совпадает");

        subTask.setStatus(TasksStatus.IN_PROGRESS);
        taskManager.updateSubTasks(subTask);
        assertEquals(epic.getStatus(), subTask.getStatus(), "Статус эпика и подзадачи не совпадает");
    }

    @Test
    void shouldTaskBeEqualsIfEqualsId() {
        taskManager.createTasks(task);
        int id = task.getId();

        Task task2 = new Task("Имя новой задачи", "Описание новой задачи", TasksStatus.IN_PROGRESS);
        taskManager.createTasks(task2);
        task2.setId(id);
        assertEquals(2, taskManager.getTasks().size(), "Неверное количество задач в списке");
        assertEquals(id, task2.getId(), "Id задач не равны");
    }

    @Test
    void shouldEpicBeEqualsIfEqualsId() {
        taskManager.creatEpic(epic);
        int id = epic.getId();

        Epic epic2 = new Epic("Имя нового эпика", "Описание нового эпика");
        taskManager.creatEpic(epic2);
        epic2.setId(id);
        assertEquals(2, taskManager.getEpic().size(), "Неверное количество эпиков в списке");
        assertEquals(id, epic2.getId(), "Id эпиков не равны");
    }

    @Test
    void shouldSubTaskBeEqualsIfEqualsId() {
        taskManager.creatEpic(epic);
        taskManager.creatSubtask(subTask);
        int id = subTask.getId();

        SubTask subTask2 = new SubTask("Имя второй подзадачи", "Описание второй подзадачи",
                TasksStatus.IN_PROGRESS, epic.getId());
        taskManager.creatSubtask(subTask2);
        subTask2.setId(id);
        assertEquals(2, taskManager.getSubTasks().size(), "Неверное количество подзадач в списке");
        assertEquals(id, subTask2.getId(), "Id подзадач не равны");
    }

    @Test
    void shouldNotAddEpicToHimself() {
        taskManager.creatEpic(epic);
        int id = epic.getId();
        assertNotNull(epic, "Эпик создан");
        subTask.setId(id);
        SubTask subTaskCreated = taskManager.creatSubtask(subTask);

        ArrayList<SubTask> getSubTask = taskManager.getSubTaskFromEpic(id);

        assertEquals(0, getSubTask.size(), "Эпик может добавить самого себя как подзадачу");
        assertNull(subTaskCreated, "Подзадача с id эпика создаётся");
    }

    private static SubTask create() {
        taskManager = Manager.getDefault();
        Epic epic1 = new Epic("И", "О");
        taskManager.creatEpic(epic1);
        return new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW, epic1.getId());
    }

}