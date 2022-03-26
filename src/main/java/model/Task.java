package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static model.Status.NEW;

public class Task {
    //Название, кратко описывающее суть задачи
    protected String name;
    //Описание, в котором раскрываются детали.
    protected String specification;
    //Уникальный идентификационный номер задачи, по которому её можно будет найти.
    protected int id = 0; //значение 0 - дефолтное состояние индификатора, которое мы будем использовать при проверке
    //Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
    protected Status status;
    //продолжительность задачи, оценка того, сколько времени она займёт
    protected Duration duration;
    //дата, когда предполагается приступить к выполнению задачи
    protected LocalDateTime startTime;
    //формат дата и времени для работы
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");

    public void setDuration(long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if(duration == null)
            return null;
        if (startTime != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    public Duration getDuration() {
        return duration;
    }

    public void resetStartTimeAndDuration() {
        startTime = null;
        duration = null;
    }

    //Если существует описание задачи
    public Task(String name, String specification) {
        this.name = name;
        this.specification = specification;
        this.status = NEW;
    }

    //Если нет описания задачи
    public Task(String name) {
        this.name = name;
        this.specification = null;
        this.status = NEW;
    }

    //Конструктор для обновления задачи
    public Task(String name, String specification, Status status) {
        if (name != null)
            this.name = name;
        if (specification != null)
            this.specification = specification;
        this.status = status;
    }

    public void setName(String name) {
        if (name != null)
            this.name = name;
    }

    public void setSpecification(String specification) {
        if (specification != null)
            this.specification = specification;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getSpecification() {
        return specification;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) &&
                Objects.equals(specification, task.specification) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, specification, id, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", specification='" + specification + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
