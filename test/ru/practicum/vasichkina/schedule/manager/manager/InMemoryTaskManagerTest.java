package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.*;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты менеджера задач")
class InMemoryTaskManagerTest {

    private static TaskManager taskManager;
    private static Task task;
    private static Epic epic;
    private static SubTask subTask;
    private static List<Task> savedTask = new ArrayList<>();
    private static List<Epic> savedEpic = new ArrayList<>();
    private static List<SubTask> savedSubTask = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        taskManager = Manager.getDefault();
        epic = new Epic("Имя эпика", "Описание эпика");
        taskManager.createEpic(epic);

        subTask = new SubTask("Имя подзадачи", "Описание подзадачи", TasksStatus.NEW, epic.getId());
        taskManager.createSubtask(subTask);

        task = new Task("Имя задачи", "Описание задачи", TasksStatus.NEW);
        taskManager.createTasks(task);
    }

    @Test
    @DisplayName("Создание и получение задачи")
    void shouldCreateTask() {
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
    @DisplayName("Создание и получение эпика")
    void shouldCreateEpic() {
        assertNotNull(epic, "Эпик не найден");

        Epic getEpic = taskManager.getEpicById(epic.getId());
        assertEquals(epic, getEpic, "Возвращает неверный эпик");

        Epic epic2 = new Epic("Второй эпик", "Описание второго эпика");
        taskManager.createEpic(epic2);

        savedEpic = taskManager.getEpic();

        assertNotNull(savedEpic, "Не получаем список эпиков");
        assertEquals(2, savedEpic.size(), "Неверное количество эпиков");
        assertEquals(epic, savedEpic.getFirst(), "Возвращает неверный эпик");
    }

    @Test
    @DisplayName("Создание и получение подхадачи")
    void shouldCreateSubTask() {
        assertNotNull(subTask, "Подзадача не найдена");

        SubTask getSubTask = taskManager.getSubTaskById(subTask.getId());
        assertEquals(subTask, getSubTask, "Возвращет неверную подзадачу");

        savedSubTask = taskManager.getSubTasks();

        assertNotNull(savedSubTask, "Не получаем список подзадач");
        assertEquals(1, savedSubTask.size(), "Неверное количество подзадач");
        assertEquals(subTask, savedSubTask.getFirst(), "Возвращает неверную подзадачу");
    }

    @Test
    @DisplayName("Удаление задачи")
    void shouldDeleteTask() {
        assertNotNull(task, "Подзадача не создана");

        taskManager.deleteTask(task.getId());
        savedTask = taskManager.getTasks();
        assertEquals(0, savedTask.size(), "Задача не удалена");
    }

    @Test
    @DisplayName("Удалени эпика")
    void shouldDeleteEpic() {
        assertNotNull(epic, "Эпик не создан");

        taskManager.deleteEpic(epic.getId());
        savedEpic = taskManager.getEpic();
        assertEquals(0, savedEpic.size(), "Эпик не удалился");
    }

    @Test
    @DisplayName("Удаление подзадачи")
    void shouldDeleteSubTask() {
        assertNotNull(subTask, "Подзадача не создана");
        taskManager.deleteSubTasks(subTask.getId());

        savedSubTask = taskManager.getSubTaskFromEpic(epic.getId());
        assertEquals(0, savedSubTask.size(), "Подзадача не удаляется из списка");
        assertNull(taskManager.getSubTaskById(subTask.getId()), "Подзадача не удалена");
    }

    @Test
    @DisplayName("Обновление задачи")
    void shouldUpdateTask() {
        assertNotNull(task, "Задача не создана");

        Task task2 = new Task(task.getId(), "Новое имя", "Новое описание", TasksStatus.IN_PROGRESS);
        Task task2Update = taskManager.updateTasks(task2);

        int id = task.getId();
        int newId = task2Update.getId();
        assertEquals(id, newId, "Id задач не совпадают");
        assertNotEquals(task, task2Update, "Поля задачи не обновились");

        savedTask = taskManager.getTasks();
        assertEquals(task2Update, savedTask.getFirst(), "Возвращает неверную задачу");
        assertEquals(1, savedTask.size(), "Не удаляет старую подзадачу при обновлении");
    }

    @Test
    @DisplayName("Обновление эпика")
    void shouldUpdateEpic() {
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
    @DisplayName("Обновление подзадачи")
    void shouldUpdateSubTask() {
        assertNotNull(epic, "Эпик не создан");
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
    @DisplayName("Обновление статуса эпика")
    void shouldUpdateEpicStatus() {
        assertEquals(epic.getStatus(), subTask.getStatus(), "Статус эпика и подзадачи не совпадает");

        SubTask newSubTask = new SubTask(subTask.getId(), "Имя подзадачи", "Описание подзадачи",
                TasksStatus.IN_PROGRESS, subTask.getEpicId());
        taskManager.updateSubTasks(newSubTask);
        assertEquals(epic.getStatus(), newSubTask.getStatus(), "Статус эпика и подзадачи не совпадает");
    }

    @Test
    @DisplayName("Определение идентичности задач, если равны их айди")
    void shouldTaskBeEqualsIfEqualsId() {
        int id = task.getId();
        Task task2 = new Task("Имя новой задачи", "Описание новой задачи", TasksStatus.IN_PROGRESS);
        taskManager.createTasks(task2);
        task2.setId(id);
        assertEquals(2, taskManager.getTasks().size(), "Неверное количество задач в списке");
        assertEquals(id, task2.getId(), "Id задач не равны");
    }

    @Test
    @DisplayName("Определение идентичности эпиков, если равны их айди")
    void shouldEpicBeEqualsIfEqualsId() {
        int id = epic.getId();
        Epic epic2 = new Epic("Имя нового эпика", "Описание нового эпика");
        taskManager.createEpic(epic2);
        epic2.setId(id);
        assertEquals(2, taskManager.getEpic().size(), "Неверное количество эпиков в списке");
        assertEquals(id, epic2.getId(), "Id эпиков не равны");
    }

    @Test
    @DisplayName("Определение идентичности подзадач, если равны их айди")
    void shouldSubTaskBeEqualsIfEqualsId() {
        int id = subTask.getId();
        SubTask subTask2 = new SubTask("Имя второй подзадачи", "Описание второй подзадачи",
                TasksStatus.IN_PROGRESS, epic.getId());
        taskManager.createSubtask(subTask2);
        subTask2.setId(id);
        assertEquals(2, taskManager.getSubTasks().size(), "Неверное количество подзадач в списке");
        assertEquals(id, subTask2.getId(), "Id подзадач не равны");
    }

    @Test
    @DisplayName("Может ли добавить эпик в самого себя")
    void shouldNotAddEpicToHimself() {
        int id = epic.getId();
        assertNotNull(epic, "Эпик не создан");

        SubTask subTaskCreated = new SubTask("Новое имя", "Новое описание", TasksStatus.NEW, epic.getId());
        subTaskCreated.setId(id);
        taskManager.createSubtask(subTaskCreated);
        int idSubTask = subTaskCreated.getId();
        assertEquals(id, idSubTask, "id подзадачи не изменился");

        savedSubTask = taskManager.getSubTaskFromEpic(id);
        SubTask subTaskCreated2 = savedSubTask.getFirst();
        assertNotEquals(subTaskCreated2, subTaskCreated, "В список сохраняет подзадача с id эпика");
        assertEquals(1, savedSubTask.size(), "Эпик может добавить самого себя как подзадачу");
    }

    @Test
    @DisplayName("Меняет ли сеттер данные всех типов задач")
    void shouldSetterNotChangeDataInManager() {
        assertNotNull(taskManager.getEpicById(epic.getId()), "Эпик не создан");
        assertNotNull(taskManager.getSubTaskById(subTask.getId()), "Подзадача не создана");
        assertNotNull(taskManager.getTaskById(task.getId()), "Задача не создана");

        task.setId(12);
        assertEquals(task.getId(), 12, "ID задачи не изменился");
        task.setName("Новое имя");
        assertEquals(task.getName(), "Новое имя", "Имя задачи не изменилось");
        task.setDescription("Новое описание");
        assertEquals(task.getDescription(), "Новое описание", "Описание задаччи не изменилось");
        task.setStatus(TasksStatus.IN_PROGRESS);
        assertEquals(task.getStatus(), TasksStatus.IN_PROGRESS, "Статус задачи не изменился");

        subTask.setId(4);
        assertEquals(subTask.getId(), 4, "ID подзадачи не изменился");
        subTask.setName("Новое имя");
        assertEquals(subTask.getName(), "Новое имя", "Имя подзадачи не изменилось");
        subTask.setDescription("Новое описание");
        assertEquals(subTask.getDescription(), "Новое описание", "Описание подзадачи не изменилось");
        subTask.setStatus(TasksStatus.IN_PROGRESS);
        assertEquals(subTask.getStatus(), TasksStatus.IN_PROGRESS, "Статус подзадачи не изменился");

        epic.setId(7);
        assertEquals(epic.getId(), 7, "ID эпика не изменился");
        epic.setName("Новое имя");
        assertEquals(epic.getName(), "Новое имя", "Имя эпика не изменилось");
        epic.setDescription("Новое описание");
        assertEquals(epic.getDescription(), "Новое описание", "Описание эпика не изменилось");
        epic.setStatus(TasksStatus.DONE);
        assertEquals(epic.getStatus(), TasksStatus.DONE, "Статус эпика не изменился");

        savedSubTask = taskManager.getSubTasks();
        assertEquals(subTask, savedSubTask.getFirst(), "Возвращает неверные поля подзадачи");

        savedTask = taskManager.getTasks();
        assertEquals(task, savedTask.getFirst(), "Возвращает неверные поля задачи");
    }
}