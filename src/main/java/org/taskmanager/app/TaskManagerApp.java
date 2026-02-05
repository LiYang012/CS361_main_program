package org.taskmanager.app;


import org.taskmanager.model.Task;
import org.taskmanager.service.TaskService;
import org.taskmanager.ui.AppState;
import org.taskmanager.ui.ConsoleUI;

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

                    System.out.println(" ▸ PLAN TASKS   ▸ TRACK WORKLOAD");
                    System.out.println("\n----------------------------------------");
                    System.out.println(" This app helps you manage tasks and");
                    System.out.println(" understand your workload.\n");
                    System.out.println(" How it works:");
                    System.out.println("  Step 1: Create tasks with deadlines");
                    System.out.println("  Step 2: View your task list");
                    System.out.println("  Step 3: Review workload summary\n");
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
                    System.out.println(" How would you like to find your tasks?\n");
                    System.out.println(" [S] Search by keyword");
                    System.out.println(" [F] Filter by deadline");
                    System.out.println(" [V] View all tasks");
                    System.out.println(" [B] Back to Main Menu");
                    System.out.println("----------------------------------------");

                    switch (ui.readLine().toUpperCase()) {
                        case "S" -> state = AppState.SEARCH_TASKS;
                        case "F" -> state = AppState.FILTER_TASKS;
                        case "V" -> state = AppState.VIEW_ALL_TASKS;
                        case "B" -> state = AppState.MAIN_MENU;
                        default -> System.out.println(" Invalid option.");
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
                        System.out.println(" [S] Try another keyword");
                        System.out.println(" [V] View all tasks");
                        System.out.println(" [F] Filter by deadline");
                        System.out.println(" [B] Back to Task List");

                        switch (ui.readLine().toUpperCase()) {
                            case "S" -> state = AppState.SEARCH_TASKS;
                            case "V" -> state = AppState.VIEW_ALL_TASKS;
                            case "F" -> state = AppState.FILTER_TASKS;
                            case "B" -> state = AppState.TASK_LIST_HUB;
                        }
                    } else {
                        ui.printTaskTable(results);
                        System.out.println(" [S] Search again");
                        System.out.println(" [V] View all tasks");
                        System.out.println(" [B] Back to Task List");

                        switch (ui.readLine().toUpperCase()) {
                            case "S" -> state = AppState.SEARCH_TASKS;
                            case "V" -> state = AppState.VIEW_ALL_TASKS;
                            case "B" -> state = AppState.TASK_LIST_HUB;
                        }
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

                    System.out.println(" [S] Search by keyword");
                    System.out.println(" [F] Filter by deadline");
                    System.out.println(" [B] Back to Task List");

                    switch (ui.readLine().toUpperCase()) {
                        case "S" -> state = AppState.SEARCH_TASKS;
                        case "F" -> state = AppState.FILTER_TASKS;
                        case "B" -> state = AppState.TASK_LIST_HUB;
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

                    service.addTask(title, deadline, hours);

                    System.out.println("\n [Enter] Save Task");
                    System.out.println(" [C] Create Another Task");
                    System.out.println(" [B] Back to Main Menu");

                    switch (ui.readLine().toUpperCase()) {
                        case "C" -> state = AppState.CREATE_TASK;
                        case "B" -> state = AppState.MAIN_MENU;
                        default -> state = AppState.MAIN_MENU;
                    }
                }

                case WORKLOAD_BASIC -> {
                    double total = service.getTotalEstimatedHours();

                    System.out.println("----------------------------------------");
                    System.out.println(" Workload Summary (Basic)");
                    System.out.println("----------------------------------------");
                    System.out.println(" Total Tasks: " + service.getTotalTaskCount());
                    System.out.println(" Total Estimated Hours: " + total);
                    System.out.println(" Weekly Capacity: " + service.getWeeklyCapacity());

                    if (total > service.getWeeklyCapacity()) {
                        System.out.println("\n Status: OVERLOADED ⚠️");
                        System.out.println(" You may be overcommitted.");
                        System.out.println(" Consider reducing or rescheduling tasks.");
                    } else {
                        System.out.println("\n Status: OK");
                    }

                    System.out.println("\n [D] View Detailed Breakdown");
                    System.out.println(" [B] Back to Main Menu");

                    switch (ui.readLine().toUpperCase()) {
                        case "D" -> state = AppState.WORKLOAD_DETAILED;
                        case "B" -> state = AppState.MAIN_MENU;
                    }
                }

                case WORKLOAD_DETAILED -> {
                    double total = service.getTotalEstimatedHours();

                    System.out.println("----------------------------------------");
                    System.out.println(" Workload Summary (Detailed)");
                    System.out.println("----------------------------------------");
                    System.out.println(" Weekly Capacity: " + service.getWeeklyCapacity() + " hrs");
                    System.out.println(" Total Estimated Hours: " + total + " hrs\n");

                    for (Task t : service.getAllTasks()) {
                        System.out.printf(" - %-20s %.1f hrs%n",
                                t.getTitle(), t.getEstimatedHours());
                    }

                    System.out.println("\n Insight:");
                    if (total > service.getWeeklyCapacity()) {
                        System.out.println(" You are overloaded by " +
                                (total - service.getWeeklyCapacity()) + " hours.");
                    } else {
                        System.out.println(" You are within your weekly capacity.");
                    }

                    System.out.println("\n [H] Hide Details (Back to Summary)");
                    System.out.println(" [B] Back to Main Menu");

                    switch (ui.readLine().toUpperCase()) {
                        case "H" -> state = AppState.WORKLOAD_BASIC;
                        case "B" -> state = AppState.MAIN_MENU;
                    }
                }
            }
        }

        System.out.println("Goodbye!");
    }
}

