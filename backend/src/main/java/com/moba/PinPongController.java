package com.moba;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PinPongController {

    private static int COUNTER = 0;
    record PinPong(String result) {}

    @GetMapping("/ping")
    public PinPong getPinPong() {
        return new PinPong("Pong %s".formatted(++COUNTER));
    }
}
