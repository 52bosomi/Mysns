package com.app.mysns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
// @ServletComponentScan(basePackages = "com.app.mysns.filter")
public class MySnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySnsApplication.class, args);

        System.out.println("started web sever !!!");
    }
}
