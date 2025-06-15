package com.moba.Customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
