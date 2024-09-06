package com.ing.broker;

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
        // TODO throw customer not found rte
        return customerOpt.orElse(null);
    }

    @Override
    public boolean isCustomerExist(Long id) {
        return findOne(id) != null;
    }
}
