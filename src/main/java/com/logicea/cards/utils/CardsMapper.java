package com.logicea.cards.utils;

import com.logicea.cards.model.dto.request.CardRequestDto;
import com.logicea.cards.model.dto.response.CardDto;
import com.logicea.cards.model.entity.CardEntity;
import com.logicea.cards.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardsMapper {

    CardsMapper INSTANCE = Mappers.getMapper(CardsMapper.class);


    @Mapping(target = "id", source = "cardId")
    @Mapping(target = "user", source = "userEntity")
    CardEntity toCardEntity(CardRequestDto cardRequestDto, UserEntity userEntity, Long cardId);
    CardDto toCardDto(CardEntity cardEntity);

}
