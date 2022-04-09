package api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controller.FileBackedTasksManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HTTPTaskServer extends FileBackedTasksManager {
    private static final int PORT = 8080;
    private static final String PATH_FILE = "src/main/resources/history.csv";

    public HTTPTaskServer() {
        super(PATH_FILE);
    }

    public void startServer() {
        HttpServer httpServer = null;
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);

            //methods Tasks
            httpServer.createContext("/tasks/task", (httpExchange) -> {
                System.out.println("Началась обработка /tasks/task запроса от клиента.");
                String method = httpExchange.getRequestMethod();
                String response = null;
                String idInfo = null;
                Gson gson = new Gson();
                switch (method) {
                    case "GET" -> {
                        idInfo = httpExchange.getRequestURI().getRawQuery();
                        //if id exists
                        if (idInfo != null) {
                            int idTask = getIdValue(idInfo);
                            response = findTaskById(idTask).toString();
                        } else
                            response = getAllTasks().toString();
                        printResponse(response, httpExchange);
                    }
                    case "POST" -> {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Task newTask = gson.fromJson(jsonString, Task.class);
                        if (tasks.containsKey(newTask.getId())) {
                            updateTaskById(newTask.getId(), newTask);
                            System.out.println("Успешно обновлена задача id = " + newTask.getId());
                            response = "Успешно обновлена задача id = " + newTask.getId();
                        } else {
                            createNewTask(newTask);
                            System.out.println("Успешно добавлена задача id = " + newTask.getId());
                            response = "Успешно добавлена задача id = " + newTask.getId();
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                    case "DELETE" -> {
                        idInfo = httpExchange.getRequestURI().getRawQuery();
                        if (idInfo != null) {
                            int idTask = getIdValue(idInfo);
                            deleteTaskById(idTask);
                            response = String.format("Задача под id = %d успешно удалена", idTask);
                        } else {
                            deleteAllTasks();
                            response = "Задачи успешно удалены";
                        }
                        printResponse(response, httpExchange);
                    }
                }

            });
            //subtask methods
            httpServer.createContext("/tasks/subtask", (httpExchange) -> {
                System.out.println("Началась обработка /tasks/subtask запроса от клиента.");
                String method = httpExchange.getRequestMethod();
                String response = null;
                String idInfo = null;
                Gson gson = new Gson();
                switch (method) {
                    case "GET" -> {
                        idInfo = httpExchange.getRequestURI().getRawQuery();
                        //if id exists
                        if (idInfo != null) {
                            int idSubtask = getIdValue(idInfo);
                            response = findSubtaskById(idSubtask).toString();
                        }
                        printResponse(response, httpExchange);
                    }
                    case "POST" -> {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Subtask newSubtask = gson.fromJson(jsonString, Subtask.class);
                        if(subtasks.containsKey(newSubtask.getId())){
                            updateSubtaskById(newSubtask.getId(), newSubtask);
                            System.out.println("Успешно обновлена подзадача id = " + newSubtask.getId());
                            response = "Успешно обновлена подзадача id = " + newSubtask.getId();
                        } else {
                            createNewSubtask(newSubtask, newSubtask.getEpic());
                            System.out.println("Успешно добавлена подзадача id = " + newSubtask.getId());
                            response = "Успешно добавлена подзадача id = " + newSubtask.getId();
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                    case "DELETE" -> {
                        idInfo = httpExchange.getRequestURI().getRawQuery();
                        if (idInfo != null) {
                            int idSubtask = getIdValue(idInfo);
                            deleteSubtaskById(idSubtask);
                            response = String.format("Подзадача под id = %d успешно удалена", idSubtask);
                        } else {
                            deleteAllSubtasks();
                            response = "Подзадачи успешно удалены";
                        }
                        printResponse(response, httpExchange);
                    }
                }
            });

            //epic methods
            httpServer.createContext("/tasks/epic", (httpExchange) -> {
                System.out.println("Началась обработка /tasks/epic запроса от клиента.");
                String method = httpExchange.getRequestMethod();
                String response = null;
                String idInfo = null;
                Gson gson = new Gson();
                switch (method) {
                    case "GET" -> {
                        idInfo = httpExchange.getRequestURI().getRawQuery();
                        //if id exists
                        if (idInfo != null) {
                            int idEpic = getIdValue(idInfo);
                            response = findEpicById(idEpic).toString();
                        } else
                            response = getAllEpics().toString();
                        printResponse(response, httpExchange);
                    }
                    case "POST" -> {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Epic newEpic = gson.fromJson(jsonString, Epic.class);
                        if (epics.containsKey(newEpic.getId())){
                            updateEpicById(newEpic.getId(), newEpic);
                            System.out.println("Успешно обновлен эпик id = " + newEpic.getId());
                            response = "Успешно обновлен эпик id = " + newEpic.getId();
                        } else {
                            createNewTask(newEpic);
                            System.out.println("Успешно добавлен эпик id = " + newEpic.getId());
                            response = "Успешно добавлен эпик id = " + newEpic.getId();
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                    case "DELETE" -> {
                        idInfo = httpExchange.getRequestURI().getRawQuery();
                        if (idInfo != null) {
                            int idEpic = getIdValue(idInfo);
                            deleteEpicById(idEpic);
                            response = String.format("Задача под id = %d успешно удалена", idEpic);
                        } else {
                            deleteAllEpics();
                            response = "Задачи успешно удалены";
                        }
                        printResponse(response, httpExchange);
                    }
                }
            });

            //get EpicSubTasks(id)
            httpServer.createContext("/tasks/subtask/epic", (httpExchange) -> {
                System.out.println("Началась обработка /tasks/subtask/epic запроса от клиента.");
                String method = httpExchange.getRequestMethod();
                String response = null;
                String idInfo = null;
                if (method.equals("GET")) {
                    idInfo = httpExchange.getRequestURI().getRawQuery();
                    //if id exists
                    if (idInfo != null) {
                        int idEpic = getIdValue(idInfo);
                        //возращает подзадачи у эпика по id эпика
                        response = getAllSubtasks(idEpic).toString();
                    }
                    printResponse(response, httpExchange);
                }
            });

            //getHistory
            httpServer.createContext("/tasks/history", (httpExchange) -> {
                System.out.println("Началась обработка /tasks/history запроса от клиента.");
                String method = httpExchange.getRequestMethod();
                String response = null;
                if (method.equals("GET")) {
                    response = history().toString();
                } else {
                    response = String.format("Запрос %s не обрабатывается", method);
                }
                printResponse(response, httpExchange);
            });

            //getPrioritizedTasks
            httpServer.createContext("/tasks", (httpExchange) -> {
                System.out.println("Началась обработка /tasks запроса от клиента.");
                String method = httpExchange.getRequestMethod();
                String response = null;
                if (method.equals("GET")) {
                    response = getPrioritizedTasks().toString();
                } else {
                    response = String.format("Запрос %s не обрабатывается", method);
                }
                printResponse(response, httpExchange);
            });


            httpServer.start();

            System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getIdValue(String idInfo) {
        String[] idInfoSplit = idInfo.split("=");
        return Integer.parseInt(idInfoSplit[1]);
    }

    private void printResponse(String response, HttpExchange httpExchange) {
        try {
            if (response != null) {
                httpExchange.sendResponseHeaders(200, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                httpExchange.sendResponseHeaders(400, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
