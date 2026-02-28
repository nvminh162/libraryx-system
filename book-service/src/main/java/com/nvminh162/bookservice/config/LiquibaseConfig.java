package com.nvminh162.bookservice.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

/**
 * Cấu hình Liquibase tường minh để chạy migration trước khi JPA/Hibernate khởi tạo.
 * Dùng khi auto-config Liquibase của Spring Boot 4 không kích hoạt.
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LiquibaseConfig {

    @Value("${spring.liquibase.change-log:classpath:db/changelog/changelog-master.xml}")
    private String changeLog;

    @Bean(name = "liquibase")
    @DependsOn("dataSource")
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(changeLog);
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
