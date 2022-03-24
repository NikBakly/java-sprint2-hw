import controller.FileBackedTasksManager;
import controller.ManagerSaveException;
import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final Managers managers = new Managers();

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager("src/main/resources/history.csv"));
    }

    @Test
    public void shouldDownloadFromFile_WithEmptyTaskList() {
        //Given
        String path = "src/main/resources/history.csv";
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        Task doShop = new Task("Сделать покупку");
        firstManager.createNewTask(doShop);
        //When
        firstManager.deleteTaskById(doShop.getId());

        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();
        //Then
        Assertions.assertTrue(secondManager.getAllTasks().isEmpty());
    }

    @Test
    public void shouldDownloadFromFile_WithTaskList() {
        //Given
        String path = "src/main/resources/history.csv";
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        Task doShop = new Task("Сделать покупку");
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
    public void shouldDownloadFromFile_WithoutSubtasksEpic() {
        //Given
        String path = "src/main/resources/history.csv";
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        Epic epic = new Epic("Test");
        firstManager.createNewEpic(epic);

        //When
        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();

        //Then
        Assertions.assertTrue(secondManager.getAllSubtasks(epic.getId()).isEmpty());
    }

    @Test
    public void shouldDownloadFromFile_EpicWithSubtasks() {
        //Given
        String path = "src/main/resources/history.csv";
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        Epic epic = new Epic("Test");
        Subtask subtask1 = new Subtask("subtask1");
        Subtask subtask2 = new Subtask("subtask2");
        Subtask subtask3 = new Subtask("subtask3");

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
    public void shouldDownloadFromFile_WithEmptyHistoryList() {
        //Given
        String path = "src/main/resources/history.csv";
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        Task doShop = new Task("Сделать покупку");
        firstManager.createNewTask(doShop);

        //When
        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();

        //Then
        Assertions.assertNull(secondManager.history());
    }

    @Test
    public void shouldDownloadFromFile_WithHistoryList() {
        //Given
        String path = "src/main/resources/history.csv";
        TaskManager firstManager = managers.getFileBackedTasksManager(path);
        Task test1 = new Task("test1");
        Task test2 = new Task("test2");
        firstManager.createNewTask(test1);
        firstManager.createNewTask(test2);

        Epic epicTest = new Epic("epicTest");
        Subtask test3 = new Subtask("test3");
        Subtask test4 = new Subtask("test4");

        firstManager.createNewEpic(epicTest);
        firstManager.createNewSubtask(test3, epicTest);
        firstManager.createNewSubtask(test4, epicTest);

        //вызывает задачи, чтобы заполнить историю
        firstManager.findTaskById(test1.getId());
        firstManager.findTaskById(test2.getId());
        firstManager.findEpicById(epicTest.getId());
        firstManager.findSubtaskById(test3.getId());
        firstManager.findSubtaskById(test4.getId());


        //When

        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();

        //Then
        Assertions.assertEquals(5, secondManager.history().size());
    }

}
