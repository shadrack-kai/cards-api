package com.logicea.cards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CardsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsApiApplication.class, args);
    }

}
