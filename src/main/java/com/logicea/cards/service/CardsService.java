package com.logicea.cards.service;

import com.logicea.cards.enums.CardStatus;
import com.logicea.cards.model.dto.request.CardRequestDto;
import com.logicea.cards.model.dto.response.ApiResponseDto;
import com.logicea.cards.model.dto.response.CardDto;
import com.logicea.cards.model.dto.response.ResponseBodyDto;

import java.util.List;
import java.util.Map;

public interface CardsService {

    ApiResponseDto<CardDto> addCard(Map<String, String> headers, CardRequestDto cardRequestDto);

    ApiResponseDto<ResponseBodyDto<List<CardDto>>> getCards(Map<String, String> headers,
                                                            Long userId, int page, int size, String[] sort);

    ApiResponseDto<CardDto> getCard(Map<String, String> headers, Long cardId);

    ApiResponseDto<CardDto> updateCard(Map<String, String> headers, CardRequestDto cardRequestDto,
                                       Long cardId);

    ApiResponseDto<Object> deleteCard(Map<String, String> headers, Long cardId);

    ApiResponseDto<ResponseBodyDto<List<CardDto>>> searchCards(Map<String, String> headers,
                                                               int page, int size, String[] sort,
                                                               String name, CardStatus status, String color,
                                                               String createdAt);
}
