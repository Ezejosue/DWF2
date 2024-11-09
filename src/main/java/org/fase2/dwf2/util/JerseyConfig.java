package org.fase2.dwf2.util;

import org.fase2.dwf2.controller.AccountController;
import org.fase2.dwf2.controller.UserController;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springdoc.webmvc.api.OpenApiResource;


@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Registrar los recursos que manejarán las rutas
        register(UserController.class);
        register(AccountController.class);


        // Agregar un proveedor de Jackson para la serialización JSON
        register(JacksonFeature.class);
        register(JacksonJaxbJsonProvider.class);
        register(OpenApiResource.class);
        register(OpenApiConfig.class);

    }

}
