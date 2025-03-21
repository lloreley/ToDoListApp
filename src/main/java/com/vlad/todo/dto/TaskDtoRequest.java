package com.vlad.todo.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class TaskDtoRequest {
    private String title;
    private String content;
    private Boolean isCompleted;
    private LocalDate deadlineDate;
    private Boolean isImportant;
    private Long userId;
}
