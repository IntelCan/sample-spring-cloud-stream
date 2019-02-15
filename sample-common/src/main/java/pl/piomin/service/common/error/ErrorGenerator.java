package pl.piomin.service.common.error;

import java.util.Random;

public class ErrorGenerator {

    public ErrorGenerator(Random random) {
        this.random = random;
    }

    private Random random;

    public void sometimesGenerateErrorFor(Object o) {
        if (random.nextBoolean()) {
            throw new IllegalArgumentException("Ups... error occurred when sending " + o);
        }
    }
}
