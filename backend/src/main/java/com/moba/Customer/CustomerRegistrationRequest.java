package com.moba.Customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age) {
}
