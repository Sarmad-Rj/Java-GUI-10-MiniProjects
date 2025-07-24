package P4_To_Do_List;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private static final String FILE_NAME = "tasks.json";
    private Gson gson = new Gson();

    public TaskManager() {
        loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        saveTasks();
    }

    public void markTaskDone(Task task) {
        task.markDone();
        saveTasks();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void saveTasks() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasks() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            Type taskListType = new TypeToken<ArrayList<Task>>() {}.getType();
            tasks = gson.fromJson(reader, taskListType);
            if (tasks == null) {
                tasks = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            // No tasks file exists yet; start fresh
            tasks = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
