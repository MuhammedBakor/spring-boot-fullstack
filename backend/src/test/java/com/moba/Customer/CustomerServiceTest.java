package com.moba.Customer;

import com.moba.exception.DuplicateResourceException;
import com.moba.exception.RequestValidationException;
import com.moba.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerDao customerDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao, customerDTOMapper, passwordEncoder);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        Mockito.verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given

        int id = 10;
        Customer customer = new Customer(
                id,
               "MAN",
                "man@email",
                "password", 20,
                Gender.MALE);

        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerDTO expected = customerDTOMapper.apply(customer);

        //When

        CustomerDTO actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willTrowWhenGetCustomerIsEmpty() {
        //Given

        int id = 10;

        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.empty());

        //Then
        assertThatThrownBy(()-> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {

        //Given

        String email = "man@gmail.com";
        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "man",
                email,
                "password", 25,
                Gender.MALE
        );

        String passwordHash = "gbxf12!@#2b";

        Mockito.when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);

        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer CaptoredCustomer = customerArgumentCaptor.getValue();

        assertThat(CaptoredCustomer.getId()).isNull();
        assertThat(CaptoredCustomer.getEmail()).isEqualTo(request.email());
        assertThat(CaptoredCustomer.getName()).isEqualTo(request.name());
        assertThat(CaptoredCustomer.getAge()).isEqualTo(request.age());
        assertThat(CaptoredCustomer.getPassword()).isEqualTo(passwordHash);

    }

    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {

        //Given
        String email = "man@gmail.com";
        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "man",
                email,
                "password", 25,
                Gender.MALE
        );
        //When
        assertThatThrownBy(()-> underTest.addCustomer(request))
        .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");

        //Then

        Mockito.verify(customerDao, Mockito.never()).insertCustomer(Mockito.any());

    }

    @Test
    void deleteCustomerById() {

        //Given
        int id = 10;
        Mockito.when(customerDao.existsCustomerById(id))
                .thenReturn(true);
        //When
        underTest.deleteCustomerById(id);

        //Then
        Mockito.verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowDeleteCustomerByIdNotExists() {

        //Given
        int id = 10;
        Mockito.when(customerDao.existsCustomerById(id))
                .thenReturn(false);
        //When
        assertThatThrownBy(()-> underTest.deleteCustomerById(id))
        .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        //Then
        Mockito.verify(customerDao, Mockito.never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {

        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "MAN",
                "man@email",
                "password", 20,
                Gender.MALE);

        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));


        String email = "newman@email";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Newman",
                email,
                25
        );

        Mockito.when(customerDao.existsPersonWithEmail(email))
                .thenReturn(false);

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer CaptoredCustomer = customerArgumentCaptor.getValue();

        assertThat(CaptoredCustomer.getId()).isEqualTo(id);
        assertThat(CaptoredCustomer.getEmail()).isEqualTo(request.email());
        assertThat(CaptoredCustomer.getName()).isEqualTo(request.name());
        assertThat(CaptoredCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void canUpdateCustomerName() {

        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "MAN",
                "man@email",
                "password", 20,
                Gender.MALE);

        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));


       // String email = "newman@email";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Newman",
                null,
                null
        );

       // Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer CaptoredCustomer = customerArgumentCaptor.getValue();

        assertThat(CaptoredCustomer.getId()).isEqualTo(id);

        assertThat(CaptoredCustomer.getName()).isEqualTo(request.name());

        assertThat(CaptoredCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(CaptoredCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateCustomerEmail() {

        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "MAN",
                "man@email",
                "password", 20,
                Gender.MALE);

        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));


         String email = "newman@email";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null,
                email,
                null
        );

         Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer CaptoredCustomer = customerArgumentCaptor.getValue();

        assertThat(CaptoredCustomer.getName()).isEqualTo(customer.getName());

        assertThat(CaptoredCustomer.getEmail()).isEqualTo(request.email());

        assertThat(CaptoredCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateCustomerAge() {

        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "MAN",
                "man@email",
                "password", 20,
                Gender.MALE);

        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));


        //String email = "newman@email";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null,
                null,
                27
        );

        //Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        //When
        underTest.updateCustomer(id, request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer CaptoredCustomer = customerArgumentCaptor.getValue();

        assertThat(CaptoredCustomer.getName()).isEqualTo(customer.getName());

        assertThat(CaptoredCustomer.getEmail()).isEqualTo(customer.getEmail());

        assertThat(CaptoredCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenUpdateCustomerWithEmailAlreadyTaken() {

        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "MAN",
                "man@email",
                "password", 20,
                Gender.MALE);

        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));


        String email = "newman@email";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null,
                email,
                null
        );

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");

        //Then
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }

    @Test
    void willThrowWhenUpdateCustomerHasNoChanges() {

        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "MAN",
                "man@email",
                "password", 20,
                Gender.MALE);

        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );

        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
        .isInstanceOf(RequestValidationException.class)
                .hasMessage("Customer cannot be updated, no data changes found.");

        //Then
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());
    }
}