package config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MongoDBContainerConfig {
    private static final String IMAGE_VERSION = "mongo:4.4.2";

    public static final org.testcontainers.containers.MongoDBContainer mongoDBContainer = new org.testcontainers.containers.MongoDBContainer(IMAGE_VERSION);


    static {
        mongoDBContainer.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            String connectionString = mongoDBContainer.getReplicaSetUrl();
            System.setProperty("spring.data.mongodb.uri", connectionString);
        }
    }
}
