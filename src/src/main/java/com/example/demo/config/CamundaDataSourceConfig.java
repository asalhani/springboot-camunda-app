//package com.example.demo.config;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
////@EnableJpaRepositories(
////        // basePackages = "your.camunda.repository.package",
////        entityManagerFactoryRef = "camundaEntityManagerFactory",
////        transactionManagerRef = "camundaTransactionManager"
////)
//public class CamundaDataSourceConfig {
//
//
//
//    @Bean
//    public PlatformTransactionManager transactionManager(@Qualifier("camundaBpmDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
////    @Bean(name = "camundaEntityManagerFactory")
////    public LocalContainerEntityManagerFactoryBean camundaEntityManagerFactory(
////            EntityManagerFactoryBuilder builder, DataSource dataSource) {
////        return builder.dataSource(dataSource)
////                .packages("your.camunda.entity.package")
////                .persistenceUnit("camunda")
////                .build();
////    }
//
//
//
////    @Bean(name = "camundaTransactionManager")
////    public PlatformTransactionManager camundaTransactionManager(EntityManagerFactory entityManagerFactory) {
////        return new JpaTransactionManager(entityManagerFactory);
////    }
//}
