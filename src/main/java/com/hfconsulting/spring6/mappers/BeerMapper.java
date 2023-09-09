package com.hfconsulting.spring6.mappers;

import com.hfconsulting.spring6.entities.Beer;
import com.hfconsulting.spring6.model.BeerDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO dto);
    BeerDTO beerToBeerDto(Beer beer);
}
