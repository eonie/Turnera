package org.turnera.server.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"org.turnera.**.repository.mybatis"})
public class MyBatisConfguration {
}
