package com.app.mysns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.web.servlet.ServletComponentScan;
// import org.springframework.context.annotation.Bean;
// import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
// @ServletComponentScan(basePackages = "com.app.mysns.filter")
@EnableCaching
public class MySnsApplication {

    // @Bean
    // public ServerEndpointExporter serverEndpointExporter() {
    //     return new ServerEndpointExporter();
    // }

    public static void main(String[] args) {
        SpringApplication.run(MySnsApplication.class, args);

        System.out.println("started web sever !!!");
    }
}
