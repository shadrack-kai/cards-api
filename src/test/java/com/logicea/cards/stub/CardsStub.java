package com.logicea.cards.stub;

import com.logicea.cards.enums.UserRole;
import com.logicea.cards.model.dto.request.LoginRequestDto;
import com.logicea.cards.model.entity.UserEntity;

public class CardsStub {

    public static UserEntity getUserEntity(Long userId) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@test.com");
        userEntity.setPassword("$2a$10$1aALnut/l1sG53TQ3wRN.usZDzeyVybzuRowkM.CA60.vdleS7mYa");
        userEntity.setRole(UserRole.MEMBER);
        return userEntity;
    }

    public static LoginRequestDto getLoginRequestDto(String email, String pass) {
        final LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@test.com");
        loginRequestDto.setPassword("Test!123");
        return loginRequestDto;
    }

}
