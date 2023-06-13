package com.github.pablomathdev.utils;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;


@Component
public class ExecuteSQL {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ResourceLoader resourceLoader;

	public void run(String path) {

		Resource resource = resourceLoader.getResource("classpath:sql/" + path);

		try {
			ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), resource);
		} catch (ScriptException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
