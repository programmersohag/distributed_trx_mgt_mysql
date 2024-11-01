package com.example.distributed_trx_mgt.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "orderEntityManager",
        transactionManagerRef = "transactionManager",
        basePackages = {"com.example.distributed_trx_mgt.order"}
)
public class OrderDataSourceConfiguration {

    @Value("${order.db.url}")
    String dbUrl;

    @Value("${order.db.username}")
    String username;

    @Value("${order.db.password}")
    String passport;

    public Properties jpaProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.boot.allow_jdbc_metadata_access", "false");
        jpaProperties.put("hibernate.current_session_context_class", "jta");
        jpaProperties.put("jakarta.persistence.transactionType", "JTA");
        return jpaProperties;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public AtomikosDataSourceBean orderDataSource() {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(dbUrl);
        mysqlXaDataSource.setUser(username);
        mysqlXaDataSource.setPassword(passport);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
//        xaDataSource.setLocalTransactionMode(false);
        xaDataSource.setUniqueResourceName("order");
        xaDataSource.setPoolSize(10); // optional
        xaDataSource.setMaxPoolSize(30);
        xaDataSource.setBorrowConnectionTimeout(10); // optional
//        xaDataSource.setXaDataSourceClassName("com.microsoft.sqlserver.jdbc.SQLServerXADataSource");
        return xaDataSource;
    }

    @Bean
    public EntityManagerFactory orderEntityManager(DataSource orderDataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.example.distributed_trx_mgt.order");
        factory.setDataSource(orderDataSource);
        factory.setJpaProperties(jpaProperties());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

}
