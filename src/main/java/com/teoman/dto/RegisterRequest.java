package com.teoman.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    private String username;

    @NotBlank(message = "boş olamaz")
    @Size(min = 6, message = "en az 6 karakter olmalıdır.")
    private String password;

    @Email
    @NotBlank
    private String email;

    private String firstName;

    private String lastName;

}
