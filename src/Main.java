import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        //Тестирование программы.
        Managers managers = new Managers();
        TaskManager manager = managers.getDefault();
        //создаем первый эпик
        Epic takeExams = new Epic("Сдать экзамены");
        manager.createNewEpic(takeExams);

        //Создаем подзадачи
        Subtask subtaskMathAnalysis = new Subtask("Мат анализ");
        Subtask subtaskComputationalMath = new Subtask("Выч мат");
        Subtask subtaskEnglish = new Subtask("Английский");

        //Добавляем подзадачи в список подзадач к первому эпику
        manager.createNewSubtask(subtaskMathAnalysis, takeExams);
        manager.createNewSubtask(subtaskComputationalMath, takeExams);
        manager.createNewSubtask(subtaskEnglish, takeExams);

        //создаем второй эпик
        Epic makeEats = new Epic("Приготовить еду");
        manager.createNewEpic(makeEats);

        //Проверка истории просмотров задач
        System.out.println("Эпик: " + manager.findEpicById(takeExams.getId()));
        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println(manager.history());

        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskComputationalMath.getId()));
        System.out.println(manager.history());

        System.out.println("Эпик: " + manager.findEpicById(makeEats.getId()));
        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println(manager.history());

        manager.deleteSubtaskById(subtaskEnglish.getId());
        System.out.println(manager.history());

        manager.deleteEpicById(takeExams.getId());
        System.out.println(manager.history());
    }
}
