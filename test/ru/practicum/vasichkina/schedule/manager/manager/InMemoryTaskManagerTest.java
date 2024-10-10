package ru.practicum.vasichkina.schedule.manager.manager;

import org.junit.jupiter.api.*;
import ru.practicum.vasichkina.schedule.manager.exceptions.InvalidTaskException;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты менеджера задач")
class InMemoryTaskManagerTest extends TaskManagerTest {

    private static List<Task> savedTask = new ArrayList<>();
    private static List<Epic> savedEpic = new ArrayList<>();
    private static List<SubTask> savedSubTask = new ArrayList<>();

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Override
    @Test
    @DisplayName("Создание и получение задачи")
    void shouldCreateTask() {
        assertNotNull(task, "Задача не найдена");

        Optional getTask = taskManager.getTaskById(task.getId());
        assertEquals(task, getTask.get(), "Возвращает неверную задачу");

        savedTask = taskManager.getTasks();

        assertEquals(1, savedTask.size(), "Неверное количество задач");
        assertNotNull(savedTask, "Не получаем список задач");
        assertEquals(task, savedTask.getFirst(), "Возвращает неверную задачу");
    }

    @Override
    @Test
    @DisplayName("Создание и получение эпика")
    void shouldCreateEpic() {
        assertNotNull(epic, "Эпик не найден");

        Optional getEpic = taskManager.getEpicById(epic.getId());
        assertEquals(epic, getEpic.get(), "Возвращает неверный эпик");

        savedEpic = taskManager.getEpic();

        assertNotNull(savedEpic, "Не получаем список эпиков");
        assertEquals(1, savedEpic.size(), "Неверное количество эпиков");
        assertEquals(epic, savedEpic.getFirst(), "Возвращает неверный эпик");
    }

    @Override
    @Test
    @DisplayName("Создание и получение подзадачи")
    void shouldCreateSubTask() {
        assertNotNull(subTask, "Подзадача не найдена");

        Optional getSubTask = taskManager.getSubTaskById(subTask.getId());
        assertEquals(subTask, getSubTask.get(), "Возвращет неверную подзадачу");

        savedSubTask = taskManager.getSubTasks();

        assertNotNull(savedSubTask, "Не получаем список подзадач");
        assertEquals(1, savedSubTask.size(), "Неверное количество подзадач");
        assertEquals(subTask, savedSubTask.getFirst(), "Возвращает неверную подзадачу");
    }

    @Override
    @Test
    @DisplayName("Удаление задачи")
    void shouldDeleteTask() {
        assertNotNull(task, "Подзадача не создана");

        taskManager.deleteTask(task.getId());
        savedTask = taskManager.getTasks();
        assertEquals(0, savedTask.size(), "Задача не удалена");
    }

    @Override
    @Test
    @DisplayName("Удаление эпика")
    void shouldDeleteEpic() {
        assertNotNull(epic, "Эпик не создан");

        taskManager.deleteEpic(epic.getId());
        savedEpic = taskManager.getEpic();
        assertEquals(0, savedEpic.size(), "Эпик не удалился");
    }

    @Override
    @Test
    @DisplayName("Удаление подзадачи")
    void shouldDeleteSubTask() {
        assertNotNull(subTask, "Подзадача не создана");
        taskManager.deleteSubTasks(subTask.getId());

        savedSubTask = taskManager.getSubTaskFromEpic(epic.getId());
        assertEquals(0, savedSubTask.size(), "Подзадача не удаляется из списка");
        assertEquals(taskManager.getSubTaskById(subTask.getId()), Optional.empty(), "Подзадача не удалена");
    }

    @Test
    @DisplayName("Обновление задачи")
    void shouldUpdateTask() {
        assertNotNull(task, "Задача не создана");

        Task task2 = new Task(task.getId(), "Новое имя", "Новое описание", TasksStatus.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 12, 11, 12, 0));
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

        SubTask newSubTask = new SubTask(subTask.getId(), "Новое имя", "Новое описание", TasksStatus.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.of(2024, 12, 11, 12, 10), subTask.getEpicId());
        taskManager.updateSubTasks(newSubTask);

        int id = subTask.getId();
        int newId = newSubTask.getId();
        assertEquals(id, newId, "Id подзадач не совпадают");
        assertNotEquals(subTask, newSubTask, "Поля подзадачи не обновились");

