package com.logicea.cards.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardStatus {

    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private String name;

}
