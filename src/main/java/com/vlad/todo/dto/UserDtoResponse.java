package com.vlad.todo.dto;

import com.vlad.todo.model.GroupEntity;
import java.util.List;
import lombok.Data;

@Data
public class UserDtoResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<TaskDtoResponse> tasks;
    private List<GroupEntity> groups;
}
