package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    //    Получение списка всех задач
    ArrayList<Task> getAllTasks();

    //    Получение списка всех эпиков.
    ArrayList<Epic> getAllEpics();

    //    Получение списка всех подзадач определённого эпика.
    ArrayList<Task> getAllSubtasks(int idEpic);

    //    Получение задачи любого типа по идентификатору.
    Task findTaskById(int id);

    Subtask findSubtaskById(int id);

    Epic findEpicById(int id);

    //    Добавление новой задачи, эпика и подзадачи. Сам объект должен передаваться в качестве параметра.
    void createNewTask(Task task);

    void createNewSubtask(Subtask subtask, Epic epic);

    void createNewEpic(Epic epic);

    //Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра.
    Task updateTaskById(int id, Task task);

    Subtask updateSubtaskById(int id, Subtask subtask);

    Epic updateEpicById(int id, Epic epic);

    //Удаление ранее добавленных задач — всех и по идентификатору.
    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    //Вывод истории просмотренных задач
    ArrayList<Task> history();
}
