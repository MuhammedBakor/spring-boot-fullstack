package com.moba.Customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        int id = 0;
        underTest.selectCustomerById(id);
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {

        //Given
        Customer customer = new Customer(
                "foo",
                "foo@email",
                "password", 20,
                Gender.MALE);

        //When
        underTest.insertCustomer(customer);

        //Then
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {

        //Given
        String email = "s@gmail.com";

        //When
        underTest.existsPersonWithEmail(email);

        //Then
        Mockito.verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsCustomerById() {
//Given
        int id = 1;

        //When
        underTest.existsCustomerById(id);

        //Then
        Mockito.verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {

        //Given
        int id = 1;
        //When
        underTest.deleteCustomerById(id);
        //Then
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {

        //Given
        Customer customer = new Customer(
                "mo",
                "mo@email",
                "password", 6,
                Gender.MALE);

        //When
        underTest.updateCustomer(customer);

        //Then
        Mockito.verify(customerRepository).save(customer);
    }
}