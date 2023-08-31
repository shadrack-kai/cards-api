package com.logicea.cards.service.impl;

import com.logicea.cards.enums.CardStatus;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.logicea.cards.repository.CardsRepository.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.data.jpa.domain.Specification.where;

@RequiredArgsConstructor
@Service
public class CardsServiceImpl implements CardsService {

    private final CardsRepository cardsRepository;
    private final UsersRepository usersRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private static  final String X_USER_ROLES = "x-user-roles";
    private static  final String X_USER_ID = "x-user-id";
    private static  final String ADMIN = "ADMIN";
    private static  final String SUCCESS = "Success";
    private static  final String SC_SUCCESS = "200";

    @Override
    public ApiResponseDto<CardDto> addCard(final Map<String, String> headers, final CardRequestDto cardRequestDto) {
        return usersRepository.findById(Long.valueOf(headers.get(X_USER_ID)))
                .map(userEntity -> {
                    final CardEntity cardEntity = CardsMapper.INSTANCE.toCardEntity(cardRequestDto, userEntity, null);
                    final CardEntity newCardEntity = cardsRepository.save(cardEntity);
                    final CardDto cardDto = CardsMapper.INSTANCE.toCardDto(newCardEntity);

                    return ApiResponseDto.<CardDto>builder()
                            .code(SC_SUCCESS)
                            .response(SUCCESS)
                            .body(cardDto)
                            .build();
                }).orElseThrow(() -> new CardRequestException(404, "Card could not be added. User details not found"));
    }

    @Override
    public ApiResponseDto<ResponseBodyDto<List<CardDto>>> getCards(final Map<String, String> headers, final Long userId,
                                                                   final int page, final int size,
                                                                   final String[] sort) {
        final int currentPage = page >= 1 ? page - 1 : 0;
        final Page<CardEntity> pagedCards = nonNull(userId) ? cardsRepository.findByUserId(userId, getPageable(sort, currentPage, size)) :
                cardsRepository.findAll(getPageable(sort, page, size));
        final List<CardDto> cards = pagedCards.getContent().stream()
                .map(CardsMapper.INSTANCE::toCardDto)
                .toList();

        if (cards.isEmpty()) {
            throw new CardRequestException(404, "No records found");
        }

        return ApiResponseDto.<ResponseBodyDto<List<CardDto>>>builder()
                .code(SC_SUCCESS)
                .response(SUCCESS)
                .body(ResponseBodyDto.<List<CardDto>>builder()
                        .page(page)
                        .size(size)
                        .totalCount((int) pagedCards.getTotalElements())
                        .totalPages(pagedCards.getTotalPages())
                        .content(cards)
                        .build())
                .build();
    }

    @Override
    public ApiResponseDto<CardDto> getCard(final Map<String, String> headers, final Long cardId) {
        return cardsRepository.findById(cardId)
                .filter(cardEntity -> headers.get(X_USER_ROLES).contains(ADMIN) ? Boolean.TRUE :
                        cardEntity.getUser().getId().toString().equals(headers.get(X_USER_ID)))
                .map(CardsMapper.INSTANCE::toCardDto)
                .map(cardDto -> ApiResponseDto.<CardDto>builder()
                        .code(SC_SUCCESS)
                        .response(SUCCESS)
                        .body(cardDto)
                        .build())
                .orElseThrow(() -> new CardRequestException(404, "Card could not be found"));
    }

    @Override
    public ApiResponseDto<CardDto> updateCard(final Map<String, String> headers, final CardRequestDto cardRequestDto,
                                              final Long cardId) {
        return cardsRepository.findById(cardId)
                .filter(cardEntity -> headers.get(X_USER_ROLES).contains(ADMIN) ? Boolean.TRUE :
                        cardEntity.getUser().getId().toString().equals(headers.get(X_USER_ID)))
                .map(cardEntity -> {
                    cardEntity.setName(isNull(cardRequestDto.getName()) ? cardEntity.getName() : cardRequestDto.getName());
                    cardEntity.setColor(cardRequestDto.getColor());
                    cardEntity.setDescription(cardRequestDto.getDescription());
                    cardEntity.setStatus(cardRequestDto.getStatus());
                    final CardEntity updatedCardEntity = cardsRepository.save(cardEntity);
                    final CardDto cardDto = CardsMapper.INSTANCE.toCardDto(updatedCardEntity);

                    return ApiResponseDto.<CardDto>builder()
                            .code(SC_SUCCESS)
                            .response(SUCCESS)
                            .body(cardDto)
                            .build();
                }).orElseThrow(() -> new CardRequestException(404, "Update failed, card could not be found"));
    }

    @Override
    public ApiResponseDto<Object> deleteCard(final Map<String, String> headers, final Long cardId) {
        return cardsRepository.findById(cardId)
                .filter(cardEntity -> headers.get(X_USER_ROLES).contains(ADMIN) ? Boolean.TRUE :
                        cardEntity.getUser().getId().toString().equals(headers.get(X_USER_ID)))
                .map(cardEntity -> {
                    cardsRepository.delete(cardEntity);
                    return ApiResponseDto.builder()
                            .code(SC_SUCCESS)
                            .response(SUCCESS)
                            .build();
                }).orElseThrow(() -> new CardRequestException(404, "Delete failed, card could not be found"));
    }

    @Override
    public ApiResponseDto<ResponseBodyDto<List<CardDto>>> searchCards(final Map<String, String> headers, final int page, final int size,
                                                                      final String[] sort, final String name,
                                                                      final CardStatus status, final String color, final String createdAt) {
        final int currentPage = page >= 1 ? page - 1 : 0;
        final Page<CardEntity> pagedCards = cardsRepository.findAll(where(headers.get(X_USER_ROLES)
                .contains(ADMIN) ? null : withUser(CardsMapper.INSTANCE.toUserEntity(Long.parseLong(headers.get(X_USER_ID)))))
                .and(name == null ? null : withName(name))
                .and(color == null ? null : hasColor(color))
                .and(status == null ? null : hasStatus(status))
                .and(getDate(createdAt) == null ? null : withDate(getDate(createdAt))), getPageable(sort, currentPage, size));

        final List<CardDto> cards = pagedCards.getContent().stream()
                .map(CardsMapper.INSTANCE::toCardDto)
                .toList();

        if (cards.isEmpty()) {
            throw new CardRequestException(404, "No records found");
        }

        return ApiResponseDto.<ResponseBodyDto<List<CardDto>>>builder()
                .code(SC_SUCCESS)
                .response(SUCCESS)
                .body(ResponseBodyDto.<List<CardDto>>builder()
                        .page(page)
                        .size(size)
                        .totalCount((int) pagedCards.getTotalElements())
                        .totalPages(pagedCards.getTotalPages())
                        .content(cards)
                        .build())
                .build();
    }

    private LocalDateTime getDate(String date) {
        try {
            return isNull(date) ? null : LocalDate.parse(date, formatter).atStartOfDay();
        } catch (DateTimeParseException ex) {
            return null;
        }
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
