package com.example.backend_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendServicesApplication.class, args);
        System.out.println("✅ Web Scraper Backend Started Successfully!");
    }
}
