package com.hfconsulting.spring6.bootstrap;

import com.hfconsulting.spring6.repositories.BeerRepository;
import com.hfconsulting.spring6.repositories.CustomerRepository;
import com.hfconsulting.spring6.services.BeerCsvService;
import com.hfconsulting.spring6.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataH2Test {
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCsvService csvService;

    BootstrapDataH2 bootstrapDataH2;
    @BeforeEach
    void setUp() {
        //csvService = new BeerCsvServiceImpl();
        bootstrapDataH2 = new BootstrapDataH2(beerRepository, customerRepository, csvService);
    }
    @Test
    void testRun() throws Exception {
        bootstrapDataH2.run(null);
        assertThat(beerRepository.count()).isGreaterThan(0);
        assertThat(customerRepository.count()).isEqualTo(4);
    }
}