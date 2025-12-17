package com.android.be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.android.be.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // SQLite configuration
}
