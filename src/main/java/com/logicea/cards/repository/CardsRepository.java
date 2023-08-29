package com.logicea.cards.repository;

import com.logicea.cards.model.entity.CardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardsRepository extends JpaRepository<CardEntity, Long> {

    Page<CardEntity> findByUserId(Long userId, Pageable pageable);
    Optional<CardEntity> findByUserIdAndId(Long userId, Long cardId);
    Page<CardEntity> findAll(Pageable pageable);

}
