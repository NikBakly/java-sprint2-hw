package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {
    protected final String path;
    public static final String START_LINE = "id,type,name,status,description,startTime,durationMinutes,epic\n";


    public FileBackedTasksManager(String path) {
        this.path = path;
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

    // ???????????????????? ????????
    public Epic getEpicById(int id) {
        //???????????????????? ?? ?????????????? ?????????????????????????? ??????????
        return epics.get(id);
    }

    //?????????????????? ?????????????? ?????????????????? ?????????????????? ?? ?????????????????? ????????
    protected void save() {
        try (Writer fileWriterStart = new FileWriter(path)) {
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

    //?????????????????? ???????? ?? ?????????????????? ???? id ??????????
    protected void addEpicInSubtaskById(Subtask subtask, int idEpic){
        subtask.setEpic(epics.get(idEpic));
    }

    //???????????????? ?? ?????????????????? ???????? ???? id ??????????, ?????????? ?????? ????????
    protected void addSubtasksInEpic(Epic epic){
        for (Subtask subtask : epic.getSubtasks()){
            addEpicInSubtaskById(subtask, epic.getId());
        }
    }

    //?????????????????? ???????????? ?? ????????????
    private String toString(Task task) {
        String typeTask = task.getClass().getSimpleName();
        String result;
        String startTime;
        long durationMinutes;

        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(Task.formatter);
        } else {
            startTime = "null";
        }

        if (task.getDuration() != null) {
            durationMinutes = task.getDuration().toMinutes();
        } else {
            durationMinutes = 0;
        }

        if (typeTask.equals("Task") || typeTask.equals("Epic")) {
            result = String.format("%d,%s,%s,%s,%s,%s,%d",
                    task.getId(),
                    typeTask,
                    task.getName(),
                    task.getStatus(),
                    task.getSpecification(),
                    startTime,
                    durationMinutes
            );
        } else {
            result = String.format("%d,%s,%s,%s,%s,%s,%d,%d",
                    task.getId(),
                    typeTask,
                    task.getName(),
                    task.getStatus(),
                    task.getSpecification(),
                    startTime,
                    durationMinutes,
                    ((Subtask) task).getEpic().getId()
            );
        }
        return result;
    }

    //???????????????? ???????????? ???? ????????????
    private Task fromString(String value) {
        String[] parameters = value.split(",");
        /*parameters[0] - id,
         parameters[1] - type,
         parameters[2] - name,
         parameters[3] - status,
         parameters[4] - Specification,
         parameters[5] - startTime,
         parameters[6] - duration,
         parameters[7] - epic */
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
                int idEpic = Integer.parseInt(parameters[7]);
                if (epics.containsKey(idEpic)) {
                    //?????????????????? ???????? ?? ??????????????????
                    ((Subtask) task).setEpic(getEpicById(idEpic));
                    //???????????????? ?? ?????????? ??????????????????
                    getEpicById(idEpic).getSubtasks().add((Subtask) task);
                }
            }
        }
        // ???????????????????????????? ?????????? ?? ??????????????????????????????????
        if (task != null) {
            if (!parameters[5].equals("null"))
                task.setStartTime(parameters[5]);
            if (!parameters[6].equals("0"))
                task.setDuration(Long.parseLong(parameters[6]));
        }
        return task;
    }

    //???????????????????? ?????????????????? ??????????????
    private static String toStringHistory(HistoryManager manager) {
        List<Task> tasksHistory = manager.getHistory();
        StringBuilder idHistory = new StringBuilder();
        idHistory.append("\n");
        for (int i = 0; i < tasksHistory.size(); i++) {
            //???????????????? ???? ?????????????????? ?????????????? ??????????????
            if (i == tasksHistory.size() - 1)
                idHistory.append(String.format("%d", tasksHistory.get(i).getId()));
            else
                idHistory.append(String.format("%d,", tasksHistory.get(i).getId()));
        }

        return idHistory.toString();
    }

    //???????????????????????????? ?????????????????? ?????????????? ???? CSV
    private static List<Integer> fromStringHistory(String value) {
        List<Integer> historyId = new ArrayList<>();
        String[] idHistoryTasks = value.split(",");
        for (String id : idHistoryTasks) {
            historyId.add(Integer.parseInt(id));
        }
        return historyId;
    }

    //???????????????????? ???????????????????? ???? ??????????
    public void downloadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isBlank()) {
                    if (!line.equals("id,type,name,status,description,startTime,durationMinutes,epic")) {
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
}
