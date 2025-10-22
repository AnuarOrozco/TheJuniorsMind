package com.anuar.thejuniorsmind.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@EntityScan(basePackages = "com.anuar.thejuniorsmind.model")
@EnableJpaRepositories(basePackages = "com.anuar.thejuniorsmind.repository")
@ComponentScan(basePackages = "com.anuar.thejuniorsmind")
@ActiveProfiles("test")
public class TestConfig {
}
