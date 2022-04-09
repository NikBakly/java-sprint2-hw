import KVServer.KVServer;
import controller.HTTPTaskManager;
import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HTTPTaskManagerTest {
    private final Managers managers = new Managers();
    private final KVServer kvServer = new KVServer();

    private TaskManager taskManager;
    private Task task;
    private Subtask subtask;
    private Epic epic;

    @BeforeEach
    void start() {
        kvServer.start();
        String uri = "http://localhost:8088";
        taskManager = managers.getHttpTaskManager(uri);
        task = new Task("Test", "Test1");
        epic = new Epic("Eat", "soup");
        subtask = new Subtask("cook", "soup");
    }

    @AfterEach
    void stop() {
        kvServer.stop();
    }

    @Test
    void test1_shouldSavedTaskToServer() {
        task.setDuration(60);
        task.setStartTime("09.04.2022|13:00");
        taskManager.createNewTask(task);
        Assertions.assertEquals(
                task,
                ((HTTPTaskManager) taskManager).load(String.valueOf(task.getId())),
                "Задачи различаются");
    }

    @Test
    void test2_shouldSavedSubtaskToServer() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);
        Task task = ((HTTPTaskManager) taskManager).load(String.valueOf(subtask.getId()));
        Assertions.assertEquals(
                subtask.toString(),
                ((HTTPTaskManager) taskManager).load(String.valueOf(subtask.getId())).toString(),
                "Подзадачи разные ");

    }

    @Test
    void test3_shouldSavedEpicToServer() {
        taskManager.createNewEpic(epic);
        taskManager.createNewSubtask(subtask, epic);
        Assertions.assertEquals(
                epic.toString(),
                ((HTTPTaskManager) taskManager).load(String.valueOf(epic.getId())).toString(),
                "Эпики разные"
        );

    }
}
