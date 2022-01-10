package controller;

public class Managers {

    public InMemoryTasksManager getDefault() {
        return new InMemoryTasksManager();
    }
}
