package org.karane;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.karane.config.AppConfig;

import java.util.LinkedHashMap;
import java.util.Map;

@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    @ConfigProperty(name = "app.greeting")
    String greeting;

    @ConfigProperty(name = "app.max-items", defaultValue = "10")
    int maxItems;

    @ConfigProperty(name = "quarkus.profile", defaultValue = "prod")
    String activeProfile;

    @Inject
    AppConfig appConfig;

    @GET
    @Path("/properties")
    public Map<String, Object> properties() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("greeting", greeting);
        result.put("maxItems", maxItems);
        result.put("featureFlag", appConfig.featureFlag());
        result.put("activeProfile", activeProfile);
        return result;
    }

    @GET
    @Path("/mapping")
    public Map<String, Object> mapping() {
        Map<String, Object> db = new LinkedHashMap<>();
        db.put("host", appConfig.database().host());
        db.put("port", appConfig.database().port());
        db.put("name", appConfig.database().name());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("greeting", appConfig.greeting());
        result.put("maxItems", appConfig.maxItems());
        result.put("featureFlag", appConfig.featureFlag());
        result.put("database", db);
        return result;
    }
}
