package com.hfconsulting.spring6.repositories;
import com.hfconsulting.spring6.entities.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
