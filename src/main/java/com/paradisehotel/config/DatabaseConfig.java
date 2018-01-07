package com.paradisehotel.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories("com.paradisehotel.repository")
@EnableTransactionManagement
public class DatabaseConfig {

}
