package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;
import java.util.stream.Collectors;

import static model.Status.INPROGRESS;


public class InMemoryTasksManager implements TaskManager {
    protected int generationId = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    // история просмотренных задач
    protected final InMemoryHistoryManager taskHistory = new InMemoryHistoryManager();
    protected TreeSet<Task> sortedTasks;


    //    Получение списка всех задач
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
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
        //добавление в историю просмотренных задач
        taskHistory.add(tasks.get(id));
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
            checkingIntersection(task);
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
            checkingIntersection(subtask);
            //По id эпика добавим в массив объект Subtask
            epics.get(epic.getId()).getSubtasks().add(subtask);
            //обновляем startTime и duration
            epics.get(epic.getId()).updateStartTimeAndDuration();
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
            checkingIntersection(task);
            value.setStartTime(task.getStartTime());
            value.setDuration(task.getDuration());

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
                //По старому id удалили подзадачу из старого списка эпики
                epics.get(value.getEpic().getId()).getSubtasks().remove(value);
                //По новому id добавили подзадачу в список эпика
                epics.get(subtask.getEpic().getId()).getSubtasks().add(subtask);

                //Обновляем ссылку на эпик в объекте подзадача
                value.setEpic(subtask.getEpic());
            }
            //обновление объекта
            value.setName(subtask.getName());
            value.setSpecification(subtask.getSpecification());
            value.setStatus(subtask.getStatus());
            checkingIntersection(subtask);
            value.setStartTime(subtask.getStartTime());
            value.setDuration(subtask.getDuration());

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
        if (epics.containsKey(id)) {
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
        //Удаляем из истории просмотренных задач
        taskHistory.remove(id);
        //удаляем из словаря задач определенную задачу по id
        tasks.remove(id);
        System.out.println("Успешное удаление Задачи id = " + id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        //Удаляем из истории просмотренных задач
        taskHistory.remove(id);
        //Удаляем у списка Эпика подзадачу
        epics.get(subtasks.get(id).getEpic().getId()).getSubtasks().remove(subtasks.get(id));
        //удаляем из словаря подзадач определенную подзадачу по id
        subtasks.remove(id);
        System.out.println("Успешное удаление Подзадачи id = " + id);
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

    @Override
    public void deleteAllTasks() {
        ArrayList<Integer> idTasks = new ArrayList<>(tasks.keySet());
        //Удаляем в цикле все задачи по id
        for (Integer idTask : idTasks) {
            deleteTaskById(idTask);
        }

        System.out.println("Все задачи удалены");
    }

    @Override
    public void deleteAllSubtasks() {
        ArrayList<Integer> idSubtasks = new ArrayList<>(subtasks.keySet());
        //Удаляем в цикле все задачи по id в словаре
        for (Integer idSubtask : idSubtasks) {
            deleteSubtaskById(idSubtask);
        }

        System.out.println("Все подзадачи удалены");
    }

    @Override
    public void deleteAllEpics() {
        ArrayList<Integer> idEpics = new ArrayList<>(epics.keySet());
        //Удаляем в цикле все задачи по id
        for (Integer idEpic : idEpics) {
            deleteEpicById(idEpic);
        }

        System.out.println("Все эпики удалены");
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        //если множество определенно и размер множества должно сор
        if (sortedTasks != null && sortedTasks.size() == getAllTasks().size()) {
            List<Task> allTasks = new ArrayList<>(sortedTasks);
            allTasks.addAll(getListTasksWithoutStarTime());
            return allTasks;
        } else {
            List<Task> allTasks = getListTasksWithStarTime()
                    .stream()
                    .sorted(Comparator.comparing(Task::getStartTime))
                    .collect(Collectors.toList());
            sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
            sortedTasks.addAll(allTasks);
            allTasks.addAll(getListTasksWithoutStarTime());
            return allTasks;
        }
    }

    //проверка пересечения
    private void checkingIntersection(Task task) {
        boolean isIntersection = false;
        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (
                    !task.equals(prioritizedTask)
                    && task.getStartTime() != null
                    && prioritizedTask.getEndTime() != null
                    && (task.getStartTime().equals(prioritizedTask.getEndTime())
                        || task.getStartTime().isBefore(prioritizedTask.getEndTime()))
            ) {
                isIntersection = true;
                break;
            }
        }
        if (isIntersection) {
            System.out.println(
                    "Обнаружилось пересечение по времени у объекта с id: " + task.getId()
                            + ", нужно поменять продолжительность или время старта"
            );
            //удаление из дерева множеств, т пересекается с другими задачами
            sortedTasks.remove(task);
            //сброс времени и продолжительности, чтобы задачи не пересекались
            task.resetStartTimeAndDuration();
        }
    }

    //Возращает список всех подзадач
    private ArrayList<Task> getListAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(subtasks.values());
        return allTasks;
    }

    //возвращает список задач без начального временем
    private List<Task> getListTasksWithoutStarTime() {
        ArrayList<Task> tasksWithoutStartTime = new ArrayList<>();
        ArrayList<Task> allTasks = getListAllTasks();
        for (Task task : allTasks) {
            if (task.getStartTime() == null)
                tasksWithoutStartTime.add(task);
        }
        return tasksWithoutStartTime;
    }

    //возвращает список задач с начальным временем
    private List<Task> getListTasksWithStarTime() {
        ArrayList<Task> tasksWithStartTime = new ArrayList<>();
        ArrayList<Task> allTasks = getListAllTasks();
        for (Task task : allTasks) {
            if (task.getStartTime() != null)
                tasksWithStartTime.add(task);
        }
        return tasksWithStartTime;
    }
}
