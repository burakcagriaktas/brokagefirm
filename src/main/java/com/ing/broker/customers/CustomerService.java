package com.ing.broker.customers;

public interface CustomerService {
    Customer findOne(Long id);
    boolean isCustomerExist(Long id);
}
