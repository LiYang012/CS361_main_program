package org.taskmanager.ui;


import org.taskmanager.model.Task;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);

    public void waitForEnter() {
        scanner.nextLine();
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public void printTaskTable(List<Task> tasks) {
        System.out.println("\n ID | Title           | Deadline     | Est. Hours");
        System.out.println(" ----------------------------------------");
        for (Task t : tasks) {
            System.out.printf(" %d  | %-15s | %s | %.1f%n",
                    t.getId(),
                    t.getTitle(),
                    t.getDeadline(),
                    t.getEstimatedHours());
        }
        System.out.println();
    }
}

