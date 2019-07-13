package com.personio.hierarchymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication
@Configuration
public class HierarchyManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HierarchyManagerApplication.class, args);
	}

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.sqlite.JDBC");
		dataSource.setUrl("jdbc:sqlite:memory:myDb?cache=shared");
		return dataSource;
	}

}
