package thotornot.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jdk.jfr.internal.instrument.ThrowableTracer;
import thotornot.application.ThotOrNotService;
import thotornot.ports.incoming.resource.ThotResource;
import thotornot.ports.incoming.sub.GcpSubscriber;

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
        configureConsumers(configuration, environment);
        configureObjectmapper(environment);
        configureHealthchecks(configuration, environment);
    }

    private void configureObjectmapper(Environment environment) {
        ObjectMapper objectMapper = environment.getObjectMapper();
        // Configure objectmapper to handle json as expected
    }

    private void configureConsumers(ThotOrNotConfiguration configuration, Environment environment) {
        // GCP consumer
        ThotOrNotService thotOrNotService = new ThotOrNotService();
        GcpSubscriber gcpSubscriber = new GcpSubscriber(configuration.project, configuration.consumerTopic, thotOrNotService, environment.getObjectMapper());
        environment.lifecycle().manage(gcpSubscriber);

        // HTTP Resource
        ThotResource thotResource = new ThotResource(thotOrNotService, environment.getObjectMapper());
        environment.lifecycle().manage(thotResource);
    }

    private void configureHealthchecks(ThotOrNotConfiguration configuration, Environment environment) {
        // Add healthcheck
    }

}
