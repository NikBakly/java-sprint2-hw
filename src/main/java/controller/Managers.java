package controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Managers {

    public TaskManager getDefault() {
        return new InMemoryTasksManager();
    }

    public TaskManager getHttpTaskManager(String uri)  {
        try {
            return new HTTPTaskManager(uri);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TaskManager getFileBackedTasksManager(String path) {
        return new FileBackedTasksManager(path);

    }
}
