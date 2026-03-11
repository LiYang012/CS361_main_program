package org.taskmanager.ui;

import org.taskmanager.model.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

        System.out.println("\n ID | Title           | Deadline     | Hours | Category | Type     | Days Left");
        System.out.println(" -------------------------------------------------------------------------------");

        for (Task t : tasks) {

            int daysRemaining = getDaysRemaining(t.getDeadline().toString());

            System.out.printf(
                    " %d  | %-15s | %s | %-5.1f | %-8s | %-8s | %d%n",
                    t.getId(),
                    t.getTitle(),
                    t.getDeadline(),
                    t.getEstimatedHours(),
                    t.getTaskCategory(),
                    t.getTimeCategory(),
                    daysRemaining
            );
        }

        System.out.println();
    }

    /* -------- Deadline Checker Microservice Call -------- */

    private int getDaysRemaining(String date) {

        try {

            String urlStr =
                    "http://127.0.0.1:8000/deadline_checker?event_date=" + date;

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

            String value = json.replaceAll("[^0-9-]", "");

            return Integer.parseInt(value);

        } catch (Exception e) {

            // If the microservice fails, return 0 so table still prints
            return 0;
        }
    }
}