package org.taskmanager.service;


import org.taskmanager.model.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;
    private final double weeklyCapacity = 40.0;

    public TaskService() {
        seedMockData();
    }

    public void addTask(String title, LocalDate deadline, double hours) {
        tasks.add(new Task(nextId++, title, deadline, hours));
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> searchByKeyword(String keyword) {
        return tasks.stream()
                .filter(t -> t.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Task> filterDueThisWeek(LocalDate now) {
        return tasks.stream()
                .filter(t -> !t.getDeadline().isAfter(now.plusDays(7)))
                .collect(Collectors.toList());
    }

    public List<Task> filterDueThisMonth(LocalDate now) {
        return tasks.stream()
                .filter(t -> !t.getDeadline().isAfter(now.plusMonths(1)))
                .collect(Collectors.toList());
    }

    public List<Task> filterOverdue(LocalDate now) {
        return tasks.stream()
                .filter(t -> t.getDeadline().isBefore(now))
                .collect(Collectors.toList());
    }

    public double getTotalEstimatedHours() {
        return tasks.stream().mapToDouble(Task::getEstimatedHours).sum();
    }

    public int getTotalTaskCount() {
        return tasks.size();
    }

    public double getWeeklyCapacity() {
        return weeklyCapacity;
    }

    private void seedMockData() {
        addTask("Homework", LocalDate.of(2026, 2, 1), 4);
        addTask("Sprint Report", LocalDate.of(2026, 2, 10), 6);
        addTask("Group Project", LocalDate.of(2026, 2, 20), 10);
        addTask("Exam Prep", LocalDate.of(2026, 2, 28), 22);
    }
}
