package com.github.pablomathdev;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
	public class DataSourceConfig {

	    @Bean
	    DataSource dataSource(MysqlProperties mysqlProperties) {
	        
	    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    	dataSource.setUrl(mysqlProperties.getMysqlUrl());
	    	dataSource.setUsername(mysqlProperties.getMysqlUser());
	    	dataSource.setPassword(mysqlProperties.getMysqlPassword());
	        return dataSource;
	    }

	}

