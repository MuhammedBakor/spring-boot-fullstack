package com.moba.Journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.moba.Customer.Customer;
import com.moba.Customer.CustomerRegistrationRequest;
import com.moba.Customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String customerUri = "/api/v1/customers";

    @Test
    void canRegisterCustomer() {

        //create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@moba.com";
        int age = faker.number().numberBetween(15, 99);

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(
                        name, email, age
                );

        //send a post request

        webTestClient.post().uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange().expectStatus().isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get().uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult().getResponseBody();

       Customer expectedCustomer = new Customer(
               name, email, age
       );

    //make sure that customer is present
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        //get customer by id
        int id = allCustomers
                .stream()
                .filter(customer -> customer.getEmail()
                        .equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        expectedCustomer.setId(id);

        webTestClient.get().uri(customerUri+ "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {

        //create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@moba.com";
        int age = faker.number().numberBetween(15, 99);

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(
                        name, email, age
                );

        //send a post request

        webTestClient.post().uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange().expectStatus().isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get().uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult().getResponseBody();

        //get customer by id
        int id = allCustomers
                .stream()
                .filter(customer -> customer.getEmail()
                        .equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();

        //delete customer
        webTestClient.delete().uri(customerUri + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk();

        webTestClient.get().uri(customerUri+ "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
        int age = faker.number().numberBetween(15, 99);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, age
        );

        // send a post request
        webTestClient.post()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer

        String newName = "Ali";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, null, null
        );

        webTestClient.put()
                .uri(customerUri + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(customerUri + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(
                id, newName, email, age
        );

        assertThat(updatedCustomer).isEqualTo(expected);
    }
}
