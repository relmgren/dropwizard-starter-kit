package thotornot.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import thotornot.application.ThotOrNotService;
import thotornot.ports.incoming.resource.ThotResource;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

public class ThotOrNotApplication extends Application<ThotOrNotConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ThotOrNotApplication().run(args);
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public void initialize(final Bootstrap<ThotOrNotConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(ThotOrNotConfiguration configuration, Environment environment) {
        registerSwagger(environment);
        configureConsumers(configuration, environment);
        configureObjectmapper(environment.getObjectMapper());
        configureHealthchecks(configuration, environment);
    }

    private void registerSwagger(Environment environment) {
        configureObjectmapper(Json.mapper());
        configureObjectmapper(Yaml.mapper());

        environment.jersey().register(new ApiListingResource());
        environment.jersey().register(new SwaggerSerializers());

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setResourcePackage("thotornot.ports.incoming.resource");
        beanConfig.setScan(true);
    }

    private void configureObjectmapper(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(SNAKE_CASE);
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Configure objectmapper to handle json as expected
    }

    private void configureConsumers(ThotOrNotConfiguration configuration, Environment environment) {
        ThotOrNotService thotOrNotService = new ThotOrNotService();
        // GCP consumer
        /*
        GcpSubscriber gcpSubscriber = new GcpSubscriber(configuration.project, configuration.consumerTopic, thotOrNotService, environment.getObjectMapper());
        environment.lifecycle().manage(gcpSubscriber);
        */
        // HTTP Resource
        ThotResource thotResource = new ThotResource(thotOrNotService, environment.getObjectMapper());
        environment.jersey().register(thotResource);
    }

    private void configureHealthchecks(ThotOrNotConfiguration configuration, Environment environment) {
        // Add healthcheck
    }

}
