import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private final Managers managers = new Managers();

    @BeforeEach
    private void beforeEachCreateEpicAndSubtasks() {

    }

    @Test
    public void shouldBeEmptyListOfSubtasks() {
        //Пустой список подзадач.
        Epic epic = new Epic("Допрыгнуть до кольца");
        assertTrue(epic.getSubtasks().isEmpty());
    }

    @Test
    public void shouldAllSubtasksWithNewStatus() {
        //Все подзадачи со статусом NEW
        Epic epic = new Epic("Допрыгнуть до кольца");
        Subtask subtask = new Subtask("Разогнаться");
        Subtask subtask1 = new Subtask("Прыгнуть");

        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask1);

        subtask.setEpic(epic);
        subtask1.setEpic(epic);

        //проверка
        assertEquals(Status.NEW, epic.getStatus());
    }


    @Test
    public void shouldAllSubtasksWithDoneStatus() {
        //Все подзадачи со статусом DONE
        Epic epic = new Epic("Допрыгнуть до кольца");
        Subtask subtask = new Subtask("Разогнаться");
        Subtask subtask1 = new Subtask("Прыгнуть");

        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask1);

        subtask.setEpic(epic);
        subtask1.setEpic(epic);

        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        //проверка статусов у Subtasks Epic'ов
        epic.checkStatusDoneSubtasks();
        //проверка
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldAllSubtasksWithInProgressStatus() {
        //Все подзадачи со статусом IN_PROGRESS
        Epic epic = new Epic("Допрыгнуть до кольца");
        Subtask subtask = new Subtask("Разогнаться");
        Subtask subtask1 = new Subtask("Прыгнуть");

        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask1);

        subtask.setEpic(epic);
        subtask1.setEpic(epic);

        subtask.setStatus(Status.INPROGRESS);
        boolean beINProgress = false;

        for (Subtask epicSubtask : epic.getSubtasks()) {
            if (epicSubtask.getStatus().equals(Status.INPROGRESS)) {
                beINProgress = true;
                break;
            }
        }
        assertTrue(beINProgress);
    }

    @Test
    public void shouldAllSubtasksWithNewOrDoneStatus() {
        //Все подзадачи со статусом New и Done
        Epic epic = new Epic("Допрыгнуть до кольца");
        Subtask subtask = new Subtask("Разогнаться");
        Subtask subtask1 = new Subtask("Прыгнуть");

        epic.getSubtasks().add(subtask);
        epic.getSubtasks().add(subtask1);

        subtask.setEpic(epic);
        subtask1.setEpic(epic);

        subtask.setStatus(Status.DONE);
        boolean beNew = false;
        boolean beDone = false;

        for (Subtask epicSubtask : epic.getSubtasks()) {
            if (epicSubtask.getStatus().equals(Status.NEW))
                beNew = true;
            else if (epicSubtask.getStatus().equals(Status.DONE))
                beDone = true;
        }

        assertTrue(beNew && beDone);
    }
}
