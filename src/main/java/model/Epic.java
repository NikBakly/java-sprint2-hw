package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    //Хранение в списке всех подзадач эпика
    private final ArrayList<Subtask> subtasks = new ArrayList<>();

    //Создания с описанием
    public Epic(String name, String specification) {
        super(name, specification);
    }

    //Создания без описания
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public void setDuration(long minutes) {
        //Обработка продолжительности задачи,
        //Продолжительность эпика - сумма продолжительности всех его подзадач.
        if(!subtasks.isEmpty()){
            for (Subtask subtask : subtasks) {
                duration = duration.plus(subtask.getDuration());
            }
        }
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }
}
