package net.lawyd.server;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@ComponentScan(SpringConfig.PACKAGE_PATH)
@EnableJpaRepositories(SpringConfig.PACKAGE_PATH)
@EnableTransactionManagement
public class SpringConfig {

    static final String PACKAGE_PATH = "net.lawyd.server";

    @Value("${db.showSql:false}")
    private Boolean showSql;

    @Value("${db.createDatabaseStructure:true}")
    private Boolean createDbStructure;

    @Value("${db.jdbc.url:jdbc:h2:mem:database}")
    private String dbUrl;

    @Value("${db.jdbc.driverClassName:org.h2.Driver}")
    private String dbDriverClassName;

    @Value("${db.jdbc.username:someUser}")
    private String dbUsername;

    @Value("${db.jdbc.password:somePassword}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setDriverClassName(dbDriverClassName);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(final DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(createHibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(PACKAGE_PATH);
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

    private HibernateJpaVendorAdapter createHibernateJpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(createDbStructure);
        jpaVendorAdapter.setShowSql(showSql);
        return jpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
