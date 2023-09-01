package com.logicea.cards.service;

import com.logicea.cards.exception.CardRequestException;
import com.logicea.cards.model.dto.response.ApiResponseDto;
import com.logicea.cards.model.dto.response.CardDto;
import com.logicea.cards.model.entity.CardEntity;
import com.logicea.cards.repository.CardsRepository;
import com.logicea.cards.repository.UsersRepository;
import com.logicea.cards.service.impl.CardsServiceImpl;
import com.logicea.cards.utils.CardsMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CardsServiceTest {

    private CardsService cardsService;

    @MockBean
    private CardsRepository cardsRepository;

    @MockBean
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        cardsService = new CardsServiceImpl(cardsRepository, usersRepository);
    }

    @Test
    void getCard_shouldReturnSuccess_whenCardFound() {
        //given
        final Long cardId = 2L;
        CardEntity cardEntity = new CardEntity();
        cardEntity.setId(cardId);
        cardEntity.setUser(CardsMapper.INSTANCE.toUserEntity(10L));

        //when
        when(cardsRepository.findById(anyLong())).thenReturn(Optional.of(cardEntity));

        //then
        ApiResponseDto<CardDto> card = cardsService.getCard(Map.of("x-user-id", "10", "x-user-roles", "MEMBER"), cardId);
        Assertions.assertNotNull(card);
    }

    @Test
    void getCards_shouldThrowAnError_whenCardsNotFound() {
        //given
        final Long userId = 1L;

        //when
        when(cardsRepository.findByUserId(anyLong(), any())).thenReturn(Page.empty());

        //then
        assertThrows(CardRequestException.class, () -> {
                    cardsService.getCards(Map.of(), userId, 1, 2, new String[]{"id,asc"});
                });

    }

}
