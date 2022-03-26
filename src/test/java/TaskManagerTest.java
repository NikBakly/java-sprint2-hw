import controller.FileBackedTasksManager;
import controller.InMemoryTasksManager;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.List;

abstract public class TaskManagerTest<T extends TaskManager> {
    private final T object;
    private final DateTimeFormatter formatter = Task.formatter;
    TaskManager taskManager;

    public TaskManagerTest(T object) {
        this.object = object;
    }
    @BeforeEach
    void createTaskManager(){
        taskManager = object;
    }

    @Test
    void test1_shouldGetAllEpics() {
        Epic epic = new Epic("Выпить стакан воды");
        taskManager.createNewEpic(epic);

        Epic[] expectedAllEpics = new Epic[]{epic};
        Epic[] allEpic = taskManager.getAllEpics().toArray(Epic[]::new);

        Assertions.assertArrayEquals(
                expectedAllEpics,
                allEpic,
                "Массивы не равны"
        );
    }

    @Test
    void test2_shouldFindTaskById() {
        Task task = new Task("Выпить стакан воды");
        taskManager.createNewTask(task);

        Assertions.assertEquals(task, taskManager.findTaskById(task.getId()));
    }

    @Test
    void test3_shouldFindSubtaskById() {
        Epic epic = new Epic("Выпить стакан воды");
        Subtask drinkWater = new Subtask("Выпить воду из стакана");

        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(drinkWater, epic);

        Assertions.assertEquals(drinkWater, taskManager.findSubtaskById(drinkWater.getId()));
    }

    @Test
    void test4_shouldFindEpicById() {
        Epic epic = new Epic("Выпить стакан воды");

        taskManager.createNewEpic(epic);

        Assertions.assertEquals(epic, taskManager.findEpicById(epic.getId()));
    }

    @Test
    void test5_shouldCreateNewTask() {
        Task task = new Task("Выпить стакан воды");
        taskManager.createNewTask(task);

        Task[] expectedAllTasks = new Task[]{task};
        Task[] allTask = taskManager.getAllTasks().toArray(Task[]::new);

        Assertions.assertArrayEquals(
                expectedAllTasks,
                allTask,
                "Массивы не равны"
        );
    }

    @Test
    void test6_shouldDoNotCreateNewTask() {
        Task run = new Task("Пробежать");
        taskManager.createNewTask(run);
        //пробуем создать повторную задачу в менеджере
        taskManager.createNewTask(run);
        int expectedSize = 1;
        //проверка на размер словаря
        Assertions.assertEquals(
                expectedSize,
                taskManager.getAllTasks().size(),
                "Создался дубликат задачи"
        );
    }

    @Test
    void test7_shouldCreateNewSubtask() {
        Epic epic = new Epic("Выпить стакан воды");
        Subtask drinkWater = new Subtask("Выпить воду из стакана");

        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(drinkWater, epic);

        Subtask[] expectedAllSubtasks = new Subtask[]{drinkWater};
        Subtask[] allSubtasks = taskManager.getAllSubtasks(epic.getId()).toArray(Subtask[]::new);

        Assertions.assertArrayEquals(expectedAllSubtasks, allSubtasks, "Массивы не равны");
    }

    @Test
    void test8_shouldDoNotCreateNewSubtask() {
        Epic epic = new Epic("Выпить стакан воды");
        Subtask drinkWater = new Subtask("Выпить воду из стакана");
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(drinkWater, epic);
        //пробуем создать повторную подзадачу в менеджере
        taskManager.createNewSubtask(drinkWater, epic);
        int expectedSize = 1;
        //проверка на размер словаря
        Assertions.assertEquals(
                expectedSize,
                taskManager.getAllSubtasks(epic.getId()).size(),
                "Создался дубликат подзадачи"
        );
    }

    @Test
    void test9_shouldCreateNewEpic() {
        Epic epic = new Epic("Выпить стакан воды");
        taskManager.createNewEpic(epic);

        Epic[] expectedAllEpics = new Epic[]{epic};
        Epic[] allEpic = taskManager.getAllEpics().toArray(Epic[]::new);

        Assertions.assertArrayEquals(
                expectedAllEpics,
                allEpic,
                "Массивы не равны"
        );
    }

    @Test
    void test10_shouldDoNotCreateNewEpic() {
        Epic epic = new Epic("Выпить стакан воды");
        taskManager.createNewEpic(epic);
        //пробуем создать повторный эпик в менеджере
        taskManager.createNewEpic(epic);
        int expectedSize = 1;
        //проверка на размер словаря
        Assertions.assertEquals(
                expectedSize,
                taskManager.getAllEpics().size(),
                "Создался дубликат эпика"
        );
    }

    @Test
    void test11_shouldUpdateTaskById() {
        Task task = new Task("Выпить стакан воды");
        taskManager.createNewTask(task);

        Task expectedTask = new Task("Убрать стакан");
        expectedTask.setId(task.getId());

        Assertions.assertEquals(expectedTask, taskManager.updateTaskById(task.getId(), new Task("Убрать стакан")));

    }

