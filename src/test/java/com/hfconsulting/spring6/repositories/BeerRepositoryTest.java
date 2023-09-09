package com.hfconsulting.spring6.repositories;

import com.hfconsulting.spring6.entities.Beer;
import com.hfconsulting.spring6.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                                       .beerName("My Beer")
                        .beerStyle(BeerStyle.LAGER)
                        .upc("12345678")
                        .price(new BigDecimal("11.99"))
                .build());
        beerRepository.flush();
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerNameTooLong() {
        final String longName = "1234567890223456789032345678904234567890512345678901";

        assertThrows(ConstraintViolationException.class, () -> {
                Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName(longName)
                .beerStyle(BeerStyle.LAGER)
                .upc("12345678")
                .price(new BigDecimal("11.99"))
                .build());
        beerRepository.flush();
        });


    }
}