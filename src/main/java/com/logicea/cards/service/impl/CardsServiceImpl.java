package com.logicea.cards.service.impl;

import com.logicea.cards.exception.CardRequestException;
import com.logicea.cards.model.dto.request.CardRequestDto;
import com.logicea.cards.model.dto.response.ApiResponseDto;
import com.logicea.cards.model.dto.response.CardDto;
import com.logicea.cards.model.dto.response.ResponseBodyDto;
import com.logicea.cards.model.entity.CardEntity;
import com.logicea.cards.repository.UsersRepository;
import com.logicea.cards.service.CardsService;
import com.logicea.cards.repository.CardsRepository;
import com.logicea.cards.utils.CardsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CardsServiceImpl implements CardsService {

    private final CardsRepository cardsRepository;
    private final UsersRepository usersRepository;

    @Override
    public ApiResponseDto<CardDto> addCard(final CardRequestDto cardRequestDto) {
        return usersRepository.findById(cardRequestDto.getUserId())
                .map(userEntity -> {
                    final CardEntity cardEntity = CardsMapper.INSTANCE.toCardEntity(cardRequestDto, userEntity, null);
                    final CardEntity newCardEntity = cardsRepository.save(cardEntity);
                    final CardDto cardDto = CardsMapper.INSTANCE.toCardDto(newCardEntity);

                    return ApiResponseDto.<CardDto>builder()
                            .code("200")
                            .response("Success")
                            .body(cardDto)
                            .build();
                }).orElseThrow(() -> new CardRequestException(404, "Card could not be added. User details not found"));
    }

    @Override
    public ApiResponseDto<ResponseBodyDto<List<CardDto>>> getCards(Long userId, int page, int size, String[] sort) {
        final int currentPage = page >= 1 ? page - 1 : 0;
        final Page<CardEntity> pageCards = cardsRepository.findByUserId(userId, getPageable(sort, currentPage, size));
        final List<CardDto> cards = pageCards.getContent().stream()
                .map(CardsMapper.INSTANCE::toCardDto)
                .toList();

        if(cards.isEmpty()) {
            throw new CardRequestException(404, "No records found");
        }

        return ApiResponseDto.<ResponseBodyDto<List<CardDto>>>builder()
                .code("200")
                .response("Success")
                .body(ResponseBodyDto.<List<CardDto>>builder()
                        .page(page)
                        .size(size)
                        .totalCount((int) pageCards.getTotalElements())
                        .totalPages(pageCards.getTotalPages())
                        .content(cards)
                        .build())
                .build();
    }

    @Override
    public ApiResponseDto<CardDto> getCard(Long userId, Long cardId) {
        return cardsRepository.findByUserIdAndId(userId, cardId)
                .map(CardsMapper.INSTANCE::toCardDto)
                .map(cardDto -> ApiResponseDto.<CardDto>builder()
                        .code("200")
                        .response("Success")
                        .body(cardDto)
                        .build())
                .orElseThrow(() -> new CardRequestException(404, "Card could not be found"));
    }

    @Override
    public ApiResponseDto<CardDto> updateCard(CardRequestDto cardRequestDto, Long userId, Long cardId) {
        return cardsRepository.findByUserIdAndId(userId, cardId)
                .flatMap(existingCardEntity -> usersRepository.findById(userId))
                .map(userEntity -> {
                    final CardEntity cardEntity = CardsMapper.INSTANCE.toCardEntity(cardRequestDto, userEntity, cardId);
                    final CardEntity newCardEntity = cardsRepository.save(cardEntity);
                    final CardDto cardDto = CardsMapper.INSTANCE.toCardDto(newCardEntity);

                    return ApiResponseDto.<CardDto>builder()
                            .code("200")
                            .response("Success")
                            .body(cardDto)
                            .build();
                }).orElseThrow(() -> new CardRequestException(404, "Card could not be found"));
    }

    private Pageable getPageable(String[] sortBy, int page, int size) {
        final List<Sort.Order> orders = new ArrayList<>();
        if (sortBy[0].contains(",")) {
            for (String sortOrder : sortBy) {
                String[] sort = sortOrder.split(",");
                orders.add(new Sort.Order(sort[1].contains("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sort[0]));
            }
        } else {
            orders.add(new Sort.Order(Sort.Direction.ASC, sortBy[0]));
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }

}