    @Test
    void test12_shouldDoNotUpdateTaskById() {
        Task task = new Task("Выпить стакан воды");
        taskManager.createNewTask(task);

        Assertions.assertNull(taskManager.updateTaskById(task.getId() + 1, new Task("Убрать стакан")));
    }

    @Test
    void test13_shouldUpdateSubtasksByIdWithoutEpic() {
        Epic epic = new Epic("Выпить стакан воды");
        Subtask drinkWater = new Subtask("Выпить воду из стакана");

        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(drinkWater, epic);

        Subtask expectedSubtask = new Subtask("убрать стакан");
        //подготавливаем подзадачу
        expectedSubtask.setEpic(epic);
        expectedSubtask.setId(drinkWater.getId());

        //проверка без нового эпика
        Assertions.assertEquals(
                expectedSubtask,
                taskManager.updateSubtaskById(drinkWater.getId(), expectedSubtask),
                "Подзадачи разные"
        );
    }

    @Test
    void test14_shouldUpdateSubtasksByIdWithEpic() {
        Epic readNews = new Epic("Прочитать новости");
        Subtask readNewsPapers = new Subtask("Читать новостную газету");
        taskManager.createNewEpic(readNews);
        taskManager.createNewSubtask(readNewsPapers, readNews);

        //новой эпик и подзадача, которые будут использоваться
        Epic cleanRoom = new Epic("Убраться в комнате");
        taskManager.createNewEpic(cleanRoom);
        //ожидаемый результат
        Subtask expectedSubtask = new Subtask("Выкинуть мусор");
        //связываем с эпиком подзадачу
        expectedSubtask.setEpic(cleanRoom);
        //подготавливаем подзадачу для сравнения
        expectedSubtask.setId(readNewsPapers.getId());
        //проверка с новым эпиком
        Assertions.assertEquals(
                expectedSubtask,
                taskManager.updateSubtaskById(readNewsPapers.getId(), expectedSubtask),
                "Подзадачи разные"
        );
    }

    @Test
    void test15_shouldDoNotUpdateSubtasksById() {
        Epic readNews = new Epic("Прочитать новости");
        Subtask readNewsPapers = new Subtask("Читать новостную газету");
        taskManager.createNewEpic(readNews);
        taskManager.createNewSubtask(readNewsPapers, readNews);
        //проверка на null, если обновить не существующею подзадачу по id
        Assertions.assertNull(
                taskManager.updateSubtaskById(readNewsPapers.getId() + 1, new Subtask("Убрать стакан"))
        );
    }

    @Test
    void test16_shouldUpdateEpicById() {
        Epic epic = new Epic("Выпить стакан воды");
        taskManager.createNewEpic(epic);

        Epic expectedEpic = new Epic("Убрать стакан");
        // подготовка для сравнения
        expectedEpic.setId(epic.getId());


        Assertions.assertEquals(
                expectedEpic,
                taskManager.updateEpicById(epic.getId(), new Epic("Убрать стакан"))
        );

    }

    @Test
    void test17_shouldDoNotUpdateEpicById() {
        Epic epic = new Epic("Выпить стакан воды");
        taskManager.createNewEpic(epic);
        //проверка на null, если обновить не существующий эпик по id
        Assertions.assertNull(taskManager.updateEpicById(epic.getId() + 1, new Epic("Убрать стакан")));
    }

    @Test
    void test18_shouldDeleteTaskById() {
        Task jump = new Task("Прыгнуть");
        //добавили задачу
        taskManager.createNewTask(jump);
        //удаляем задачу
        taskManager.deleteTaskById(jump.getId());
        //проверка
        Assertions.assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void test19_shouldDeleteSubtaskById() {
        Epic winOlympics = new Epic("Выиграть олимпиаду");
        Subtask time = new Subtask("Уложиться во время");
        //добавили эпик и подзадачу
        taskManager.createNewEpic(winOlympics);
        taskManager.createNewSubtask(time, winOlympics);
        //удаляем подзадачу
        taskManager.deleteSubtaskById(time.getId());
        //проверка, что удалился у эпика
        Assertions.assertTrue(winOlympics.getSubtasks().isEmpty());
        //проверка, что удалился из словаря менеджера, с помощью обновления, который вернет null, если нет Subtask
        Assertions.assertNull(taskManager.updateSubtaskById(time.getId(), new Subtask("Выспаться")));
    }

    @Test
    void test20_shouldDeleteEpicById() {
        Epic winOlympics = new Epic("Выиграть олимпиаду");
        Subtask time = new Subtask("Уложиться во время");
        //добавили эпик и подзадачу
        taskManager.createNewEpic(winOlympics);
        taskManager.createNewSubtask(time, winOlympics);
        //удаляем эпик
        taskManager.deleteEpicById(winOlympics.getId());
        //проверка, что в словаре менеджера эпика не осталось
        Assertions.assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void test20_shouldGetHistory() {
        Task task = new Task("дописать тесты");
        taskManager.createNewTask(task);
        //добавим в историю task
        taskManager.findTaskById(task.getId());
        //ожидаемый результат
        Task[] expectedHistoryTasks = new Task[]{task};
        Task[] historyTasks = taskManager.history().toArray(Task[]::new);
        //проверка
        Assertions.assertArrayEquals(expectedHistoryTasks, historyTasks);
    }
}
