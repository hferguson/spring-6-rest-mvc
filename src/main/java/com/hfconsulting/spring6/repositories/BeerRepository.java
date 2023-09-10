package com.hfconsulting.spring6.repositories;
import com.hfconsulting.spring6.entities.Beer;
import com.hfconsulting.spring6.model.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;
public interface BeerRepository extends JpaRepository<Beer, UUID> {

    List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);

    List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);
    List<Beer> findAllByBeerStyle(BeerStyle beerStyle);
}
