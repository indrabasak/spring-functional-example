package com.basaki.example.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * {@code Application} represents the entry point for spring 5 functional
 * example.
 * <p/>
 *
 * @author Indra Basak
 * @since 2/23/17
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.basaki.example.config",
        "com.basaki.example.controller",
        "com.basaki.example.data.entity",
        "com.basaki.example.data.repository",
        "com.basaki.example.error",
        "com.basaki.example.handler",
        "com.basaki.example.model",
        "com.basaki.example.service",
        "com.basaki.example.swagger"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
