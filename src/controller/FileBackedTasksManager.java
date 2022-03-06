package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {
    private final File file;
    public static final String START_LINE = "id,type,name,status,description,epic\n";

    public static void main(String[] args) {
        String path = "src\\res\\history.csv";
        //Тестирование программы.
        Managers managers = new Managers();
        TaskManager firstManager = managers.getFileBackedTasksManager(path);

        //Создаем две задачи
        Task doShop = new Task("Сделать покупку");
        Task comeHome = new Task("Прийти Домой");
        firstManager.createNewTask(doShop);
        firstManager.createNewTask(comeHome);

        //создаем первый эпик
        Epic takeExams = new Epic("Сдать экзамены");
        firstManager.createNewEpic(takeExams);

        //Создаем подзадачи для первого эпика
        Subtask subtaskMathAnalysis = new Subtask("Мат анализ");
        Subtask subtaskComputationalMath = new Subtask("Выч мат");
        Subtask subtaskEnglish = new Subtask("Английский");

        //Добавляем подзадачи в список подзадач к первому эпику
        firstManager.createNewSubtask(subtaskMathAnalysis, takeExams);
        firstManager.createNewSubtask(subtaskComputationalMath, takeExams);
        firstManager.createNewSubtask(subtaskEnglish, takeExams);

        //создаем второй эпик, у которого не будет подзадач
        Epic makeEats = new Epic("Приготовить еду");
        firstManager.createNewEpic(makeEats);

        firstManager.updateTaskById(doShop.getId(),
                new Task(doShop.getName(), doShop.getSpecification(), Status.DONE));
        firstManager.updateEpicById(makeEats.getId(),
                new Epic(makeEats.getName(), makeEats.getSpecification(), Status.INPROGRESS));

        /*Запрашиваем созданные задачи в разном порядке и
                после каждого запроса выводим историю и убеждаемся, что в ней нет повторов*/
        System.out.println("Эпик: " + firstManager.findEpicById(takeExams.getId()));
        System.out.println(firstManager.history());
        System.out.println("Подзадача: " + firstManager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println(firstManager.history());
        System.out.println("Подзадача: " + firstManager.findSubtaskById(subtaskMathAnalysis.getId()));
        System.out.println(firstManager.history());

        System.out.println("Подзадача: " + firstManager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println(firstManager.history());
        System.out.println("Задача: " + firstManager.findTaskById(doShop.getId()));
        System.out.println(firstManager.history());
        System.out.println("Подзадача: " + firstManager.findSubtaskById(subtaskComputationalMath.getId()));
        System.out.println(firstManager.history());

        System.out.println("Эпик: " + firstManager.findEpicById(makeEats.getId()));
        System.out.println(firstManager.history());
        System.out.println("Задача: " + firstManager.findTaskById(comeHome.getId()));
        System.out.println(firstManager.history());
        System.out.println("Подзадача: " + firstManager.findSubtaskById(subtaskEnglish.getId()));
        System.out.println(firstManager.history());

        //Создаем новый FIleBackedTasksManager
        TaskManager secondManager = managers.getFileBackedTasksManager(path);
        ((FileBackedTasksManager) secondManager).downloadFromFile();
        System.out.println();

    }

    public FileBackedTasksManager(String path) {
        file = new File(path);
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewSubtask(Subtask subtask, Epic epic) {
        super.createNewSubtask(subtask, epic);
        save();
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    @Override
    public Task findTaskById(int id) {
        Task task = super.findTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask findSubtaskById(int id) {
        Subtask subtask = super.findSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic findEpicById(int id) {
        Epic epic = super.findEpicById(id);
        save();
        return epic;
    }

    @Override
    public Task updateTaskById(int id, Task task) {
        Task updateTask = super.updateTaskById(id, task);
        save();
        return updateTask;
    }

    @Override
    public Subtask updateSubtaskById(int id, Subtask subtask) {
        Subtask updatedSubtask = super.updateSubtaskById(id, subtask);
        save();
        return updatedSubtask;
    }

    @Override
    public Epic updateEpicById(int id, Epic epic) {
        Epic updateEpic = super.updateEpicById(id, epic);
        save();
        return updateEpic;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    // Возвращает эпик
    public Epic getEpicById(int id) {
        //добавление в историю просмотренных задач
        return epics.get(id);
    }

    //Сохраняет текущие состояние менеджера в указанный файл
    private void save() {
        try (Writer fileWriterStart = new FileWriter(file)) {
            fileWriterStart.write(START_LINE);

            for (Task task : tasks.values()) {
                fileWriterStart.write(toString(task) + "\n");
            }
            for (Epic epic : epics.values()) {
                fileWriterStart.write(toString(epic) + "\n");
            }
            for (Subtask subtask : subtasks.values()) {
                fileWriterStart.write(toString(subtask) + "\n");
            }
            if (taskHistory.getHistory() != null)
                fileWriterStart.write(toStringHistory(taskHistory));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    //Сохраняет задачу в строку
    private String toString(Task task) {
        String typeTask = task.getClass().getSimpleName();
        String result;
        if (typeTask.equals("Task") || typeTask.equals("Epic")) {
            result = String.format("%d,%s,%s,%s,%s,",
                    task.getId(),
                    typeTask,
                    task.getName(),
                    task.getStatus(),
                    task.getSpecification()
            );
        } else {
            result = String.format("%d,%s,%s,%s,%s,%d",
                    task.getId(),
                    typeTask,
                    task.getName(),
                    task.getStatus(),
                    task.getSpecification(),
                    ((Subtask) task).getEpic().getId()
            );
        }
        return result;
    }

    //Создание задачи из строки
    private Task fromString(String value) {
        String[] parameters = value.split(",");
        /*parameters[0] - id,
         parameters[1] - type,
         parameters[2] - name,
         parameters[3] - status,
         parameters[4] - Specification,
         parameters[5] - epic */
        String typeTask = parameters[1];
        Task task = null;
        switch (typeTask) {
            case "Task" -> {
                task = new Task(parameters[2], parameters[4], Status.valueOf(parameters[3]));
                task.setId(Integer.parseInt(parameters[0]));
            }
            case "Epic" -> {
                task = new Epic(parameters[2], parameters[4], Status.valueOf(parameters[3]));
                task.setId(Integer.parseInt(parameters[0]));
            }
            case "Subtask" -> {
                task = new Subtask(parameters[2], parameters[4], Status.valueOf(parameters[3]));
                task.setId(Integer.parseInt(parameters[0]));
                if (epics.containsKey(Integer.parseInt(parameters[5])))
                    ((Subtask) task).setEpic(getEpicById(Integer.parseInt(parameters[5])));
            }
        }
        return task;
    }

    //Сохранение менеджера истории
    public static String toStringHistory(HistoryManager manager) {
        List<Task> tasksHistory = manager.getHistory();
        StringBuilder idHistory = new StringBuilder();
        idHistory.append("\n");
        for (int i = 0; i < tasksHistory.size(); i++) {
            //проверка на последний элемент массива
            if (i == tasksHistory.size() - 1)
                idHistory.append(String.format("%d", tasksHistory.get(i).getId()));
            else
                idHistory.append(String.format("%d,", tasksHistory.get(i).getId()));
        }

        return idHistory.toString();
    }

    //Восстановление менеджера истории из CSV
    public static List<Integer> fromStringHistory(String value) {
        List<Integer> historyId = new ArrayList<>();
        String[] idHistoryTasks = value.split(",");
        for (String id : idHistoryTasks) {
            historyId.add(Integer.parseInt(id));
        }
        return historyId;
    }

    //считывание информации из файла
    public void downloadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isBlank()) {
                    if (!line.equals("id,type,name,status,description,epic")) {
                        Task task = fromString(line);
                        switch (task.getClass().getSimpleName()) {
                            case "Task" -> tasks.put(task.getId(), task);
                            case "Epic" -> epics.put(task.getId(), (Epic) task);
                            case "Subtask" -> subtasks.put(task.getId(), (Subtask) task);
                        }
                    }
                } else {
                    String newLine = br.readLine();
                    List<Integer> idHistory = fromStringHistory(newLine);
                    Task task = null;
                    for (Integer id : idHistory) {
                        if (tasks.containsKey(id)) {
                            task = tasks.get(id);
                            taskHistory.add(task);
                        } else if (epics.containsKey(id)) {
                            task = epics.get(id);
                            taskHistory.add(task);
                        } else if (subtasks.containsKey(id)) {
                            task = subtasks.get(id);
                            taskHistory.add(task);
                        }
                    }
                    if (task != null)
                        taskHistory.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }
}
