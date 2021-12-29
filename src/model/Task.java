package model;

import java.util.Objects;

import static model.Status.NEW;

public class Task {
    //Название, кратко описывающее суть задачи
    protected String name;
    //Описание, в котором раскрываются детали.
    protected String specification;
    //Уникальный идентификационный номер задачи, по которому её можно будет найти.
    protected int id = 0; //значение 0 - дефолтное состояние индификатора,которое мы будем использовать при проверке
    //Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
    protected Status status;

    //Если сушествует описание задачи
    public Task(String name, String specification) {
        this.name = name;
        this.specification = specification;
        this.status = NEW;
    }

    //Если нету описания задачи
    public Task(String name) {
        this.name = name;
        this.specification = null;
        this.status = NEW;
    }

    //Конструктор для обновление задачи
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
