package com.example.demo;

import com.example.demo.models.Student;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import java.util.Objects;

@Configuration()
@EnableTransactionManagement
// @formatter:off
@EnableJpaRepositories(basePackages = "com.example.demo.respositories",
        entityManagerFactoryRef = "domainEntityManagerFactory",
        transactionManagerRef = "domainTransactionManager")
// @formatter:on
public class DomainDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "domain.datasource")
    public DataSourceProperties domainDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "domain.datasource.configuration")
    public HikariDataSource domainDataSource() {
        return domainDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean
    @ConfigurationProperties("domain.jpa")
    public JpaProperties domainJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "domainEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean domainEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        // @formatter:off
        EntityManagerFactoryBuilder builder1 = createEntityManagerFactoryBuilder(domainJpaProperties());
        return builder.dataSource(domainDataSource()).packages(Student.class).persistenceUnit("domainEntityManager").build();

//        return builder
//                .dataSource(domainDataSource())
//                .packages("com.example.demo.models")
//                .persistenceUnit("domainEntityManager")
//                .build();
        // @formatter:on
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(jpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);
    }

    private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
        // ... map JPA properties as needed
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public PlatformTransactionManager domainTransactionManager(
            final @Qualifier("domainEntityManagerFactory") LocalContainerEntityManagerFactoryBean domainEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(domainEntityManagerFactory.getObject()));
    }
}