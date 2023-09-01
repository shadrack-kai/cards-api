package com.logicea.cards.stub;

import com.logicea.cards.enums.CardStatus;
import com.logicea.cards.enums.UserRole;
import com.logicea.cards.model.dto.request.LoginRequestDto;
import com.logicea.cards.model.entity.CardEntity;
import com.logicea.cards.model.entity.UserEntity;
import com.logicea.cards.utils.CardsMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardsStub {

    public static UserEntity getUserEntity(Long userId) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("$2a$10$1aALnut/l1sG53TQ3wRN.usZDzeyVybzuRowkM.CA60.vdleS7mYa");
        userEntity.setRole(UserRole.MEMBER);
        return userEntity;
    }

    public static UserEntity getUserEntity(UserRole role) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(10L);
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("$2a$10$1aALnut/l1sG53TQ3wRN.usZDzeyVybzuRowkM.CA60.vdleS7mYa");
        userEntity.setRole(role);
        return userEntity;
    }

    public static LoginRequestDto getLoginRequestDto(String email, String pass) {
        final LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail(email);
        loginRequestDto.setPassword(pass);
        return loginRequestDto;
    }

    public static List<CardEntity> getCards() {
        final List<CardEntity> cards = new ArrayList<>();
        for(int k=1; k <= 2; k++) {
            cards.add(getCardEntity(k, 10, CardStatus.TODO));
        }
        for(int k=3; k <= 5; k++) {
            cards.add(getCardEntity(k, 10, CardStatus.IN_PROGRESS));
        }
        return cards;
    }

    public static CardEntity getCardEntity(long id, long userId, CardStatus status) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setId(id);
        cardEntity.setName("Card "+id);
        cardEntity.setColor("#000000");
        cardEntity.setStatus(status);
        cardEntity.setCreatedAt(LocalDateTime.now().minusDays(3));
        cardEntity.setUser(CardsMapper.INSTANCE.toUserEntity(userId));
        return cardEntity;
    }

}
