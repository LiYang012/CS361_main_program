package org.ui;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Task {
    private int id;
    private String title;
    private String deadline;
    private int estimatedHours;
}
