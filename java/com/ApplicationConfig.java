package com;

import com.controller.TaiwanIDResource;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/jerseyapi")
public class ApplicationConfig extends ResourceConfig {
	public ApplicationConfig() {
		packages("com.controller");
        //		register(TaiwanIDResource.class);

		// register(BankAccountResource.class);
        //  register(HashController.class);
	}
}
