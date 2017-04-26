package gov.uspto.openData.csService;

import gov.uspto.openData.csService.util.ServiceConfiguration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Import(ServiceConfiguration.class)
public class ServiceConfigurationTest {
	
	@Bean
	public DataSource dataSource(){
		 DriverManagerDataSource dataSource =  new DriverManagerDataSource("jdbc:mysql://localhost:3306/content_store?jdbcCompliantTruncation=false&amp;zeroDateTimeBehavior=convertToNull", 
				 "root", "");
		 dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		 return dataSource;
	}
	

}
