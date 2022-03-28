import controller.FileBackedTasksManager;
import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final Managers managers = new Managers();
    private final String path = "src/main/resources/history.csv";

    private Task doShop;
    private Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Task test1;
    Task test2;

    @BeforeEach
    void init() {
        doShop = new Task("Сделать покупку");
        epic = new Epic("Test");
        subtask1 = new Subtask("subtask1");
        subtask2 = new Subtask("subtask2");
        subtask3 = new Subtask("subtask3");
        test1 = new Task("test1");
        test2 = new Task("test2");
    }

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager("src/main/resources/history.csv"));
    }

    @Test
    public void test1_shouldDownloadFromFile_WithEmptyTaskList() {
        //Given
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        firstManager.createNewTask(doShop);
        //When
        firstManager.deleteTaskById(doShop.getId());

        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();
        //Then
        Assertions.assertTrue(secondManager.getAllTasks().isEmpty());
    }

    @Test
    public void test2_shouldDownloadFromFile_WithTaskList() {
        //Given
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        firstManager.createNewTask(doShop);

        //When
        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();

        //Then
        //проверяем, что в новом менеджере есть:
        //задача с одинаковым названием
        Assertions.assertEquals(doShop.getName(), secondManager.findTaskById(doShop.getId()).getName());
        //задача с пустым описанием
        Assertions.assertEquals("null", secondManager.findTaskById(doShop.getId()).getSpecification());
        //задача с одинаковым статусом
        Assertions.assertEquals(doShop.getStatus(), secondManager.findTaskById(doShop.getId()).getStatus());
    }

    @Test
    public void test3_shouldDownloadFromFile_WithoutSubtasksEpic() {
        //Given
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        firstManager.createNewEpic(epic);

        //When
        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();

        //Then
        Assertions.assertTrue(secondManager.getAllSubtasks(epic.getId()).isEmpty());
    }

    @Test
    public void test4_shouldDownloadFromFile_EpicWithSubtasks() {
        //Given
        TaskManager firstManager = managers.getFileBackedTasksManager(path);

        firstManager.createNewEpic(epic);
        firstManager.createNewSubtask(subtask1, epic);
        firstManager.createNewSubtask(subtask2, epic);
        firstManager.createNewSubtask(subtask3, epic);

        //When
        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();

        //Then
        //проверим на количество подзадач у скопированного эпика в новом менеджере
        Assertions.assertEquals(3, secondManager.getAllSubtasks(epic.getId()).size());
    }


    @Test
    public void test5_shouldDownloadFromFile_WithEmptyHistoryList() {
        //Given
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        firstManager.createNewTask(doShop);

        //When
        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();

        //Then
        Assertions.assertNull(secondManager.history());
    }

    @Test
    public void test6_shouldDownloadFromFile_WithHistoryList() {
        //Given
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        firstManager.createNewTask(test1);
        firstManager.createNewTask(test2);

        firstManager.createNewEpic(epic);
        firstManager.createNewSubtask(subtask1, epic);
        firstManager.createNewSubtask(subtask2, epic);

        //вызывает задачи, чтобы заполнить историю
        firstManager.findTaskById(test1.getId());
        firstManager.findTaskById(test2.getId());
        firstManager.findEpicById(epic.getId());
        firstManager.findSubtaskById(subtask1.getId());
        firstManager.findSubtaskById(subtask2.getId());


        //When

        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();

        //Then
        Assertions.assertEquals(5, secondManager.history().size());
    }

    @Test
    void test7_checkingRecoveryFromCVCFile() {
        TaskManager firstTaskManager = new FileBackedTasksManager(path);
        //Назначаем продолжительность подзадач
        subtask1.setDuration(60);
        subtask2.setDuration(120);
        //назначение начало работы
        subtask1.setStartTime("25.03.2022|13:00");
        subtask2.setStartTime("25.03.2022|14:01");
        //связываем эпики с подзадачами и наоборот
        firstTaskManager.createNewEpic(epic);
        firstTaskManager.createNewSubtask(subtask1, epic);
        firstTaskManager.createNewSubtask(subtask2, epic);

        FileBackedTasksManager secondTaskManager = new FileBackedTasksManager(path);
        secondTaskManager.downloadFromFile();

        Assertions.assertEquals(
                "25.03.2022|13:00",
                secondTaskManager
                        .getEpicById(epic.getId())
                        .getStartTime()
                        .format(Task.formatter)
        );
        Assertions.assertEquals(
                "25.03.2022|16:01",
                secondTaskManager
                        .getEpicById(epic.getId())
                        .getEndTime().format(Task.formatter)
        );
        Assertions.assertEquals(
                180,
                secondTaskManager
                        .getEpicById(epic.getId())
                        .getDuration().toMinutes()
        );
    }

}
