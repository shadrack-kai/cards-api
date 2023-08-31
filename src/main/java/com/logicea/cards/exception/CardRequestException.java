package com.logicea.cards.exception;

import lombok.Getter;

@Getter
public class CardRequestException extends RuntimeException{

    private int code;

    public CardRequestException(int code, String message) {
        super(message);
        this.code = code;
    }

}
