import controller.InMemoryTasksManager;
import model.Epic;
import model.Status;
import model.Subtask;

public class Main {
    public static void main(String[] args) {
        //Тестирование программы.
        InMemoryTasksManager manager = new InMemoryTasksManager();
        Test test = new Test();
        test.testMainProgram(manager);
        /*Epic firstEpicTest = new Epic("Сдать экзамен");
        manager.createNewEpic(firstEpicTest);

        Subtask subtaskMathAnalysis = new Subtask("Мат анализ");
        Subtask subtaskComputationalMath = new Subtask("Выч мат");
        Subtask subtaskEnglish = new Subtask("Английский");

        manager.createNewSubtask(subtaskMathAnalysis, firstEpicTest);
        manager.createNewSubtask(subtaskComputationalMath, firstEpicTest);
        manager.createNewSubtask(subtaskEnglish, firstEpicTest);

        Epic secondEpicTest = new Epic("Сдать зачеты");
        manager.createNewEpic(secondEpicTest);
        subtaskEnglish = manager.updateSubtaskById(subtaskEnglish.getId(), new Subtask(null, null,
                Status.NEW, secondEpicTest));
        subtaskEnglish = manager.updateSubtaskById(subtaskEnglish.getId(), new Subtask(null, null,
                Status.INPROGRESS));
        subtaskEnglish = manager.updateSubtaskById(subtaskEnglish.getId(), new Subtask(null, null,
                Status.DONE));
        System.out.println(manager.getAllTasks());
//        subtaskComputationalMath = manager.deleteSubtaskById(subtaskComputationalMath.getId());
        manager.deleteEpicById(firstEpicTest.getId());

        System.out.println();
    }*/
    }
}
