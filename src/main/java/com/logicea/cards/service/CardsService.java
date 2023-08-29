package com.logicea.cards.service;

import com.logicea.cards.model.dto.request.CardRequestDto;
import com.logicea.cards.model.dto.response.ApiResponseDto;
import com.logicea.cards.model.dto.response.CardDto;
import com.logicea.cards.model.dto.response.ResponseBodyDto;

import java.util.List;

public interface CardsService {

    ApiResponseDto<CardDto> addCard(CardRequestDto cardRequestDto);

    ApiResponseDto<ResponseBodyDto<List<CardDto>>> getCards(Long userId, int page, int size, String[] sort);

    ApiResponseDto<CardDto> getCard(Long userId, Long cardId);

    ApiResponseDto<CardDto> updateCard(CardRequestDto cardRequestDto, Long userId, Long cardId);
}
