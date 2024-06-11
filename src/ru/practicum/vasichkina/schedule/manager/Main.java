package ru.practicum.vasichkina.schedule.manager;

import ru.practicum.vasichkina.schedule.manager.manager.TaskManager;
import ru.practicum.vasichkina.schedule.manager.task.Epic;
import ru.practicum.vasichkina.schedule.manager.task.SubTask;
import ru.practicum.vasichkina.schedule.manager.task.Task;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        testsTasks();
    }

    public static void testsTasks() {
        TaskManager taskManager = new TaskManager();

        System.out.println("Тест 1: пустые таски");
        ArrayList<Task> tasks = taskManager.getTasks();
        System.out.println("Таски должны быть пустые: " + tasks.isEmpty());
        System.out.println();

        System.out.println("Тест 2: добавляем первую таску");
        Task task1 = new Task("Уборка", "Сделать уборку на кухне", TasksStatus.NEW);
        Task task1Created = taskManager.createTasks(task1);
        System.out.println("Созданная таска должна содержать айди: " + (task1Created.getId() != null));
        System.out.println("Список тасок должен содержать нашу таску: " +
                (taskManager.getTaskById(task1Created.getId())));
        System.out.println();

        System.out.println("Тест 3: добавляем вторую таску");
        Task task2 = new Task("Покупки", "Сходить в новый магазин за углом", TasksStatus.NEW);
        Task task2Created = taskManager.createTasks(task2);
        System.out.println("Созданная таска должна содержать айди: " + (task2Created.getId() != null));
        System.out.println("Список тасок должен содержать нашу таску: " +
                (taskManager.getTaskById(task2Created.getId())));
        System.out.println();

        System.out.println("Тест 4: добавляем третью таску");
        Task task4 = new Task("Учеба", "Загрузить в гит ДЗ 4 спринта", TasksStatus.NEW);
        Task task4Created = taskManager.createTasks(task4);
        System.out.println("Созданная таска должна содержать айди: " + (task4Created.getId() != null));
        System.out.println("Список тасок должен содержать нашу таску: " +
                (taskManager.getTaskById(task4Created.getId())));
        System.out.println();

        System.out.println("Тест 5: выводим список всех тасок: ");
        System.out.println("В нашем списке такие таски: ");
        System.out.println(taskManager.getTasks());
        System.out.println();

        System.out.println("Тест 6: обновляем таску");
        Task task3 = new Task(task1Created.getId(), "Новая уборка", "Сделать новую уборку на кухне",
                TasksStatus.IN_PROGRESS);
        Task task3Updated = taskManager.updateTasks(task3);
        System.out.println("Обновленная таска должна иметь обновленные поля: " + task3Updated);
        System.out.println();

        System.out.println("Тест 7: удаляем таску");
        boolean deleteTask = taskManager.deleteTask(task2Created.getId());
        System.out.println("Таска удалена: " + deleteTask);
        System.out.println();

        System.out.println("Тест 8: удаляем все таски");
        taskManager.deleteAllTasks();
        System.out.println("Таски должны быть пустые: " + tasks.isEmpty());
        System.out.println();

        System.out.println("Тест 9: пустые эпики");
        ArrayList<Epic> epics = taskManager.getEpic();
        System.out.println("Эпики пустые: " + epics.isEmpty());
        System.out.println();

        System.out.println("Тест 10: создаём эпик");
        Epic epic1 = new Epic("Учеба", "Изучить ДЗ 4 спринта");
        Epic epic1Created = taskManager.creatEpic(epic1);
        System.out.println("Созданный эпик должен содержать id: " + (epic1Created.getId() != null));
        System.out.println("Список эпиков должен содержать наш эпик:");
        System.out.println(taskManager.getEpic());
        System.out.println();

        System.out.println("Тест 11: список подзадач пуст");
        ArrayList<SubTask> subTasks = taskManager.getSubTasks();
        System.out.println("Подзадачи пустые: " + subTasks.isEmpty());
        System.out.println();

        System.out.println("Тест 12: создаём подзадачу для первого эпика");
        SubTask subTask1 = new SubTask("Ознакомление", "Изучить ТЗ",
                TasksStatus.NEW, epic1Created.getId());
        SubTask subTask1Created = taskManager.creatSubtask(subTask1);
        System.out.println("Созданная подзадача должна содержать id: " + (subTask1Created.getId() != null));
        System.out.println("Список подзадач должен содержать нашу подзадачу:");
        System.out.println(taskManager.getSubTaskFromEpic(epic1Created.getId()));
        System.out.println();

        System.out.println("Тест 13: создаём вторую подзадачу для первого эпика");
        SubTask subTask2 = new SubTask("Ознакомление", "Посмотреть вебинар",
                TasksStatus.NEW, epic1Created.getId());
        SubTask subTask2Created = taskManager.creatSubtask(subTask2);
        System.out.println("Созданная подзадача должна содержать id: " + (subTask2Created.getId() != null));
        System.out.println("Список подзадач должен содержать нашу подзадачу:");
        System.out.println(taskManager.getSubTaskFromEpic(epic1Created.getId()));
        System.out.println();

        System.out.println("Тест 14: создаём второй эпик");
        Epic epic2 = new Epic("Авто", "Провести ТО автомобиля");
        Epic epic2Created = taskManager.creatEpic(epic2);
        System.out.println("Созданный эпик должен содержать id: " + (epic2Created.getId() != null));
        System.out.println("Список эпиков должен содержать наш эпик:");
        System.out.println(taskManager.getEpic());
        System.out.println();

        System.out.println("Тест 15: создаём первую подзадачу для второго эпика");
        SubTask subTask3 = new SubTask("Двигатель", "Выяснить почему жрёт масло",
                TasksStatus.NEW, epic2Created.getId());
        SubTask subTask3Created = taskManager.creatSubtask(subTask3);
        System.out.println("Созданная подзадача должна содержать id: " + (subTask3Created.getId() != null));
        System.out.println("Список подзадач должен содержать нашу подзадачу:");
        System.out.println(taskManager.getSubTaskFromEpic(epic2Created.getId()));
        System.out.println();

        System.out.println("Тест 16: обновление подзадачи");
        SubTask subTask4 = new SubTask(subTask1Created.getId(), "Выполнение",
                "Набросать схему классов и методов", TasksStatus.IN_PROGRESS, subTask1Created.getEpicId());
        taskManager.updateSubTasks(subTask4);
        System.out.println("Обновленная подзадача должна иметь обновленные поля: " + subTask4);
        System.out.println("Статус эпика должен обновиться: " + taskManager.getEpicById(subTask4.getEpicId()));
        System.out.println();

        System.out.println("Тест 17: обновление эпика");
        Epic epic3 = new Epic(epic1Created.getId(), "Учеба", "Выполнить ДЗ 4 спринта");
        taskManager.updateEpic(epic3);
        System.out.println("Обновленный эпик должен иметь обновленные поля: " + taskManager.getEpicById(epic3.getId()));
        System.out.println();

        System.out.println("Тест 18: удаление эпика");
        boolean deleteEpicResult = taskManager.deleteEpic(epic2Created.getId()) != null;
        System.out.println("Удаление должно пройти успешно: " + deleteEpicResult);
        System.out.println("В списке эпиков теперь такие эпики:");
        System.out.println(taskManager.getEpic());
        System.out.println();

        System.out.println("Тест 19: удаление подзадачи");
        boolean deleteSubTaskResult = taskManager.deleteSubTasks(subTask4.getId()) != null;
        System.out.println("Удаление должно пройти успешно: " + deleteSubTaskResult);
        System.out.println("Обновленный эпик должен иметь обновленный статус: " + taskManager.getEpic());
        System.out.println("Список подзадач теперь содержит:");
        System.out.println(taskManager.getSubTaskFromEpic(epic3.getId()));
        System.out.println();

    }
}

