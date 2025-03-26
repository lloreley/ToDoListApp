package com.vlad.todo.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserDtoRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
