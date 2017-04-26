package gov.uspto.openData.csService.util;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@ComponentScan(value={"gov.uspto.openData.csService", "gov.uspto.openData.csRest.resource"})
@EnableTransactionManagement
@PropertySource("classpath:app.properties")
public class ServiceConfiguration{
	
	@Inject
	private Environment env;
	
	@Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        // instantiate, configure and return PropertyPlaceholderConfigurer
		return new PropertyPlaceholderConfigurer();
    }
	
	@Bean
	public AmazonS3Client amazonS3Client(){
		AmazonS3Client s3Client = new AmazonS3Client(awsCredentials());
		return s3Client;
	}
	
	@Bean
	public AWSCredentials awsCredentials(){
		String accessKey = env.getProperty("aws_access_key");
		String secretKey = env.getProperty("aws_secret_key");
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		return credentials;
	}
	
	@Bean
	@Profile({"dev", "prod"})
	public JndiObjectFactoryBean dataSource(){
		JndiObjectFactoryBean jndiFactoryBean = new JndiObjectFactoryBean();
		jndiFactoryBean.setProxyInterface(DataSource.class);
		jndiFactoryBean.setJndiName("java:comp/env/"+env.getProperty("jndi.datasource_name"));
		return jndiFactoryBean;
	}
	
	@Bean
	@Autowired
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	@Bean
	@Autowired
	public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource){
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
}
