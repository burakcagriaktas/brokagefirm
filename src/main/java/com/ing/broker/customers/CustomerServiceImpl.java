package com.ing.broker.customers;

import com.ing.broker.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findOne(Long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        return customerOpt.orElseThrow(() -> new NotFoundException(String.format(
                "Customer: %s is not found!", id
        )));
    }
}
