package com.moba.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existsCustomerById(Integer id);
    public void deleteCustomerById(Integer customerId);
    public void updateCustomer(Customer customer);
    Optional<Customer> selectUserByEmail(String email);
}
