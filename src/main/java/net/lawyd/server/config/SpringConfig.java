package net.lawyd.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(SpringConfig.PACKAGE_PATH)
@EnableJpaRepositories(SpringConfig.PACKAGE_PATH)
@EnableTransactionManagement
public class SpringConfig {

    public static final String PACKAGE_PATH = "net.lawyd.server";

}
