package com.moba.Customer;

import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        Page<Customer> page = mock(Page.class);
        List<Customer> customers = List.of(new  Customer());
        when(page.getContent()).thenReturn(customers);
        when(customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        //when
        List<Customer> expected = underTest.selectAllCustomers();

        //then
        assertThat(expected).isEqualTo(customers);
        ArgumentCaptor<Pageable> pageArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(customerRepository).findAll(pageArgumentCaptor.capture());
        assertThat(pageArgumentCaptor.getValue()).isEqualTo(Pageable.ofSize(1000));
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