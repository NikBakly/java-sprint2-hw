package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static model.Status.INPROGRESS;


public class InMemoryTasksManager implements TaskManager {
    private int generationId = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    // история просмотренных задач
    private final InMemoryHistoryManager taskHistory = new InMemoryHistoryManager();


    //    Получение списка всех задач
    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasksAll = new ArrayList<>(tasks.values());
        tasksAll.addAll(subtasks.values());
        return tasksAll;
    }

    //    Получение списка всех эпиков.
    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    //    Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Task> getAllSubtasks(int idEpic) {
        return new ArrayList<>(epics.get(idEpic).getSubtasks());
    }

    //    Получение задачи любого типа по идентификатору.
    @Override
    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public Subtask findSubtaskById(int id) {
        //добавление в историю просмотренных задач
        taskHistory.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic findEpicById(int id) {
        //добавление в историю просмотренных задач
        taskHistory.add(epics.get(id));
        return epics.get(id);
    }

    //    Добавление новой задачи, эпика и подзадачи. Сам объект должен передаваться в качестве параметра.
    @Override
    public void createNewTask(Task task) {
        if (task.getId() != 0)
            System.out.println("Такая задача уже существует, ее id = " + task.getId());
        else {
            int idTask = ++generationId;
            //Присваиваем индивидуальное id к объекту
            task.setId(idTask);
            //Добавляем в словарь
            tasks.put(idTask, task);
            System.out.println("Успешно создана задача;");
        }
    }

    @Override
    public void createNewSubtask(Subtask subtask, Epic epic) {
        if (subtask.getId() != 0)
            System.out.println("Такая подзадача уже существует, ее id = " + subtask.getId());
        else {
            int idSubtask = ++generationId;
            //Присваиваем индивидуальное id к объекту
            subtask.setId(idSubtask);
            //Привязываем к подзадаче ссылку на эпик
            subtask.setEpic(epic);
            //Добавляем в словарь
            subtasks.put(idSubtask, subtask);
            //По id эпика добавим в массив объект Subtask
            epics.get(subtask.getEpic().getId()).getSubtasks().add(subtask);
            System.out.println("Успешно создана подзадача;");
        }
    }

    @Override
    public void createNewEpic(Epic epic) {
        if (epic.getId() != 0)
            System.out.println("Такой эпик уже существует, ее id = " + epic.getId());
        else {
            int idEpic = ++generationId;
            //Присваиваем индивидуальное id к объекту
            epic.setId(idEpic);
            epics.put(idEpic, epic);
            System.out.println("Успешно создан эпик;");
        }
    }

    //Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра.
    @Override
    public Task updateTaskById(int id, Task task) {
        //Находим объект в словаре по его id
        if (tasks.containsKey(id)) {
            Task value = tasks.get(id);
            //обновление объекта
            value.setName(task.getName());
            value.setSpecification(task.getSpecification());
            value.setStatus(task.getStatus());

            System.out.println("Задача по id = " + id + " успешно обновлено");
            return value;
        } else {
            System.out.println("По id = " + id + " такой задачи не существует;");
            return null;
        }
    }

    @Override
    public Subtask updateSubtaskById(int id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            Subtask value = subtasks.get(id);
            //Если в объекте subtask обновленная ссылка на epic, то мы меняем ссылку и обновляем массив в
            if (!value.getEpic().equals(subtask.getEpic()) && subtask.getEpic() != null) {
                //По старому id удалили подзадачу из старого списака эпики
                epics.get(value.getEpic().getId()).getSubtasks().remove(value);
                //По новому id добавили подзадачу в список эпика
                epics.get(subtask.getEpic().getId()).getSubtasks().add(value);
                //Обновляем ссылку на эпик в объекту подзадача
                value.setEpic(subtask.getEpic());
            }
            //обновление объекта
            value.setName(subtask.getName());
            value.setSpecification(subtask.getSpecification());
            value.setStatus(subtask.getStatus());

            System.out.println("Подзадача по id = " + id + " успешно обновлено");
            //проверяем статус эпика
            epics.get(value.getEpic().getId()).checkStatusDoneSubtasks();
            //если эпик еще не в статусе INPROGRESS, то провить на этот стутус
            if (!epics.get(value.getEpic().getId()).getStatus().equals(INPROGRESS))
                epics.get(value.getEpic().getId()).checkStatusInProgressSubtasks();
            return value;
        } else {
            System.out.println("По id = " + id + " такой подзадачи не существует;");
            return null;
        }
    }

    @Override
    public Epic updateEpicById(int id, Epic epic) {
        if (tasks.containsKey(id)) {
            Epic value = epics.get(id);
            //обновление объекта
            value.setName(epic.getName());
            value.setSpecification(epic.getSpecification());
            value.setStatus(epic.getStatus());

            System.out.println("Эпик по id = " + id + " успешно обновлено");
            return value;
        } else {
            System.out.println("По id = " + id + " такого эпика не существует;");
            return null;
        }
    }

    //Удаление ранее добавленных задач — всех и по идентификатору.
    @Override
    public void deleteTaskById(int id) {
        //удаляем из словаря задач определенную задачу по id
        tasks.remove(id);
        System.out.println("Успешное удалиние Задачи id = " + id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        //Удаляем из истории просмотренных задач
        taskHistory.remove(id);
        //Удаляем у списка Эпика подзадачу
        epics.get(subtasks.get(id).getEpic().getId()).getSubtasks().remove(subtasks.get(id));
        //удаляем из словаря подзадач определенную подзадачу по id
        subtasks.remove(id);
        System.out.println("Успешное удалиние Подзадачи id = " + id);
    }

    @Override
    public void deleteEpicById(int id) {
        //Удалим эпик из истории просмотренных задач
        taskHistory.remove(id);

        //удалить подзадачи эпика из HashMap
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            //удаляем подзадачи из истории просмотренных задач
            taskHistory.remove(subtask.getId());
            subtasks.remove(subtask.getId());
        }
        //Удалить из HashMap эпик по id
        epics.remove(id);
        System.out.println("Успешное удаление Эпики id = " + id);
    }

    //Вывод истории просмотренных задач
    @Override
    public List<Task> history() {
        return taskHistory.getHistory();
    }

}
