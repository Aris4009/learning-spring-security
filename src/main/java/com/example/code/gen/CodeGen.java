package com.example.code.gen;

import java.util.ArrayList;
import java.util.List;

import org.beetl.core.GroupTemplate;
import org.beetl.core.ReThrowConsoleErrorHandler;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.sql.core.*;
import org.beetl.sql.gen.SourceBuilder;
import org.beetl.sql.gen.SourceConfig;
import org.beetl.sql.gen.simple.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariDataSource;

/**
 * 自动生成orm
 */
public class CodeGen {

	private static Logger log = LoggerFactory.getLogger(CodeGen.class);

	public static void main(String[] args) {
		try {
			SQLManager sqlManager = sqlManager();
			initGroupTemplate();
			genCode(sqlManager);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static SQLManager sqlManager() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/security?useSSL=false");
		dataSource.setUsername("root");
		ConnectionSource connectionSource = ConnectionSourceHelper.getSingle(dataSource);
		SQLManagerBuilder sqlManagerBuilder = new SQLManagerBuilder(connectionSource);
		sqlManagerBuilder.setNc(new UnderlinedNameConversion());
		return sqlManagerBuilder.build();
	}

	public static void initGroupTemplate() {
		GroupTemplate groupTemplate = BaseTemplateSourceBuilder.getGroupTemplate();
		String root = System.getProperty("user.dir");
		String templatePath = root + "/src/main/resources/templates/";
		FileResourceLoader resourceLoader = new FileResourceLoader(templatePath);
		groupTemplate.setResourceLoader(resourceLoader);
	}

	public static void genCode(SQLManager sqlManager) {
		List<SourceBuilder> sourceBuilder = new ArrayList<>();
		SourceBuilder entityBuilder = new EntitySourceBuilder();
		SourceBuilder mapperBuilder = new MapperSourceBuilder();
		SourceBuilder mdBuilder = new MDSourceBuilder();

		sourceBuilder.add(entityBuilder);
		sourceBuilder.add(mapperBuilder);
		sourceBuilder.add(mdBuilder);

		SourceConfig config = new SourceConfig(sqlManager, sourceBuilder);
		// 如果有错误，抛出异常而不是继续运行1
		EntitySourceBuilder.getGroupTemplate().setErrorHandler(new ReThrowConsoleErrorHandler());

		CustomProject project = new CustomProject("com.example");
		String tableName = "role_permission";
		config.gen(tableName, project);
	}

	static class CustomProject extends SimpleMavenProject {

		public CustomProject() {
			super();
		}

		public CustomProject(String basePackage) {
			super(basePackage);
		}
	}
}
