package com.github.pablomathdev;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "mysql.datasource")
public class MysqlProperties {

	    private String mysqlUrl;
	    private String mysqlUser;
	    private String mysqlPassword;


	}

