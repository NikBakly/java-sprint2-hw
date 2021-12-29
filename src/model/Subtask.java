package model;

import java.util.Objects;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String specification) {
        super(name, specification);
    }

    public Subtask(String name) {
        super(name);
    }

    // Обновление подзадачи, когда надо обновить idEpic
    public Subtask(String name, String specification, Status status, Epic epic) {
        super(name, specification, status);
        this.epic = epic;
    }

    // Обновление подзадачи, когда не надообновить idEpic
    public Subtask(String name, String specification, Status status) {
        super(name, specification, status);

    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epic, subtask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epic);
    }
}
