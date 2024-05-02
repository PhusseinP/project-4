import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        deserializeTasks();
        int choice;
        do {
            choice = displayMenu();
            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    deleteTask();
                    break;
                case 3:
                    updateTask();
                    break;
                case 4:
                    listTasks();
                    break;
                case 5:
                    setPriority();
                    break;
                case 0:
                    serializeTasks();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }

    private static int displayMenu() {
        System.out.println("Please select a task:\n" +
                "1. Add a task\n" +
                "2. Remove a task\n" +
                "3. Update a task\n" +
                "4. List all tasks\n" +
                "5. Set task priority\n" +
                "0. Exit");
        return scanner.nextInt();
    }

    private static void addTask() {
        System.out.println("Enter task details:");
        scanner.nextLine(); // Consume newline
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Priority (0-5): ");
        int priority = scanner.nextInt();
        tasks.add(new Task(name, description, priority));
    }

    private static void deleteTask() {
        System.out.print("Enter index of task to remove: ");
        int index = scanner.nextInt();
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            System.out.println("Task removed successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void updateTask() {
        System.out.print("Enter index of task to update: ");
        int index = scanner.nextInt();
        if (index >= 0 && index < tasks.size()) {
            System.out.print("Enter new description: ");
            scanner.nextLine(); // Consume newline
            String description = scanner.nextLine();
            tasks.get(index).setDescription(description);
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        System.out.println("Index | Task Name | Description | Priority");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.printf("%d | %s | %s | %d\n", i, task.getName(), task.getDescription(), task.getPriority());
        }
    }

    private static void setPriority() {
        listTasks();
        System.out.print("Enter index of task to set priority: ");
        int index = scanner.nextInt();
        if (index >= 0 && index < tasks.size()) {
            System.out.print("Enter new priority (0-5): ");
            int priority = scanner.nextInt();
            tasks.get(index).setPriority(priority);
            System.out.println("Priority set successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void serializeTasks() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("tasks.json")) {
            gson.toJson(tasks, writer);
            System.out.println("Tasks serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deserializeTasks() {
        try (FileReader reader = new FileReader("tasks.json")) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(reader);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Task>>() {}.getType();
            tasks.addAll(gson.fromJson(jsonElement, type));
            System.out.println("Tasks deserialized successfully.");
        } catch (IOException e) {
            System.out.println("No tasks found.");
        }
    }
}
