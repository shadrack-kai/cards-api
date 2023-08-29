package com.logicea.cards.controller;

import com.logicea.cards.model.dto.request.CardRequestDto;
import com.logicea.cards.model.dto.response.ApiResponseDto;
import com.logicea.cards.model.dto.response.CardDto;
import com.logicea.cards.model.dto.response.ResponseBodyDto;
import com.logicea.cards.service.CardsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CardsController {

    private final CardsService cardsService;

    @PostMapping("/1.0/cards")
    public ResponseEntity<ApiResponseDto<CardDto>> addCard(@RequestBody @Valid CardRequestDto cardRequestDto) {
        return ResponseEntity.ok(cardsService.addCard(cardRequestDto));
    }

    @GetMapping("/1.0/cards/{userId}")
    public ResponseEntity<ApiResponseDto<ResponseBodyDto<List<CardDto>>>> getCards(@PathVariable Long userId,
                                                                                   @RequestParam(defaultValue = "1") int page,
                                                                                   @RequestParam(defaultValue = "5") int size,
                                                                                   @RequestParam(defaultValue = "id,asc") String[] sort) {
        return ResponseEntity.ok(cardsService.getCards(userId, page, size, sort));
    }

    @GetMapping("/1.0/cards/{userId}/{cardId}")
    public ResponseEntity<ApiResponseDto<CardDto>> getCard(@PathVariable Long userId,
                                                                                  @PathVariable Long cardId) {
        return ResponseEntity.ok(cardsService.getCard(userId, cardId));
    }

    @PutMapping("/1.0/cards/{userId}/{cardId}")
    public ResponseEntity<ApiResponseDto<CardDto>> updateCard(@RequestBody @Valid CardRequestDto cardRequestDto,
                                                              @PathVariable Long userId,
                                                              @PathVariable Long cardId) {
        return ResponseEntity.ok(cardsService.updateCard(cardRequestDto, userId, cardId));
    }

}
