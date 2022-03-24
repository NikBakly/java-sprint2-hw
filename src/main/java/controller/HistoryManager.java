package controller;

import model.Task;

import java.util.List;

public interface HistoryManager {
    //для добавления нового просмотра задачи
    void add(Task task);

    //для удаления просмотра из истории
    void remove(int id);

    //для получения истории последних просмотров
    List<Task> getHistory();
}
