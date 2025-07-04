package com.moba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PooController {

    private static int COUNTER = 0;
    record PinPong(String result) {}

    @GetMapping("/poo")
    public PinPong getPinPong() {
        return new PinPong("Pong %s".formatted(++COUNTER));
    }
}
