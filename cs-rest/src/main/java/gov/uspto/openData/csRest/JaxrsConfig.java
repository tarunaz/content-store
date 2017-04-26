package gov.uspto.openData.csRest;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class JaxrsConfig extends ResourceConfig {
	
	public JaxrsConfig(){
		register(MultiPartFeature.class);
	}

}
