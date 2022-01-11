package controller;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTasksManager();
    }
}
