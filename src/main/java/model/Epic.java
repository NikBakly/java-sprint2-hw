package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    //Хранение в списке всех подзадач эпика
    private final ArrayList<Subtask> subtasks = new ArrayList<>();
    //дата и время окончания эпика
    private LocalDateTime endTime;

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
        return Objects.equals(subtasks, epic.subtasks) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks, endTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return findLateEndTimeSubtasks();
    }

    //расчет старта и конца эпика и его продолжительсти
    public void updateStartTimeAndDuration() {
        //Обработка продолжительности задачи,
        //Продолжительность эпика - сумма продолжительности всех его подзадач.
        duration = null;
        startTime = null;
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                //Если у подзадачи есть продолжительность с ней можно работать
                if (subtask.getDuration() != null) {
                    if (duration == null) {
                        duration = subtask.getDuration();
                    } else {
                        duration = duration.plus(subtask.getDuration());
                    }
                }
            }

            //инициализация старта по времени для  эпика
            startTime = findEarlyStartTimeSubtasks();
        }
    }

    private LocalDateTime findEarlyStartTimeSubtasks() {
        LocalDateTime earlyStartTime = subtasks.get(0).getStartTime();
        if (subtasks.size() > 1) {
            for (int i = 1; i < subtasks.size(); i++) {
                if (earlyStartTime == null)
                    earlyStartTime = subtasks.get(i).getStartTime();
                else if (subtasks.get(i).getStartTime() != null
                        && subtasks.get(i).getStartTime().isBefore(earlyStartTime)
                ) {
                    earlyStartTime = subtasks.get(i).getStartTime();
                }
            }
        }
        return earlyStartTime;
    }

    private LocalDateTime findLateEndTimeSubtasks() {
        LocalDateTime lateEndTime = subtasks.get(0).getEndTime();
        if (subtasks.size() > 1) {
            for (int i = 1; i < subtasks.size(); i++) {
                if (lateEndTime == null)
                    lateEndTime = subtasks.get(i).getEndTime();
                else if (subtasks.get(i).getEndTime() != null
                        && subtasks.get(i).getEndTime().isAfter(lateEndTime)
                ) {
                    lateEndTime = subtasks.get(i).getEndTime();
                }
            }
        }
        return lateEndTime;
    }
}
