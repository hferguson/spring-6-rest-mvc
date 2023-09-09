package com.hfconsulting.spring6.services;

import com.hfconsulting.spring6.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);
    CustomerDTO addCustomer(CustomerDTO customer);

    Optional<CustomerDTO>  updateCustomerById(UUID customerId, CustomerDTO customer);

    Boolean deleteCustomerById(UUID id);

    Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer);
}
