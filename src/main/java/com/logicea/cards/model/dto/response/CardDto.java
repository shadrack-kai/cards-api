package com.logicea.cards.model.dto.response;

import com.logicea.cards.enums.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDto {

    private Long id;
    private String name;
    private String color;
    private String description;
    private CardStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
