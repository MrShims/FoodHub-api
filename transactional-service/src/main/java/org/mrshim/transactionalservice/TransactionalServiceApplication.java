package org.mrshim.transactionalservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransactionalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionalServiceApplication.class,args);
    }
}
