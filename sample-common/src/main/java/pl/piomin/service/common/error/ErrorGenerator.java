package pl.piomin.service.common.error;

import java.util.Random;

public class ErrorGenerator {

    private Random random = new Random();

    public void sometimesGenerateErrorFor(Object o) {
        if (random.nextBoolean()) {
            throw new IllegalArgumentException("Ups... error occurred when sending " + o);
        }
    }
}
