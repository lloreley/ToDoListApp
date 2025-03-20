package com.vlad.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoResponse extends UserDtoRequest {
    private long id;
    private List<TaskDtoResponse> tasks;
}
