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
        //создаем эпик
        Epic firstEpicTest = new Epic("Сдать экзамен");
        manager.createNewEpic(firstEpicTest);

        //Создаем подзадачи
        Subtask subtaskMathAnalysis = new Subtask("Мат анализ");
        Subtask subtaskComputationalMath = new Subtask("Выч мат");
        Subtask subtaskEnglish = new Subtask("Английский");

        //Добавляем подзадачи в список подзадач к определенному эпику
        manager.createNewSubtask(subtaskMathAnalysis, firstEpicTest);
        manager.createNewSubtask(subtaskComputationalMath, firstEpicTest);
        manager.createNewSubtask(subtaskEnglish, firstEpicTest);

        //Проверка истории просмотров задач
        System.out.println("Эпик: " + manager.findEpicById(firstEpicTest.getId()));

        System.out.println("Первая подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println("Первая подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println("Первая подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println("Первая подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println("Первая подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println("Вторая подзадача: " + manager.findSubtaskById(subtaskComputationalMath.getId()));
        System.out.println("Вторая подзадача: " + manager.findSubtaskById(subtaskComputationalMath.getId()));
        System.out.println("Вторая подзадача: " + manager.findSubtaskById(subtaskComputationalMath.getId()));
        System.out.println("Вторая подзадача: " + manager.findSubtaskById(subtaskComputationalMath.getId()));
        System.out.println("Третья подзадача: " + manager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println("Третья подзадача: " + manager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println("Третья подзадача: " + manager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println("Третья подзадача: " + manager.findSubtaskById(subtaskEnglish.getId()));

        //вывод истории
        System.out.println(manager.history());

    }
}
