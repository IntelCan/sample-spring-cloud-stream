package pl.piomin.services.payment;

import java.util.Random;

public class CustomRandom extends Random {

    @Override
    public boolean nextBoolean() {
        return true;
    }
}
