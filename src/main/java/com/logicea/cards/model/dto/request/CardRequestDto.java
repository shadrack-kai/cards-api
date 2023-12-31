package com.logicea.cards.model.dto.request;

import com.logicea.cards.enums.CardStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CardRequestDto {

    @NotBlank(message = "name is required")
    private String name;

    @Pattern(regexp = "^#[A-Za-z0-9-]{6}$", message = "color is invalid")
    @Size(min = 7, message = "color cannot be empty")
    private String color;

    private String description;

    private CardStatus status = CardStatus.TODO;

}
