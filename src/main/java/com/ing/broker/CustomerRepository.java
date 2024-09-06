package com.ing.broker;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer save(@NonNull Customer customer);
    Optional<Customer> findById(Long id);
}
