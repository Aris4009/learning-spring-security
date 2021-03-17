package com.example.config;

import javax.sql.DataSource;

import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.core.loader.MarkdownClasspathLoader;
import org.beetl.sql.ext.DebugInterceptor;
import org.beetl.sql.ext.spring.BeetlSqlScannerConfigurer;
import org.beetl.sql.ext.spring.SpringConnectionSource;
import org.beetl.sql.ext.spring.SqlManagerFactoryBean;
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
	public SqlManagerFactoryBean sqlManagerFactoryBean(@Autowired DataSource dataSource) {
		SqlManagerFactoryBean sqlManagerFactoryBean = new SqlManagerFactoryBean();
		sqlManagerFactoryBean.setDbStyle(new MySqlStyle());
		sqlManagerFactoryBean.setSqlLoader(new MarkdownClasspathLoader("sql"));
		sqlManagerFactoryBean.setNc(new UnderlinedNameConversion());
		sqlManagerFactoryBean.setInterceptors(new Interceptor[]{new DebugInterceptor()});
		SpringConnectionSource springConnectionSource = new SpringConnectionSource();
		springConnectionSource.setMasterSource(dataSource);
		sqlManagerFactoryBean.setCs(springConnectionSource);
		return sqlManagerFactoryBean;
	}

	@Bean
	public SQLManager sqlManager(@Autowired SqlManagerFactoryBean sqlManagerFactoryBean,
			@Value("${worker.id}") long workerId, @Value("${data.center.id}") long dataCenterId) {
		SQLManager sqlManager = sqlManagerFactoryBean.getSqlManager();
		Snowflake snowflake = new Snowflake(workerId, dataCenterId);
		sqlManager.addIdAutoGen("snow", params -> snowflake.nextId());
		return sqlManager;
	}

	@Bean
	public BeetlSqlScannerConfigurer beetlSqlScannerConfigurer() {
		BeetlSqlScannerConfigurer beetlSqlScannerConfigurer = new BeetlSqlScannerConfigurer();
		beetlSqlScannerConfigurer.setDaoSuffix("Dao");
		beetlSqlScannerConfigurer.setBasePackage("com.example.mapper");
		beetlSqlScannerConfigurer.setSqlManagerFactoryBeanName("sqlManagerFactoryBean");
		return beetlSqlScannerConfigurer;
	}
}
