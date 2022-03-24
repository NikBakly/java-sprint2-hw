package controller;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTasksManager();
    }

    public TaskManager getFileBackedTasksManager(String path){
        return new FileBackedTasksManager(path);
    }
}
