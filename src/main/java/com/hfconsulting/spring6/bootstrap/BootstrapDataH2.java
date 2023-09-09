package com.hfconsulting.spring6.bootstrap;

import com.hfconsulting.spring6.entities.Beer;
import com.hfconsulting.spring6.entities.Customer;
import com.hfconsulting.spring6.model.BeerCSVRecord;
import com.hfconsulting.spring6.model.BeerStyle;
import com.hfconsulting.spring6.repositories.BeerRepository;
import com.hfconsulting.spring6.repositories.CustomerRepository;
import com.hfconsulting.spring6.services.BeerCsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapDataH2 implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvService beerCsvService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.debug("H2 Bootstrap class running");
        populateBeers();
        loadCSVData();
        populateCustomers();
        log.debug("H2 Bootstrap class exiting");
    }

    private void loadCSVData() throws FileNotFoundException {
        if (beerRepository.count() < 10) {
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

            List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

            recs.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };
                beerRepository.save(Beer.builder()
                                .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                                .beerStyle(beerStyle)
                                .price(BigDecimal.TEN)
                                .upc(beerCSVRecord.getRow().toString())
                                .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });


        }
        log.debug("CSV data loaded");
    }

    private void populateBeers() {
        if (this.beerRepository.count() > 0) {
            return;
        }
        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        beerRepository.save(beer1);
        beerRepository.save(beer2);
        beerRepository.save(beer3);

    }

    private void populateCustomers() {
        if (customerRepository.count() > 0) {
            return;
        }
        Customer cust1 = Customer.builder()
                .name("Heart and Crown pub")
                .createdDate(LocalDateTime.now())
                .updateDate((LocalDateTime.now()))
                .build();
        Customer cust2 = Customer.builder()
                .name("Paddy Boland's")
                .createdDate(LocalDateTime.now())
                .updateDate((LocalDateTime.now()))
                .build();
        Customer cust3 = Customer.builder()
                .name("Clocktower Pub")
                .createdDate(LocalDateTime.now())
                .updateDate((LocalDateTime.now()))
                .build();
        Customer cust4 = Customer.builder()
                .name("Social")
                .createdDate(LocalDateTime.now())
                .updateDate((LocalDateTime.now()))
                .build();
        customerRepository.save(cust1);
        customerRepository.save(cust2);
        customerRepository.save(cust3);
        customerRepository.save(cust4);
    }
}
