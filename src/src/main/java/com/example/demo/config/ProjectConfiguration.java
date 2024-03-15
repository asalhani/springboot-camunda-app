package com.example.demo.config;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.engine.spring.SpringProcessEngineServicesConfiguration;
import org.camunda.bpm.engine.spring.SpringTransactionsProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.CamundaBpmActuatorConfiguration;
import org.camunda.bpm.spring.boot.starter.CamundaBpmConfiguration;
import org.camunda.bpm.spring.boot.starter.CamundaBpmPluginConfiguration;
import org.camunda.bpm.spring.boot.starter.event.ProcessApplicationEventPublisher;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.camunda.bpm.spring.boot.starter.property.ManagementProperties;
import org.camunda.bpm.spring.boot.starter.util.CamundaBpmVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@EnableConfigurationProperties({
        CamundaBpmProperties.class,
        ManagementProperties.class
})
@Import({
        CamundaBpmConfiguration.class,
        CamundaBpmActuatorConfiguration.class,
        CamundaBpmPluginConfiguration.class,
        SpringProcessEngineServicesConfiguration.class
})
@ConditionalOnProperty(prefix = CamundaBpmProperties.PREFIX, name = "enabled", matchIfMissing = true)
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
@Configuration
public class ProjectConfiguration {
    @Autowired
    protected DataSource dataSource;
    @Autowired
    protected ProcessEngineConfigurationImpl processEngineConfigurationImpl;
    @Autowired
    protected SpringTransactionsProcessEngineConfiguration springTransactionsProcessEngineConfiguration;
    @Autowired
    protected SpringProcessEngineConfiguration processEngineConfiguration;


    @Bean(name = "camundaBpmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.camunda")
    public DataSource camundaBpmDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("camundaBpmDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

//    @Bean
//    public DataSourceTransactionManager transactionManager(){
//        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
//        dataSourceTransactionManager.setDataSource(dataSource);
//        return dataSourceTransactionManager;
//    }


    @Bean
    public SpringProcessEngineConfiguration myProcessEngineConfiguration(){
        processEngineConfiguration.setTransactionManager(transactionManager(dataSource));

        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        processEngineConfiguration.setJobExecutorActivate(false);
        processEngineConfiguration.setHistory("full");
        processEngineConfiguration.setJavaSerializationFormatEnabled(true);
        return processEngineConfiguration;
    }


    @Bean
    public ProcessEngineFactoryBean myProcessEngine(){
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(myProcessEngineConfiguration());
        return processEngineFactoryBean;
    }


    @Bean
    @Primary
    public CommandExecutor commandExecutorTxRequired() {
        return myProcessEngineConfiguration().getCommandExecutorTxRequired();
    }

    @Bean
    public CommandExecutor commandExecutorTxRequiresNew() {
        return myProcessEngineConfiguration().getCommandExecutorTxRequiresNew();
    }

    @Bean
    public CommandExecutor commandExecutorSchemaOperations() {
        return myProcessEngineConfiguration().getCommandExecutorSchemaOperations();
    }


    @Bean
    public CamundaBpmVersion camundaBpmVersion() {
        return new CamundaBpmVersion();
    }

    @Bean
    public ProcessApplicationEventPublisher processApplicationEventPublisher(ApplicationEventPublisher publisher) {
        return new ProcessApplicationEventPublisher(publisher);
    }
}