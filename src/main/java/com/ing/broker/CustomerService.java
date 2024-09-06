package com.ing.broker;

public interface CustomerService {
    Customer findOne(Long id);
    boolean isCustomerExist(Long id);
}
