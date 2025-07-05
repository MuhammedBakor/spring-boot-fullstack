package com.moba.auth;

import com.moba.Customer.CustomerDTO;

public record AuthenticationResponse(
        String token, CustomerDTO customerDTO
) {
}
