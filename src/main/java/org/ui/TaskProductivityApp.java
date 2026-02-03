package org.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskProductivityApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Task> tasks = new ArrayList<>();
    private static int nextTaskId = 1;
    private static final int WEEKLY_CAPACITY = 40;

    public static void main(String[] args) {
        showWelcomeScreen();
        mainMenuLoop();
    }

    /* ---------------- Welcome Screen ---------------- */

    private static void showWelcomeScreen() {
        System.out.println("----------------------------------------");
        System.out.println(" Task & Productivity Intelligence System ");
        System.out.println("----------------------------------------");
        System.out.println();
        System.out.println(" This application helps you manage tasks");
        System.out.println(" and understand your workload.");
        System.out.println();
        System.out.println(" Press ENTER to continue.");
        System.out.println("----------------------------------------");
        scanner.nextLine();
    }

    /* ---------------- Main Menu ---------------- */

    private static void mainMenuLoop() {
        while (true) {
            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println(" Main Menu");
            System.out.println("----------------------------------------");
            System.out.println();
            System.out.println(" [Menu Options]");
            System.out.println(" [1] Create Task");
            System.out.println(" [2] View Task List");
            System.out.println(" [3] Workload Summary");
            System.out.println(" [4] Exit");
            System.out.println();
            System.out.println("----------------------------------------");
            System.out.print(" Select an option (1–4): ");
            System.out.println();
            System.out.println("----------------------------------------");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createTaskFlow();
                    break;
                case "2":
                    showTaskList();
                    break;
                case "3":
                    showWorkloadSummary();
                    break;
                case "4":
                    System.out.println("\nGoodbye!");
                    return;
                default:
                    System.out.println("\nInvalid option. Please try again.");
            }
        }
    }

    /* ---------------- Create Task ---------------- */

    private static void createTaskFlow() {
        while (true) {
            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println(" Create New Task");
            System.out.println("----------------------------------------");
            System.out.println();

            System.out.print(" Task Title:\n ");
            String title = scanner.nextLine();

            System.out.print("\n Deadline (YYYY-MM-DD):\n ");
            String deadline = scanner.nextLine();

            System.out.print("\n Estimated Hours:\n ");
            int hours = Integer.parseInt(scanner.nextLine());

            Task task = new Task(nextTaskId++, title, deadline, hours);
            tasks.add(task);

            System.out.println();
            System.out.println(" [Enter] Save Task");
            System.out.println(" [C] Create Another Task");
            System.out.println(" [B] Back to Main Menu");
            System.out.println("----------------------------------------");

            String option = scanner.nextLine().trim().toUpperCase();

            if (option.equals("B")) {
                return;
            } else if (!option.equals("C")) {
                return;
            }
        }
    }

    /* ---------------- Task List ---------------- */

    private static void showTaskList() {
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println(" Task List");
        System.out.println("----------------------------------------");
        System.out.println();
        System.out.println(" ID | Title           | Deadline     | Est. Hours");
        System.out.println(" -------------------------------------------");

        for (Task task : tasks) {
            System.out.printf(" %-2d | %-15s | %-12s | %d%n",
                    task.getId(),
                    task.getTitle(),
                    task.getDeadline(),
                    task.getEstimatedHours());
        }

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println(" [Options]");
        System.out.println(" [R] Refresh List");
        System.out.println(" [B] Back to Main Menu");
        System.out.println("----------------------------------------");

        String option = scanner.nextLine().trim().toUpperCase();
        if (option.equals("R")) {
            showTaskList();
        }
    }

    /* ---------------- Workload Summary ---------------- */

    private static void showWorkloadSummary() {
        int totalHours = tasks.stream()
                .mapToInt(task -> task.getEstimatedHours())
                .sum();

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println(" Workload Summary");
        System.out.println("----------------------------------------");
        System.out.println();
        System.out.println(" Total Estimated Hours: " + totalHours);
        System.out.println(" Weekly Capacity: " + WEEKLY_CAPACITY);
        System.out.println();
        System.out.println("----------------------------------------");

        if (totalHours > WEEKLY_CAPACITY) {
            System.out.println();
            System.out.println(" Status: OVERLOADED");
            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println(" [System Feedback]");
            System.out.println(" You may be overcommitted.");
            System.out.println(" Consider reducing or rescheduling tasks.");
        } else {
            System.out.println();
            System.out.println(" Status: WITHIN CAPACITY");
            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println(" [System Feedback]");
            System.out.println(" Your workload appears manageable.");
        }

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println(" [Options]");
        System.out.println(" [B] Back to Main Menu");
        System.out.println("----------------------------------------");

        scanner.nextLine();
    }
}
