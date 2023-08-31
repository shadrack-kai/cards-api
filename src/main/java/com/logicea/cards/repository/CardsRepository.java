package com.logicea.cards.repository;

import com.logicea.cards.enums.CardStatus;
import com.logicea.cards.model.entity.CardEntity;
import com.logicea.cards.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

public interface CardsRepository extends JpaRepository<CardEntity, Long>, JpaSpecificationExecutor<CardEntity> {

    Page<CardEntity> findByUserId(Long userId, Pageable pageable);

    Page<CardEntity> findAll(Pageable pageable);

    static Specification<CardEntity> withUser(UserEntity user) {
        return (root, query, builder) -> builder.equal(root.get("user"), user);
    }

    static Specification<CardEntity> withName(String name) {
        return (root, query, builder) -> builder.equal(root.get("name"), name);
    }

    static Specification<CardEntity> hasColor(String color) {
        return (root, query, builder) -> builder.equal(root.get("color"), color);
    }

    static Specification<CardEntity> hasStatus(CardStatus status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    static Specification<CardEntity> withDate(LocalDateTime createdAt) {
        return (root, query, builder) -> builder.between(root.get("createdAt"), createdAt, createdAt.plusDays(1));
    }

}
