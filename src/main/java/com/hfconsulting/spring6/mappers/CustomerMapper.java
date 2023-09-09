package com.hfconsulting.spring6.mappers;

import com.hfconsulting.spring6.entities.Customer;
import com.hfconsulting.spring6.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer );
}
