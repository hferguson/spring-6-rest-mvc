package com.hfconsulting.spring6.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hfconsulting.spring6.entities.Customer;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
