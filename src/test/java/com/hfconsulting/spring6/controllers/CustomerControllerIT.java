package com.hfconsulting.spring6.controllers;

import com.hfconsulting.spring6.entities.Customer;
import com.hfconsulting.spring6.mappers.CustomerMapper;
import com.hfconsulting.spring6.model.CustomerDTO;
import com.hfconsulting.spring6.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Transactional
    @Rollback
    @Test
    void testDeleteExisting() {
        Customer cust = customerRepository.findAll().get(0);
        ResponseEntity responseEntity = customerController.deleteCustomer(cust.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(cust.getId())).isEmpty();
    }

    @Test
    void testDeleteNotExisting() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomer(UUID.randomUUID());
        });
    }
    @Transactional
    @Rollback
    @Test
    void testAddCustomer() {
        CustomerDTO newCustomer = CustomerDTO.builder().name("NEW BREWERY").build();
        ResponseEntity responseEntity = customerController.addCustomer(newCustomer);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
    }
    @Test
    void testListCustomers() {
        List<CustomerDTO> customers = customerController.listCustomers();
        assertThat(customers.size()).isEqualTo(4);
    }
    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> customers = customerController.listCustomers();
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerController.getCustomerById(customer.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void getCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
                customerController.getCustomerById(UUID.randomUUID());
        });
    }
    @Transactional
    @Rollback
    @Test
    void testUpdateExisting() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerMapper.customerToCustomerDto(customer);
        final String newName = "UPDATED";
        dto.setName(newName);
        dto.setId(null);
        dto.setVersion(null);
        ResponseEntity responseEntity = customerController.updateCustomer(customer.getId(), dto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Customer updated = customerRepository.findById(customer.getId()).get();
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(newName);

    }

    @Test
    void testUpdateNotExisting() {
        CustomerDTO dto = CustomerDTO.builder().name("NON-EXISTING").build();

        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomer(UUID.randomUUID(), dto);
        });
    }

    @Transactional
    @Rollback
    @Test
    void testPatchExisting() {
        final String newName = "PATCHED-NAME";
        Customer customer  = customerRepository.findAll().get(0);
        CustomerDTO dto = CustomerDTO.builder().name(newName).build();
        ResponseEntity responseEntity = customerController.patchCustomer(customer.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        CustomerDTO updated = customerController.getCustomerById(customer.getId());
        assertThat(updated.getName()).isEqualTo(newName);
    }


    @Test
    void testPatchNotExisting() {
        final String newName = "PATCHED-NAME";
        CustomerDTO dto = CustomerDTO.builder().name(newName).build();
        assertThrows(NotFoundException.class, () -> {
            customerController.patchCustomer(UUID.randomUUID(), dto);
        });
    }
}