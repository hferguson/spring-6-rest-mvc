package com.hfconsulting.spring6.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfconsulting.spring6.model.BeerDTO;
import com.hfconsulting.spring6.services.BeerService;
import com.hfconsulting.spring6.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
    //@Autowired
    // BeerController beerController;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @MockBean
    BeerService beerService;
    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void getBeerIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testCreateBeer() throws Exception {
        System.out.println("Beer list size BEFORE " + beerServiceImpl.listBeers(null, null, true).size());
        BeerDTO beer = beerServiceImpl.listBeers(null, null, true).get(0);
        beer.setVersion(null);
        beer.setId(null);
        // Whenever we do a mock call to save new beer, return  the beer in position 1 instead.
        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null, true).get(1));

        mockMvc.perform(post(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
        System.out.println("Beer list size AFTER " + beerServiceImpl.listBeers(null, null, true).size());
    }

    @Test
    void testCreateBeerNullName() throws Exception {
        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null, true).get(1));

        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testDeleteBeer() throws Exception  {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, true).get(0);
        given(beerService.deleteById(any())).willReturn(true);
        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteById(uuidArgCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgCaptor.getValue());
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, true).get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "A New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());
        verify(beerService).patchBeerById(uuidArgCaptor.capture(), beerArgCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgCaptor.getValue().getBeerName());

    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, true).get(0);

        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));
        mockMvc.perform(put(BeerController.BEER_PATH_ID,  beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());
        verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void testUpdateBeerNullName() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, true).get(0);
        UUID uid = beer.getId();
        beer.setBeerName(null);
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)));

    }
    @Test
    void testUpdateBeerNullUpc() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, true).get(0);
        UUID uid = beer.getId();
        beer.setUpc(null);
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(2)));

    }
    @Test
    void testUpdateBeerNullType() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, true).get(0);
        UUID uid = beer.getId();
        beer.setBeerStyle(null);
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));

    }
    @Test
    void testUpdateBeerNullPrice() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers(null, null, true).get(0);
        UUID uid = beer.getId();
        beer.setPrice(null);
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));

    }
    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers(null, null, true)).willReturn(beerServiceImpl.listBeers(null, null, true));

        mockMvc.perform(get(BeerController.BEER_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }
    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers(null, null, true).get(0);
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BeerController.BEER_PATH_ID,  testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }
}