        savedSubTask = taskManager.getSubTaskFromEpic(epic.getId());
        assertEquals(newSubTask, savedSubTask.getFirst(), "Подзадача не обновляется в мапе");
        assertEquals(1, savedSubTask.size(), "Старая подзадача не удаляется при обновлении");
    }

    @Test
    @DisplayName("Обновление статуса эпика")
    void shouldUpdateEpicStatus() {
        assertEquals(epic.getStatus(), subTask.getStatus(), "Статус эпика и подзадачи не совпадает");

        SubTask newSubTask = new SubTask(subTask.getId(), "Имя подзадачи", "Описание подзадачи",
                TasksStatus.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 5, 5,5, 5), subTask.getEpicId());
        taskManager.updateSubTasks(newSubTask);
        assertEquals(epic.getStatus(), newSubTask.getStatus(), "Статус эпика и подзадачи не совпадает");
    }

    @Test
    @DisplayName("Статус эпика при статусе всех подзадач NEW")
    void shouldEpicStatusNew() {
        SubTask newSubTask = new SubTask(subTask.getId(), "Имя подзадачи", "Описание подзадачи",
                TasksStatus.NEW, Duration.ofMinutes(15), LocalDateTime.of(2024, 5, 5,5, 5),
                subTask.getEpicId());
        taskManager.createSubtask(newSubTask);

        assertEquals(epic.getStatus(), TasksStatus.NEW, "Возвращает неверный статус эпика");
    }

    @Test
    @DisplayName("Статус эпика при статусе всех подзадач DONE")
    void shouldEpicStatusDone() {
        SubTask subTask1 = new SubTask(subTask.getId(), "Имя подзадачи", "Описание подзадачи", TasksStatus.DONE,
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.updateSubTasks(subTask1);

        SubTask newSubTask = new SubTask(subTask.getId(), "Имя подзадачи", "Описание подзадачи",
                TasksStatus.DONE, Duration.ofMinutes(15), LocalDateTime.of(2024, 5, 5,5, 5),
                subTask.getEpicId());
        taskManager.createSubtask(newSubTask);

        assertEquals(epic.getStatus(), TasksStatus.DONE, "Возвращает неверный статус эпика");
    }

    @Test
    @DisplayName("Статус эпика при статусе подзадач DONE, NEW")
    void shouldEpicStatusDoneNew() {
        SubTask newSubTask = new SubTask(subTask.getId(), "Имя подзадачи", "Описание подзадачи",
                TasksStatus.DONE, Duration.ofMinutes(15), LocalDateTime.of(2024, 5, 5,5, 5),
                subTask.getEpicId());
        taskManager.createSubtask(newSubTask);

        assertEquals(epic.getStatus(), TasksStatus.IN_PROGRESS, "Возвращает неверный статус эпика");
    }

    @Test
    @DisplayName("Статус эпика при статусе подзадач IN_PROGRESS")
    void shouldEpicStatusInProgress() {
        SubTask subTask1 = new SubTask(subTask.getId(), "Имя подзадачи", "Описание подзадачи",
                TasksStatus.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 10, 12, 0), epic.getId());
        taskManager.updateSubTasks(subTask1);

        SubTask newSubTask = new SubTask(subTask.getId(), "Имя подзадачи", "Описание подзадачи",
                TasksStatus.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 5, 5,5, 5), subTask.getEpicId());
        taskManager.createSubtask(newSubTask);

        assertEquals(epic.getStatus(), TasksStatus.IN_PROGRESS, "Возвращает неверный статус эпика");
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

    @Test
    @DisplayName("Добавление времени выполнения в задачу")
    void shouldAddDurationAndTimeInTask() {
        Duration durationTask = task.getDurationTask();
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime engTime = task.getEndTime();

        assertNotNull(durationTask, "Не добавляет продолжительность в задачу");
        assertNotNull(startTime, "Не добавляет время начала");
        assertNotNull(engTime, "Не добавляет время окончания");
    }

    @Test
    @DisplayName("Добавление времени выполнения в подзадачу")
    void shouldAddDurationAndTimeInSubTask() {
        Duration durationSubTask = subTask.getDurationTask();
        LocalDateTime startTime = subTask.getStartTime();
        LocalDateTime engTime = subTask.getEndTime();

        assertNotNull(durationSubTask, "Не добавляет продолжительность в подзадачу");
        assertNotNull(startTime, "Не добавляет время начала");
        assertNotNull(engTime, "Не добавляет время окончания");
    }

    @Test
    @DisplayName("Добавление времени выполнения в эпик")
    void shouldAddDurationAndTimeInEpic() {
        Duration durationEpic = epic.getDurationTask();
        LocalDateTime startTime = epic.getStartTime();
        LocalDateTime engTime = epic.getEndTimeEpic();

        assertNotNull(durationEpic, "Не добавляет продолжительность в подзадачу");
        assertNotNull(startTime, "Не добавляет время начала");
        assertNotNull(engTime, "Не добавляет время окончания");
    }

    @Test
    @DisplayName("Обновление времени выполнения эпика при добавлении подзадачи")
    void shouldUpdateEpicTimeAddSubTask() {
        SubTask subTask1 = new SubTask("Имя подзадачи 2", "Описание подзадачи 2",
                TasksStatus.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 12, 12, 12, 0), epic.getId());
        taskManager.createSubtask(subTask1);

        Duration actuallyDuration = epic.getDurationTask();
        LocalDateTime actuallyStartTime = epic.getStartTime();
        LocalDateTime actuallyEndTime = epic.getEndTimeEpic();

        Duration expectedDuration = Duration.ofMinutes(25);
        LocalDateTime expectedStartTime = subTask.getStartTime();
        LocalDateTime expectedEndTime = subTask1.getEndTime();

        assertEquals(actuallyDuration, expectedDuration, "Сохраняет неверную продолжительность");
        assertEquals(actuallyStartTime, expectedStartTime, "Сохраняет неверное время начала");
        assertEquals(actuallyEndTime, expectedEndTime, "Сохраняет неверное время окончания");
    }

    @Test
    @DisplayName("Обновление времени выполнения эпика при обновлении подзадачи")
    void shouldUpdateEpicTimeUpdateSubTask() {
        SubTask subTask1 = new SubTask(subTask.getId(), "Новое имя", "Новое описание",
                TasksStatus.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 5, 5, 12, 0), subTask.getEpicId());
        taskManager.updateSubTasks(subTask1);

        Duration actuallyDuration = epic.getDurationTask();
        LocalDateTime actuallyStartTime = epic.getStartTime();
        LocalDateTime actuallyEndTime = epic.getEndTimeEpic();

        Duration expectedDuration = subTask1.getDurationTask();
        LocalDateTime expectedStartTime = subTask1.getStartTime();
        LocalDateTime expectedEndTime = subTask1.getEndTime();

        assertEquals(actuallyDuration, expectedDuration, "Сохраняет неверную продолжительность");
        assertEquals(actuallyStartTime, expectedStartTime, "Сохраняет неверное время начала");
        assertEquals(actuallyEndTime, expectedEndTime, "Сохраняет неверное время окончания");
    }

    @Test
    @DisplayName("Обновление времени выполнения эпика при удалении задачи")
    void shouldUpdateEpicTimeDeleteSubTask() {
        SubTask subTask1 = new SubTask("Имя подзадачи 2", "Описание подзадачи 2",
                TasksStatus.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2024, 12, 12, 12, 0), epic.getId());
        taskManager.createSubtask(subTask1);
        taskManager.deleteSubTasks(subTask.getId());

        Duration actuallyDuration = epic.getDurationTask();
        LocalDateTime actuallyStartTime = epic.getStartTime();
        LocalDateTime actuallyEndTime = epic.getEndTimeEpic();

        Duration expectedDuration = subTask1.getDurationTask();
        LocalDateTime expectedStartTime = subTask1.getStartTime();
        LocalDateTime expectedEndTime = subTask1.getEndTime();

        assertEquals(actuallyDuration, expectedDuration, "Сохраняет неверную продолжительность");
        assertEquals(actuallyStartTime, expectedStartTime, "Сохраняет неверное время начала");
        assertEquals(actuallyEndTime, expectedEndTime, "Сохраняет неверное время окончания");
    }

    @Test
    @DisplayName("Проверка исключения пересечений по времени выполнения задач при полном совпадении времени")
    void shouldThrowingExceptionIntersectionTask() {
        Task task1 = new Task("Имя второй задачи", "Описание второй задачи", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 12, 0));
                assertThrows(InvalidTaskException.class, () -> taskManager.createTasks(task1),
                        "Не выкидывает исключение при пересечении времени");
    }

    @Test
    @DisplayName("Проверка исключения пересечений по времени выполнения задач при совпадении времени времени начала и конца")
    void shouldThrowingExceptionIntersectionTaskStartTime() {
        Task task1 = new Task("Имя второй задачи", "Описание второй задачи", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 11, 50));
        assertThrows(InvalidTaskException.class, () -> taskManager.createTasks(task1),
                "Не выкидывает исключение при пересечении времени");
    }

    @Test
    @DisplayName("Проверка исключения пересечений по времени выполнения задач при совпадении времени времени конца и начала")
    void shouldThrowingExceptionIntersectionTaskEndTime() {
        Task task1 = new Task("Имя второй задачи", "Описание второй задачи", TasksStatus.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(2024, 10, 11, 12, 14));
        assertThrows(InvalidTaskException.class, () -> taskManager.createTasks(task1),
                "Не выкидывает исключение при пересечении времени");
    }
}