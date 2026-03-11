package org.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class Task {
    private final int id;
    private final String title;
    private final LocalDate deadline;
    private final double estimatedHours;

    // From Task Categorizer microservice
    private final String taskCategory;   // Work / Personal

    private final String timeCategory;   // Flexible / Rigid
}