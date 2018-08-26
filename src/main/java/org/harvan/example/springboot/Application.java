/*
 * Copyright 2018-2018 the original author or authors.
 */

package org.harvan.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Harvan Irsyadi
 * @version 1.0.0
 * @since 1.0.0 (6 May 2018)
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(new Class<?>[]{Application.class}, args);
    }
}