package com.app.mysns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class MySnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySnsApplication.class, args);

        System.out.println("started web sever !!!");
    }
}
