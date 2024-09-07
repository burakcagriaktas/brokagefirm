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
    public CommandLineRunner saveInitialData(CustomerRepository customerRepository, AssetRepository assetRepository) {
        return args -> {
            // region Customers
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
            // endregion Customers

            // region Customer 1 assets
            assetRepository.save(new Asset(
                    1L,
                    "TRY",
                    43178.93f,
                    29178.88f,
                    customerRepository.findById(1L).get()
            ));
            assetRepository.save(new Asset(
                    2L,
                    "DOLLAR",
                    203178.93f,
                    129178.88f,
                    customerRepository.findById(1L).get()
            ));
            assetRepository.save(new Asset(
                    3L,
                    "BITCOIN",
                    100f,
                    50f,
                    customerRepository.findById(1L).get()
            ));
            // endregion Customer 1 assets

            // region Customer 2 assets
            assetRepository.save(new Asset(
                    4L,
                    "TRY",
                    1000000.93f,
                    78113.11f,
                    customerRepository.findById(2L).get()
            ));
            assetRepository.save(new Asset(
                    5L,
                    "DOLLAR",
                    203178.93f,
                    129178.88f,
                    customerRepository.findById(2L).get()
            ));
            // endregion Customer 2 assets

            // region Customer 3 assets
            assetRepository.save(new Asset(
                    6L,
                    "TRY",
                    91000.22f,
                    91000.22f,
                    customerRepository.findById(3L).get()
            ));
            assetRepository.save(new Asset(
                    7L,
                    "ETHEREUM",
                    2178.109f,
                    1000.00f,
                    customerRepository.findById(3L).get()
            ));
            assetRepository.save(new Asset(
                    8L,
                    "NVIDIA",
                    50f,
                    35f,
                    customerRepository.findById(3L).get()
            ));
            // endregion Customer 3 assets

            // region Customer 4 assets
            assetRepository.save(new Asset(
                    9L,
                    "DOLLAR",
                    4222178.109f,
                    3000000.99f,
                    customerRepository.findById(4L).get()
            ));
            assetRepository.save(new Asset(
                    10L,
                    "AKBNK",
                    1000f,
                    900f,
                    customerRepository.findById(4L).get()
            ));
            assetRepository.save(new Asset(
                    11L,
                    "EURO",
                    10000000f,
                    350000f,
                    customerRepository.findById(4L).get()
            ));
            assetRepository.save(new Asset(
                    12L,
                    "TRY",
                    100000000f,
                    75000000f,
                    customerRepository.findById(4L).get()
            ));
            // endregion Customer 4 assets
        };
    }
}
