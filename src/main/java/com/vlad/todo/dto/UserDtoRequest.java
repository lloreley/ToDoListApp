package com.vlad.todo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDtoRequest {
    @Size(max = 50, message = "Длина имени слишком большая")
    @NotBlank(message = "Имя не должно быть пустым")
    private String firstName;
    @Size(max = 50, message = "Длина фамилии слишком большая")
    @NotBlank(message = "Фамилия не должна быть пустой")
    private String lastName;

    @Size(max = 50, message = "Длина электронной почты слишком большая")
    @NotBlank(message = "Электронная почта не должна быть пустой")
    @Email(message = "Электронная почта задана неверно")
    private String email;
    @Size(min = 3, max = 15, message = "Длина номера должна быть от 3 до 15")
    @NotBlank(message = "Номер не должен быть пустой")
    private String phone;
}
