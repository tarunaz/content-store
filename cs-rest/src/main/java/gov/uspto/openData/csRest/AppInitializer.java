package gov.uspto.openData.csRest;

import gov.uspto.openData.csService.util.ServiceConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.catalina.filters.CorsFilter;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Order(1)
public class AppInitializer implements WebApplicationInitializer {
	
	public void onStartup(ServletContext container) throws ServletException {
		
		AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
		springContext.register(ServiceConfiguration.class);
		
		// Manage the lifecycle of the root application context
		ContextLoaderListener contextLoaderListener = new ContextLoaderListener(springContext); 
		container.addListener(contextLoaderListener);
		container.addListener("org.springframework.web.context.request.RequestContextListener");
		
		ServletRegistration.Dynamic registration = container.addServlet("jersey-servlet", ServletContainer.class);
		registration.addMapping("/rest/*");
		registration.setInitParameter("jersey.config.server.provider.packages", "gov.uspto.openData.csRest.resource,gov.uspto.openData.csRest.mapper,org.codehaus.jackson.jaxrs");
		registration.setInitParameter("javax.ws.rs.Application", "gov.uspto.openData.csRest.JaxrsConfig");
		
//		container.setInitParameter("spring.profiles.active", "dev");
		container.setInitParameter("spring.profiles.default", "dev");
		
		// this is required to prevent 'SpringWebApplicationInitializer' from kicking in 
		container.setInitParameter("contextConfigLocation", "");
		
		container.addFilter("CorsFilter", CorsFilter.class).addMappingForUrlPatterns(null, false, "/*");;
	}
	
	
	
	

}
