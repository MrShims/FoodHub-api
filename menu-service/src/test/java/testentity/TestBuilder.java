package testentity;

import java.util.Random;

public interface TestBuilder<T> {
    Random random = new Random();

    T build();
}
