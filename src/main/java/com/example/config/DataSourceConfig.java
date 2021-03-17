package com.example.config;

import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.beetl.sql.core.*;
import org.beetl.sql.core.db.MySqlStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import cn.hutool.core.lang.Snowflake;

@Configuration
@PropertySource("classpath:beetl-sql.properties")
public class DataSourceConfig {

	@Bean
	public DataSource dataSource(@Value("${spring.datasource.url}") final String url,
			@Value("${spring.datasource.username}") final String username,
			@Value("${spring.datasource.password}") final String password) {
		final HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setJdbcUrl(url);
		hikariDataSource.setUsername(username);
		hikariDataSource.setPassword(password);
		return hikariDataSource;
	}

	@Bean("tx")
	public TransactionManager tx(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public SQLManager sqlManager(@Autowired DataSource dataSource, @Value("${worker.id}") long workerId,
			@Value("${data.center.id}") long dataCenterId) {
		ConnectionSource connectionSource = ConnectionSourceHelper.getSingle(dataSource);
		SQLManagerBuilder sqlManagerBuilder = new SQLManagerBuilder(connectionSource);
		sqlManagerBuilder.setNc(new UnderlinedNameConversion());
		sqlManagerBuilder.setDbStyle(new MySqlStyle());
		sqlManagerBuilder.setSqlLoader("sql", StandardCharsets.UTF_8.name());
		sqlManagerBuilder.addInterDebug();
		SQLManager sqlManager = sqlManagerBuilder.build();
		Snowflake snowflake = new Snowflake(workerId, dataCenterId);
		sqlManager.addIdAutoGen("snow", params -> snowflake.nextId());
		return sqlManager;
	}
}
