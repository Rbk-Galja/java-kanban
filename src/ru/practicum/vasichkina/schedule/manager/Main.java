package ru.practicum.vasichkina.schedule.manager;

import ru.practicum.vasichkina.schedule.manager.manager.Manager;
import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        testsTasks();
    }

    public static void testsTasks() {
        TaskManager inMemoryTaskManager = Manager.getDefault();

        System.out.println("Тест 1: пустые таски");
        List<Task> tasks = inMemoryTaskManager.getTasks();
        System.out.println("Таски должны быть пустые: " + tasks.isEmpty());
        System.out.println();

        System.out.println("Тест 2: добавляем первую таску");
        Task task1 = new Task("Уборка", "Сделать уборку на кухне", TasksStatus.NEW);
        Task task1Created = inMemoryTaskManager.createTasks(task1);
        System.out.println("Созданная таска должна содержать айди: " + (task1Created.getId() != null));
        System.out.println("Список тасок должен содержать нашу таску: " +
                (inMemoryTaskManager.getTaskById(task1Created.getId())));
        System.out.println();

        System.out.println("Тест 3: добавляем вторую таску");
        Task task2 = new Task("Покупки", "Сходить в новый магазин за углом", TasksStatus.NEW);
        Task task2Created = inMemoryTaskManager.createTasks(task2);
        System.out.println("Созданная таска должна содержать айди: " + (task2Created.getId() != null));
        System.out.println("Список тасок должен содержать нашу таску: " +
                (inMemoryTaskManager.getTaskById(task2Created.getId())));
        System.out.println();

        System.out.println("Тест 4: добавляем третью таску");
        Task task4 = new Task("Учеба", "Загрузить в гит ДЗ 4 спринта", TasksStatus.NEW);
        Task task4Created = inMemoryTaskManager.createTasks(task4);
        System.out.println("Созданная таска должна содержать айди: " + (task4Created.getId() != null));
        System.out.println("Список тасок должен содержать нашу таску: " +
                (inMemoryTaskManager.getTaskById(task4Created.getId())));
        System.out.println();

        System.out.println("Тест 4-1: добавляем таску c заданным айди");
        Task task7 = new Task("Тестовая таска", "Имеет айди 3", TasksStatus.NEW);
        Task task7Created = inMemoryTaskManager.createTasks(task7);
        task7Created.setId(task4Created.getId());
        System.out.println(task7Created);
        System.out.println("В списке есть наша таска");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println();

        System.out.println("Тест 5: выводим список всех тасок: ");
        System.out.println("В нашем списке такие таски: ");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println();

        System.out.println("Тест 6: обновляем таску");
        Task task3 = new Task(task1Created.getId(), "Новая уборка", "Сделать новую уборку на кухне",
                TasksStatus.IN_PROGRESS);
        Task task3Updated = inMemoryTaskManager.updateTasks(task3);
        System.out.println("Обновленная таска должна иметь обновленные поля: " + task3Updated);
        System.out.println();

        System.out.println("Тест 7: удаляем таску");
        System.out.println("Таска удалена: " + inMemoryTaskManager.deleteTask(task2Created.getId()));
        System.out.println();

        System.out.println("Тест 8: пустые эпики");
        List<Epic> epics = inMemoryTaskManager.getEpic();
        System.out.println("Эпики пустые: " + epics.isEmpty());
        System.out.println();

        System.out.println("Тест 9: создаём эпик");
        Epic epic1 = new Epic("Учеба", "Изучить ДЗ 4 спринта");
        Epic epic1Created = inMemoryTaskManager.createEpic(epic1);
        System.out.println("Созданный эпик должен содержать id: " + (epic1Created.getId() != null));
        System.out.println("Список эпиков должен содержать наш эпик:");
        System.out.println(inMemoryTaskManager.getEpic());
        System.out.println();

        System.out.println("Тест 10: список подзадач пуст");
        List<SubTask> subTasks = inMemoryTaskManager.getSubTasks();
        System.out.println("Подзадачи пустые: " + subTasks.isEmpty());
        System.out.println();

        System.out.println("Тест 11: создаём подзадачу для первого эпика");
        SubTask subTask1 = new SubTask("Ознакомление", "Изучить ТЗ",
                TasksStatus.NEW, epic1Created.getId());
        SubTask subTask1Created = inMemoryTaskManager.createSubtask(subTask1);
        System.out.println("Созданная подзадача должна содержать id: " + (subTask1Created.getId() != null));
        System.out.println("Список подзадач должен содержать нашу подзадачу:");
        System.out.println(inMemoryTaskManager.getSubTaskFromEpic(epic1Created.getId()));
        System.out.println();

        System.out.println("Тест 12: создаём вторую подзадачу для первого эпика");
        SubTask subTask2 = new SubTask("Ознакомление", "Посмотреть вебинар",
                TasksStatus.NEW, epic1Created.getId());
        SubTask subTask2Created = inMemoryTaskManager.createSubtask(subTask2);
        System.out.println("Созданная подзадача должна содержать id: " + (subTask2Created.getId() != null));
        System.out.println("Список подзадач должен содержать нашу подзадачу:");
        System.out.println(inMemoryTaskManager.getSubTaskFromEpic(epic1Created.getId()));
        System.out.println();

        System.out.println("Тест 13: создаём второй эпик");
        Epic epic2 = new Epic("Авто", "Провести ТО автомобиля");
        Epic epic2Created = inMemoryTaskManager.createEpic(epic2);
        System.out.println("Созданный эпик должен содержать id: " + (epic2Created.getId() != null));
        System.out.println("Список эпиков должен содержать наш эпик:");
        System.out.println(inMemoryTaskManager.getEpic());
        System.out.println();

        System.out.println("Тест 14: создаём первую подзадачу для второго эпика");
        SubTask subTask3 = new SubTask("Двигатель", "Выяснить почему жрёт масло",
                TasksStatus.NEW, epic2Created.getId());
        SubTask subTask3Created = inMemoryTaskManager.createSubtask(subTask3);
        System.out.println("Созданная подзадача должна содержать id: " + (subTask3Created.getId() != null));
        System.out.println("Список подзадач должен содержать нашу подзадачу:");
        System.out.println(inMemoryTaskManager.getSubTaskFromEpic(epic2Created.getId()));
        System.out.println();

        System.out.println("Тест 15: обновление подзадачи");
        SubTask subTask4 = new SubTask(subTask1Created.getId(), "Выполнение",
                "Набросать схему классов и методов", TasksStatus.IN_PROGRESS, subTask1Created.getEpicId());
        inMemoryTaskManager.updateSubTasks(subTask4);
        System.out.println("Обновленная подзадача должна иметь обновленные поля: " + subTask4);
        System.out.println("Статус эпика должен обновиться: " + inMemoryTaskManager.getEpicById(subTask4.getEpicId()));
        System.out.println();

        System.out.println("Тест 16: обновление эпика");
        Epic epic3 = new Epic(epic1Created.getId(), "Учеба", "Выполнить ДЗ 4 спринта");
        inMemoryTaskManager.updateEpic(epic3);
        System.out.println("Обновленный эпик должен иметь обновленные поля: " +
                inMemoryTaskManager.getEpicById(epic3.getId()));
        System.out.println();

        System.out.println("Тест 17: удаление эпика");
        System.out.println("Удаление должно пройти успешно: " + inMemoryTaskManager.deleteEpic(epic2Created.getId()));
        System.out.println("В списке эпиков теперь такие эпики:");
        System.out.println(inMemoryTaskManager.getEpic());
        System.out.println();

        System.out.println("Тест 18: удаление подзадачи");
        System.out.println("Удаление должно пройти успешно: " + inMemoryTaskManager.deleteSubTasks(subTask4.getId()));
        System.out.println("Обновленный эпик должен иметь обновленный статус: " + inMemoryTaskManager.getEpic());
        System.out.println("Список подзадач теперь содержит:");
        System.out.println(inMemoryTaskManager.getSubTaskFromEpic(epic3.getId()));
        System.out.println();

        System.out.println("Тест 19: проверяем, что эпик нельзя добавить в самого себя в виде подзадачи ");
        SubTask subTask8 = new SubTask("Подзадача в эпике", "Добавляем эпик в эпик", TasksStatus.NEW,
                epic3.getId());
        subTask8.setId(epic3.getId());
        inMemoryTaskManager.createSubtask(subTask8);
        System.out.println("В списке нет нашей подзадачи");
        System.out.println(inMemoryTaskManager.getSubTaskFromEpic(epic3.getId()));
        System.out.println();

        System.out.println("Тест 20: просмотр истории");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Тест 21: проверка дублей в истории");
        System.out.println(inMemoryTaskManager.getEpicById(epic3.getId()));
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Тест 22: проверка добавления в историю при создании");
        Task task8 = new Task("Имя", "Описание", TasksStatus.NEW);
        inMemoryTaskManager.createTasks(task8);
        System.out.println(inMemoryTaskManager.getTaskById(task8.getId()));
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Тест 23: проверка полной очистки истории");
        inMemoryTaskManager.deleteAllTasks();
        inMemoryTaskManager.deleteAllSubTasks();
        inMemoryTaskManager.deleteAllEpics();
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Создаем задачи для пользовательского сценария из ТЗ");
        Task task9 = new Task("Имя задачи 9", "Описание задачи 9", TasksStatus.NEW);
        inMemoryTaskManager.createTasks(task9);
        Task task10 = new Task("Имя задачи 10", "Описание задачи 10", TasksStatus.IN_PROGRESS);
        inMemoryTaskManager.createTasks(task10);

        Epic epic4 = new Epic("Имя эпика 4", "Описание эпика 4");
        inMemoryTaskManager.createEpic(epic4);
        SubTask subTask9 = new SubTask("Имя подзадачи 9", "Описание подзадачи 9", TasksStatus.NEW,
                epic4.getId());
        inMemoryTaskManager.createSubtask(subTask9);
        SubTask subTask10 = new SubTask("Имя подзадачи 10", "Описание подзадачи 10",
                TasksStatus.IN_PROGRESS, epic4.getId());
        inMemoryTaskManager.createSubtask(subTask10);
        SubTask subTask11 = new SubTask("Имя подзадачи 11", "Описание подзадачи 11", TasksStatus.DONE,
                epic4.getId());
        inMemoryTaskManager.createSubtask(subTask11);

        Epic epic5 = new Epic("Имя эпика 5", "Описание эпика 5");
        inMemoryTaskManager.createEpic(epic5);
        System.out.println();

        System.out.println("Вызываем задачи для пользовательского сценария из ТЗ");
        inMemoryTaskManager.getTaskById(task10.getId());
        System.out.println("Вызвали задачу 10");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        inMemoryTaskManager.getTaskById(task9.getId());
        System.out.println("Вызвали задачу 9");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        inMemoryTaskManager.getSubTaskById(subTask11.getId());
        System.out.println("Вызвали подзадачу 11");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        inMemoryTaskManager.getEpicById(epic5.getId());
        System.out.println("Вызвали эпик 5");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        inMemoryTaskManager.getSubTaskById(subTask9.getId());
        System.out.println("Вызвали подзадачу 9");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        inMemoryTaskManager.getSubTaskById(subTask10.getId());
        System.out.println("Вызвали подзадачу 10");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        inMemoryTaskManager.getEpicById(epic4.getId());
        System.out.println("Вызвали эпик 4");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Удаляем задачи для пользовательского сценария из ТЗ");
        inMemoryTaskManager.deleteTask(task10.getId());
        System.out.println("Удалили задачу 10");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        inMemoryTaskManager.deleteEpic(epic4.getId());
        System.out.println("Удалили эпик 4 с подзадачами 9, 10, 11");
        System.out.println("История:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();
    }
}

