package com.moba.Customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    //db
    private static List<Customer> customers;
    static  {
        customers = new ArrayList<>();

        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                "password", 21, Gender.MALE);
        customers.add(alex);

        Customer jamila = new Customer(
                2,
                "Jamila",
                "jamila@gmail.com",
                "password", 21, Gender.MALE);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream().filter(
                        c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c-> c.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        //customers.removeIf(c -> c.getId().equals(customerId));
        customers.stream().filter(c -> c.getId().equals(customerId))
                .findFirst().ifPresent(customers::remove); /* o -> customers.remove(o)*/
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return customers.stream().filter(
                        c -> c.getUsername().equals(email))
                .findFirst();
    }
}
