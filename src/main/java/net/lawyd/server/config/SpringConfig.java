package net.lawyd.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("net.lawyd.server")
@EnableJpaRepositories("net.lawyd.server")
@EnableTransactionManagement
public class SpringConfig {

    // Nothing to do here

}
