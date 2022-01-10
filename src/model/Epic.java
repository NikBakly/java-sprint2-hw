package model;

import java.util.ArrayList;

public class Epic extends Task {
    //Хранение в списку всех подзадач эпика
    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    //Создания с описанием
    public Epic(String name, String specification) {
        super(name, specification);
    }

    //Создания без описанием
    public Epic(String name) {
        super(name);
    }

    //обновление
    public Epic(String name, String specification, Status status) {
        super(name, specification, status);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void checkStatusDoneSubtasks() {
        boolean cheakStatus = true;
        for (Subtask subtask : subtasks) {
            if (!subtask.getStatus().equals(Status.DONE)) {
                cheakStatus = false;
                break;
            }
        }
        if (cheakStatus) {
            status = Status.DONE;
            System.out.println("Эпик под id = " + id + " выполнен!");
        }
    }

    public void checkStatusInProgressSubtasks() {
        boolean cheakStatus = true;
        for (Subtask subtask : subtasks) {
            if (!subtask.getStatus().equals(Status.INPROGRESS)) {
                cheakStatus = false;
                break;
            }
        }
        if (cheakStatus) {
            System.out.println("Эпик под id = " + id + " в прогрессе!");
            status = Status.INPROGRESS;
        }
    }

}
