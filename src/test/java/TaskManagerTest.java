import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

abstract public class TaskManagerTest<T extends TaskManager> {
    private final T object;

    public TaskManagerTest(T object) {
        this.object = object;
    }

    @Test
    public void shouldGetAllEpics() {
        TaskManager taskManager = object;
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
    public void shouldFindTaskById() {
        TaskManager taskManager = object;
        Task task = new Task("Выпить стакан воды");
        taskManager.createNewTask(task);

        Assertions.assertEquals(task, taskManager.findTaskById(task.getId()));
    }

    @Test
    public void shouldFindSubtaskById() {
        TaskManager taskManager = object;
        Epic epic = new Epic("Выпить стакан воды");
        Subtask drinkWater = new Subtask("Выпить воду из стакана");

        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(drinkWater, epic);

        Assertions.assertEquals(drinkWater, taskManager.findSubtaskById(drinkWater.getId()));
    }

    @Test
    public void shouldFindEpicById() {
        TaskManager taskManager = object;
        Epic epic = new Epic("Выпить стакан воды");

        taskManager.createNewEpic(epic);

        Assertions.assertEquals(epic, taskManager.findEpicById(epic.getId()));
    }

    @Test
    public void shouldCreateNewTask() {
        TaskManager taskManager = object;
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
    public void shouldDoNotCreateNewTask() {
        TaskManager taskManager = object;
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
    public void shouldCreateNewSubtask() {
        TaskManager taskManager = object;
        Epic epic = new Epic("Выпить стакан воды");
        Subtask drinkWater = new Subtask("Выпить воду из стакана");

        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(drinkWater, epic);

        Subtask[] expectedAllSubtasks = new Subtask[]{drinkWater};
        Subtask[] allSubtasks = taskManager.getAllSubtasks(epic.getId()).toArray(Subtask[]::new);

        Assertions.assertArrayEquals(expectedAllSubtasks, allSubtasks, "Массивы не равны");
    }

    @Test
    public void shouldDoNotCreateNewSubtask() {
        TaskManager taskManager = object;
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
    public void shouldCreateNewEpic() {
        TaskManager taskManager = object;
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
    public void shouldDoNotCreateNewEpic() {
        TaskManager taskManager = object;
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
    public void shouldUpdateTaskById() {
        TaskManager taskManager = object;
        Task task = new Task("Выпить стакан воды");
        taskManager.createNewTask(task);

        Task expectedTask = new Task("Убрать стакан");
        expectedTask.setId(task.getId());

        Assertions.assertEquals(expectedTask, taskManager.updateTaskById(task.getId(), new Task("Убрать стакан")));

    }

    @Test
    public void shouldDoNotUpdateTaskById() {
        TaskManager taskManager = object;
        Task task = new Task("Выпить стакан воды");
        taskManager.createNewTask(task);

        Assertions.assertNull(taskManager.updateTaskById(task.getId() + 1, new Task("Убрать стакан")));
    }

    @Test
    public void shouldUpdateSubtasksByIdWithoutEpic() {
        TaskManager taskManager = object;
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
    public void shouldUpdateSubtasksByIdWithEpic() {
        TaskManager taskManager = object;

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
    public void shouldDoNotUpdateSubtasksById() {
        TaskManager taskManager = object;
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
    public void shouldUpdateEpicById() {
        TaskManager taskManager = object;
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
    public void shouldDoNotUpdateEpicById() {
        TaskManager taskManager = object;
        Epic epic = new Epic("Выпить стакан воды");
        taskManager.createNewEpic(epic);
        //проверка на null, если обновить не существующий эпик по id
        Assertions.assertNull(taskManager.updateEpicById(epic.getId() + 1, new Epic("Убрать стакан")));
    }

    @Test
    public void shouldDeleteTaskById() {
        TaskManager taskManager = object;
        Task jump = new Task("Прыгнуть");
        //добавили задачу
        taskManager.createNewTask(jump);
        //удаляем задачу
        taskManager.deleteTaskById(jump.getId());
        //проверка
        Assertions.assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldDeleteSubtaskById() {
        TaskManager taskManager = object;
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
    public void shouldDeleteEpicById() {
        TaskManager taskManager = object;
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
    public void shouldGetHistory() {
        TaskManager taskManager = object;
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