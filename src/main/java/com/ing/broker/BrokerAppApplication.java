package com.ing.broker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class BrokerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrokerAppApplication.class, args);
    }
    @Bean
    public CommandLineRunner saveCustomers(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer(
                    1L,
                    "Warren",
                    "Buffet",
                    Collections.emptySet(),
                    Collections.emptySet()
            ));

            customerRepository.save(new Customer(
                    2L,
                    "George",
                    "Soros",
                    Collections.emptySet(),
                    Collections.emptySet()
            ));
            customerRepository.save(new Customer(
                    3L,
                    "Paul Tudor",
                    "Jones",
                    Collections.emptySet(),
                    Collections.emptySet()
            ));
            customerRepository.save(new Customer(
                    4L,
                    "Jesse",
                    "Livermore",
                    Collections.emptySet(),
                    Collections.emptySet()
            ));
        };
    }
}
