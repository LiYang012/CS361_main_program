package org.taskmanager.app;

import org.taskmanager.model.Task;
import org.taskmanager.service.TaskService;
import org.taskmanager.ui.AppState;
import org.taskmanager.ui.ConsoleUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskManagerApp {

    private static AppState state = AppState.WELCOME;

    public static void main(String[] args) {

        TaskService service = new TaskService();
        ConsoleUI ui = new ConsoleUI();

        while (state != AppState.EXIT) {

            switch (state) {

                case WELCOME -> {
                    System.out.println("----------------------------------------");
                    System.out.println(" Welcome to Task Manager\n");

                    System.out.println(" ████████╗ █████╗ ███████╗██╗  ██╗");
                    System.out.println("╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝");
                    System.out.println("   ██║   ███████║███████╗█████╔╝");
                    System.out.println("   ██║   ██╔══██║╚════██║██╔═██╗");
                    System.out.println("   ██║   ██║  ██║███████║██║  ██╗");
                    System.out.println("   ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝\n");

                    System.out.println("   ▸ PLAN TASKS   ▸ TRACK WORKLOAD");
                    System.out.println("\n----------------------------------------");
                    System.out.println(" Press ENTER to continue");
                    System.out.println("----------------------------------------");

                    ui.waitForEnter();
                    state = AppState.MAIN_MENU;
                }

                case MAIN_MENU -> {
                    System.out.println("----------------------------------------");
                    System.out.println(" Main Menu");
                    System.out.println("----------------------------------------");
                    System.out.println(" [1] Create Task");
                    System.out.println(" [2] View Task List");
                    System.out.println(" [3] Workload Summary");
                    System.out.println(" [4] Exit");
                    System.out.println("----------------------------------------");
                    System.out.print(" Select an option (1–4): ");

                    switch (ui.readLine()) {
                        case "1" -> state = AppState.CREATE_TASK;
                        case "2" -> state = AppState.TASK_LIST_HUB;
                        case "3" -> state = AppState.WORKLOAD_BASIC;
                        case "4" -> state = AppState.EXIT;
                        default -> System.out.println(" Invalid option.");
                    }
                }

                case TASK_LIST_HUB -> {
                    System.out.println("----------------------------------------");
                    System.out.println(" Task List");
                    System.out.println("----------------------------------------");
                    System.out.println(" [S] Search by keyword");
                    System.out.println(" [F] Filter by deadline");
                    System.out.println(" [V] View all tasks");
                    System.out.println(" [B] Back to Main Menu");

                    switch (ui.readLine().toUpperCase()) {
                        case "S" -> state = AppState.SEARCH_TASKS;
                        case "F" -> state = AppState.FILTER_TASKS;
                        case "V" -> state = AppState.VIEW_ALL_TASKS;
                        case "B" -> state = AppState.MAIN_MENU;
                    }
                }

                case SEARCH_TASKS -> {
                    System.out.println("----------------------------------------");
                    System.out.println(" Search Tasks");
                    System.out.println("----------------------------------------");
                    System.out.print(" Enter keyword:\n > ");
                    String keyword = ui.readLine();

                    List<Task> results = service.searchByKeyword(keyword);

                    if (results.isEmpty()) {
                        System.out.println("\n No matching tasks found.\n");
                    } else {
                        ui.printTaskTable(results);
                    }

                    System.out.println(" [S] Search again");
                    System.out.println(" [V] View all tasks");
                    System.out.println(" [B] Back to Task List");

                    switch (ui.readLine().toUpperCase()) {
                        case "S" -> state = AppState.SEARCH_TASKS;
                        case "V" -> state = AppState.VIEW_ALL_TASKS;
                        case "B" -> state = AppState.TASK_LIST_HUB;
                    }
                }

                case FILTER_TASKS -> {
                    System.out.println("----------------------------------------");
                    System.out.println(" Filter Tasks by Deadline");
                    System.out.println("----------------------------------------");
                    System.out.println(" [1] Due this week");
                    System.out.println(" [2] Due this month");
                    System.out.println(" [3] Overdue tasks");
                    System.out.print("\n > ");

                    LocalDate now = LocalDate.now();
                    List<Task> results = switch (ui.readLine()) {
                        case "1" -> service.filterDueThisWeek(now);
                        case "2" -> service.filterDueThisMonth(now);
                        case "3" -> service.filterOverdue(now);
                        default -> List.of();
                    };

                    if (results.isEmpty()) {
                        System.out.println("\n No matching tasks found.\n");
                    } else {
                        ui.printTaskTable(results);
                    }

                    System.out.println(" [F] Filter again");
                    System.out.println(" [V] View all tasks");
                    System.out.println(" [B] Back to Task List");

                    switch (ui.readLine().toUpperCase()) {
                        case "F" -> state = AppState.FILTER_TASKS;
                        case "V" -> state = AppState.VIEW_ALL_TASKS;
                        case "B" -> state = AppState.TASK_LIST_HUB;
                    }
                }

                case VIEW_ALL_TASKS -> {
                    System.out.println("----------------------------------------");
                    System.out.println(" All Tasks");
                    System.out.println("----------------------------------------");
                    ui.printTaskTable(service.getAllTasks());

                    System.out.println(" [B] Back to Task List");

                    if (ui.readLine().equalsIgnoreCase("B")) {
                        state = AppState.TASK_LIST_HUB;
                    }
                }

                case CREATE_TASK -> {
                    System.out.println("----------------------------------------");
                    System.out.println(" Create New Task");
                    System.out.println("----------------------------------------");

                    System.out.print(" Task Title:\n ");
                    String title = ui.readLine();

                    LocalDate deadline;
                    while (true) {
                        try {
                            System.out.print(" Deadline (YYYY-MM-DD):\n ");
                            deadline = LocalDate.parse(ui.readLine());
                            break;
                        } catch (DateTimeParseException e) {
                            System.out.println(" Invalid date format.");
                        }
                    }

                    double hours;
                    while (true) {
                        try {
                            System.out.print(" Estimated Hours:\n ");
                            hours = Double.parseDouble(ui.readLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println(" Invalid number.");
                        }
                    }

                    String taskCategory = "Personal";
                    String timeCategory = "Flexible";

                    try {
                        String urlStr =
                                "http://localhost:8001/categorize?title=" +
                                        URLEncoder.encode(title, "UTF-8");

                        URL url = new URL(urlStr);
                        HttpURLConnection conn =
                                (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("GET");

                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(conn.getInputStream()));

                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        reader.close();

                        String json = response.toString();

                        if (json.contains("Work")) {
                            taskCategory = "Work";
                        }

                        if (json.contains("Rigid")) {
                            timeCategory = "Rigid";
                        }

                    } catch (Exception ignored) {}

                    service.addTask(title, deadline, hours, taskCategory, timeCategory);

                    System.out.println("\n Task saved.");
                    System.out.println(" Press ENTER to return to menu.");
                    ui.waitForEnter();

                    state = AppState.MAIN_MENU;
                }

                case WORKLOAD_BASIC -> {

                    List<Task> weeklyTasks = service.getTasksDueThisWeek();

                    double weeklyHours = weeklyTasks.stream()
                            .mapToDouble(Task::getEstimatedHours)
                            .sum();

                    String status = "Not overloaded";

                    try {
                        StringBuilder tasksParam = new StringBuilder();

                        for (Task t : weeklyTasks) {
                            if (tasksParam.length() > 0) {
                                tasksParam.append(",");
                            }
                            tasksParam.append(t.getEstimatedHours());
                        }

                        String urlStr =
                                "http://localhost:8002/detect_overload?tasks=" + tasksParam;

                        URL url = new URL(urlStr);
                        HttpURLConnection conn =
                                (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("GET");

                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(conn.getInputStream()));

                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        reader.close();

                        String json = response.toString();

                        if (json.contains("Near overload")) {
                            status = "Near overload";
                        } else if (json.contains("Overloaded")) {
                            status = "Overloaded";
                        }

                    } catch (Exception ignored) {}

                    System.out.println("----------------------------------------");
                    System.out.println(" Workload Summary");
                    System.out.println("----------------------------------------");
                    System.out.println(" Tasks Due This Week: " + weeklyTasks.size());
                    System.out.println(" Hours This Week: " + weeklyHours);
                    System.out.println(" Weekly Capacity: " + service.getWeeklyCapacity());
                    System.out.println("\n Status: " + status);

                    try {
                        String body =
                                "{"
                                        + "\"status\":\"" + status + "\","
                                        + "\"total_workload\":" + weeklyHours + ","
                                        + "\"weekly_capacity\":" + service.getWeeklyCapacity() + ","
                                        + "\"flexible_tasks\":" + service.countFlexibleTasks() + ","
                                        + "\"rigid_tasks\":" + service.countRigidTasks() + ","
                                        + "\"work_tasks\":" + service.countWorkTasks() + ","
                                        + "\"personal_tasks\":" + service.countPersonalTasks()
                                        + "}";

                        URL url = new URL("http://localhost:8003/generate_recommendation");
                        HttpURLConnection conn =
                                (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setRequestProperty("Content-Type", "application/json");

                        try (OutputStream os = conn.getOutputStream()) {
                            os.write(body.getBytes());
                            os.flush();
                        }

                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(conn.getInputStream()));

                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        reader.close();

                        System.out.println("\n Recommendation:");
                        System.out.println(" " + response);

                    } catch (Exception ignored) {}

                    System.out.println("\n Press ENTER to return to Main Menu.");
                    ui.waitForEnter();
                    state = AppState.MAIN_MENU;
                }

                case EXIT -> {
                    System.out.println("Goodbye!");
                }
            }
        }
    }
}