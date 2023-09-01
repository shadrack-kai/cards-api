package com.logicea.cards.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotNull(message = "email is required")
    private String email;

    @NotNull(message = "password is required")
    private String password;

}
