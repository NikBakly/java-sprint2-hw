import controller.FileBackedTasksManager;
import controller.InMemoryTasksManager;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTasksManager> {
    public InMemoryTasksManagerTest() {
        super(new InMemoryTasksManager());
    }

    private InMemoryTasksManager taskManager;

    @BeforeEach
    void createInMemoryTasksManager() {
        taskManager = new InMemoryTasksManager();
    }

    @Test
    void test1_shouldReturnLocalDateTimeForTask() {
        String startTime = "25.03.2022|13:00";
        Task test1 = new Task("test1");
        test1.setStartTime(startTime);
        test1.setDuration(60);

        Assertions.assertEquals("25.03.2022|14:00", test1.getEndTime().format(Task.formatter));
    }

    @Test
    void test2_shouldReturnLocalDateTimeForSubtask() {
        String startTime = "25.03.2022|13:00";
        Subtask test1 = new Subtask("test1");
        test1.setStartTime(startTime);
        test1.setDuration(60);

        Assertions.assertEquals("25.03.2022|14:00", test1.getEndTime().format(Task.formatter));
    }

    @Test
    void test3_shouldReturnLocalDateTimeForEpic_WhenSubtasksStartAtSameTime() {
        //проверка корректности времени, когда подзадачи у эпика начинаются в одно время
        TaskManager taskManager = new InMemoryTasksManager();

        Epic epic = new Epic("testEpic");
        Subtask subtask1 = new Subtask("test1");
        Subtask subtask2 = new Subtask("test2");
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        subtask2.setDuration(120);
        //назначение начало работы
        subtask1.setStartTime("25.03.2022|13:00");
        subtask2.setStartTime("25.03.2022|14:01");
        //связываем эпики с подзадачами и наоборот
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask1, epic);
        taskManager.createNewSubtask(subtask2, epic);

        Assertions.assertEquals("25.03.2022|13:00", epic.getStartTime().format(Task.formatter));
        Assertions.assertEquals("25.03.2022|16:01", epic.getEndTime().format(Task.formatter));
        Assertions.assertEquals(180, epic.getDuration().toMinutes());
    }

    @Test
    void test4_shouldReturnLocalDateTimeForEpic_WhenSubtaskIsNotFullyDefinedInStartTime() {
        //проверка корректности времени, когда подзадачи у эпика начинаются в одно время
        String startTime = "25.03.2022|13:00";
        TaskManager taskManager = new InMemoryTasksManager();

        Epic epic = new Epic("testEpic");
        Subtask subtask1 = new Subtask("test1");
        Subtask subtask2 = new Subtask("test2");
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        subtask2.setDuration(120);
        //назначение начало работы
        subtask1.setStartTime(startTime);
        //связываем эпики с подзадачами и наоборот
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask1, epic);
        taskManager.createNewSubtask(subtask2, epic);

        Assertions.assertEquals(startTime, epic.getStartTime().format(Task.formatter));
        Assertions.assertEquals("25.03.2022|14:00", epic.getEndTime().format(Task.formatter));
        Assertions.assertEquals(180, epic.getDuration().toMinutes());
    }

    @Test
    void test5_shouldReturnLocalDateTimeForEpic_WhenSubtaskIsNotFullyDefinedInDuration() {
        //проверка корректности времени, когда подзадачи у эпика начинаются в одно время
        String startTime = "25.03.2022|13:00";
        TaskManager taskManager = new InMemoryTasksManager();

        Epic epic = new Epic("testEpic");
        Subtask subtask1 = new Subtask("test1");
        Subtask subtask2 = new Subtask("test2");
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        //назначение начало работы
        subtask1.setStartTime(startTime);
        subtask2.setStartTime(startTime);
        //связываем эпики с подзадачами и наоборот
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask1, epic);
        taskManager.createNewSubtask(subtask2, epic);

        Assertions.assertEquals(startTime, epic.getStartTime().format(Task.formatter));
        Assertions.assertEquals("25.03.2022|14:00", epic.getEndTime().format(Task.formatter));
        Assertions.assertEquals(60, epic.getDuration().toMinutes());
    }

    @Test
    void test6_shouldReturnLocalDateTimeForEpic_WhenSubtasksStartAtDifferentTime() {
        //проверка корректности времени, когда подзадачи у эпика начинаются в разное время
        String startTimeForSubtask1 = "25.03.2022|12:00";
        String startTimeForSubtask2 = "25.03.2022|14:00";
        TaskManager taskManager = new InMemoryTasksManager();

        Epic epic = new Epic("testEpic");
        Subtask subtask1 = new Subtask("test1");
        Subtask subtask2 = new Subtask("test2");
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        subtask2.setDuration(120);
        //назначение начало работы
        subtask1.setStartTime(startTimeForSubtask1);
        subtask2.setStartTime(startTimeForSubtask2);
        //связываем эпики с подзадачами и наоборот
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask1, epic);
        taskManager.createNewSubtask(subtask2, epic);

        Assertions.assertEquals(startTimeForSubtask1, epic.getStartTime().format(Task.formatter));
        Assertions.assertEquals("25.03.2022|16:00", epic.getEndTime().format(Task.formatter));
        Assertions.assertEquals(180, epic.getDuration().toMinutes());
    }

    @Test
    void test8_shouldReturnSortedListByTime() {
        Task test1 = new Task("test1");
        test1.setStartTime("25.03.2022|17:00");

        Task test2 = new Task("test2");
        test2.setStartTime("25.03.2022|14:00");

        Task test3 = new Task("test3");
        test3.setStartTime("25.03.2022|11:00");

        Task test4 = new Task("test4");
        test4.setStartTime("25.03.2022|16:00");
        //шпион
        Task test5 = new Task("test5");

        taskManager.createNewTask(test1);
        taskManager.createNewTask(test2);
        taskManager.createNewTask(test3);
        taskManager.createNewTask(test4);
        taskManager.createNewTask(test5);
        Task[] expectedSortedTasks = new Task[]{test3, test2, test4, test1, test5};

        List<Task> tasks = taskManager.getPrioritizedTasks();
        Assertions.assertArrayEquals(
                expectedSortedTasks,
                tasks.toArray(Task[]::new),
                "не верная сортировка по времени");
    }

    @Test
    void test9_checkingIntersectionWhenThereIsNoIntersection() {
        Task test1 = new Task("test1");
        test1.setStartTime("25.03.2022|13:00");
        test1.setDuration(60);

        Task test2 = new Task("test2");
        test2.setStartTime("25.03.2022|14:01");
        test2.setDuration(60);

        Task test3 = new Task("test3");
        test3.setStartTime("25.03.2022|15:02");
        test3.setDuration(60);

        Task test4 = new Task("test4");
        test4.setStartTime("25.03.2022|16:03");
        test4.setDuration(60);

        //задача, которая пересекается с test4
        Task test5 = new Task("test5");
        test5.setStartTime("25.03.2022|16:03");
        test5.setDuration(60);

        taskManager.createNewTask(test1);
        taskManager.createNewTask(test2);
        taskManager.createNewTask(test3);
        taskManager.createNewTask(test4);
        taskManager.createNewTask(test5);

        Task[] expectedSortedTasks = new Task[]{test1, test2, test3, test4, test5};

        List<Task> tasks = taskManager.getPrioritizedTasks();
        Assertions.assertArrayEquals(
                expectedSortedTasks,
                tasks.toArray(Task[]::new),
                "не верная сортировка при пересечение");
        Assertions.assertNull(tasks.get(tasks.size() - 1).getStartTime(), "Не правильная обработка пересечения");
        Assertions.assertNull(tasks.get(tasks.size() - 1).getDuration(), "Не правильная обработка пересечения");
    }

    @Test
    void test10_checkingIntersectionWhenThereIsIntersection() {
        Task test1 = new Task("test1");
        test1.setStartTime("25.03.2022|13:00");
        test1.setDuration(60);

        Task test2 = new Task("test2");
        test2.setStartTime("25.03.2022|14:01");
        test2.setDuration(60);

        Task test3 = new Task("test3");
        test3.setStartTime("25.03.2022|15:02");
        test3.setDuration(60);

        Task test4 = new Task("test4");
        test4.setStartTime("25.03.2022|16:03");
        test4.setDuration(60);

        //задача, которая пересекается с test3
        Task test5 = new Task("test5");
        test5.setStartTime("25.03.2022|16:02");
        test5.setDuration(60);

        taskManager.createNewTask(test1);
        taskManager.createNewTask(test2);
        taskManager.createNewTask(test3);
        taskManager.createNewTask(test4);
        taskManager.createNewTask(test5);

        Task[] expectedSortedTasks = new Task[]{test1, test2, test3, test4, test5};

        List<Task> tasks = taskManager.getPrioritizedTasks();
        Assertions.assertArrayEquals(
                expectedSortedTasks,
                tasks.toArray(Task[]::new),
                "не верная сортировка при пересечение");
        Assertions.assertNull(tasks.get(tasks.size() - 1).getStartTime(), "Не правильная обработка пересечения");
        Assertions.assertNull(tasks.get(tasks.size() - 1).getDuration(), "Не правильная обработка пересечения");
    }
}

