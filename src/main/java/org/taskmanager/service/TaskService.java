package org.taskmanager.service;

import org.taskmanager.model.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    // user-configurable weekly workload capacity
    private double weeklyCapacity = 40.0;

    public TaskService() {
        seedMockData();
    }

    /* ---------------- Task CRUD ---------------- */

    public void addTask(String title,
                        LocalDate deadline,
                        double hours,
                        String taskCategory,
                        String timeCategory) {

        tasks.add(new Task(
                nextId++,
                title,
                deadline,
                hours,
                taskCategory,
                timeCategory
        ));
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

    /* ---------------- Workload Logic ---------------- */

    public double getTotalEstimatedHours() {
        return tasks.stream()
                .mapToDouble(Task::getEstimatedHours)
                .sum();
    }

    public int getTotalTaskCount() {
        return tasks.size();
    }

    public double getWeeklyCapacity() {
        return weeklyCapacity;
    }

    public void updateWeeklyCapacity(double newCapacity) {
        if (newCapacity > 0) {
            this.weeklyCapacity = newCapacity;
        }
    }

    /* ---------------- Weekly Tasks ---------------- */

    public List<Task> getTasksDueThisWeek() {

        LocalDate now = LocalDate.now();
        LocalDate endOfWeek = now.plusDays(7);

        return tasks.stream()
                .filter(t -> !t.getDeadline().isAfter(endOfWeek))
                .filter(t -> !t.getDeadline().isBefore(now))
                .collect(Collectors.toList());
    }

    /* ---------------- Category Statistics ---------------- */

    public long countFlexibleTasks() {
        return tasks.stream()
                .filter(t -> "Flexible".equalsIgnoreCase(t.getTimeCategory()))
                .count();
    }

    public long countRigidTasks() {
        return tasks.stream()
                .filter(t -> "Rigid".equalsIgnoreCase(t.getTimeCategory()))
                .count();
    }

    public long countWorkTasks() {
        return tasks.stream()
                .filter(t -> "Work".equalsIgnoreCase(t.getTaskCategory()))
                .count();
    }

    public long countPersonalTasks() {
        return tasks.stream()
                .filter(t -> "Personal".equalsIgnoreCase(t.getTaskCategory()))
                .count();
    }

    /* ---------------- Mock Data ---------------- */

    private void seedMockData() {

        addTask("Homework", LocalDate.of(2026, 2, 1), 4, "Work", "Flexible");
        addTask("Sprint Report", LocalDate.of(2026, 2, 10), 6, "Work", "Rigid");
        addTask("Group Project", LocalDate.of(2026, 2, 20), 10, "Work", "Flexible");
        addTask("Exam Prep", LocalDate.of(2026, 2, 28), 22, "Work", "Flexible");
    }
}