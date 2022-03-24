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

        //Создаем две задачи
        Task doShop = new Task("Сделать покупку");
        Task comeHome = new Task("Прийти Домой");
        manager.createNewTask(doShop);
        manager.createNewTask(comeHome);

        //создаем первый эпик
        Epic takeExams = new Epic("Сдать экзамены");
        manager.createNewEpic(takeExams);

        //Создаем подзадачи для первого эпика
        Subtask subtaskMathAnalysis = new Subtask("Мат анализ");
        Subtask subtaskComputationalMath = new Subtask("Выч мат");
        Subtask subtaskEnglish = new Subtask("Английский");

        //Добавляем подзадачи в список подзадач к первому эпику
        manager.createNewSubtask(subtaskMathAnalysis, takeExams);
        manager.createNewSubtask(subtaskComputationalMath, takeExams);
        manager.createNewSubtask(subtaskEnglish, takeExams);

        //создаем второй эпик, у которого не будет подзадач
        Epic makeEats = new Epic("Приготовить еду");
        manager.createNewEpic(makeEats);

        /*Запрашиваем созданные задачи в разном порядке и
                после каждого запроса выводим историю и убеждаемся, что в ней нет повторов*/
        System.out.println("Эпик: " + manager.findEpicById(takeExams.getId()));
        System.out.println(manager.history());
        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println(manager.history());
        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println(manager.history());

        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println(manager.history());
        System.out.println("Задача: " + manager.findTaskById(doShop.getId()));
        System.out.println(manager.history());
        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskComputationalMath.getId()));
        System.out.println(manager.history());

        System.out.println("Эпик: " + manager.findEpicById(makeEats.getId()));
        System.out.println(manager.history());
        System.out.println("Задача: " + manager.findTaskById(comeHome.getId()));
        System.out.println(manager.history());
        System.out.println("Подзадача: " + manager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println(manager.history());

        //Удаляем задачу и убеждаемся, что из истории она тоже удалилась
        manager.deleteTaskById(comeHome.getId());
        System.out.println(manager.history());

        /*Удаляем эпик с тремя подзадачами и убеждаемся,
                 что из истории удалился как сам эпик, так и всего его подзадачи*/
        manager.deleteEpicById(takeExams.getId());
        System.out.println(manager.history());
    }
}
