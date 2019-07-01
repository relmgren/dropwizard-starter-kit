package thotornot.bootstrap;

import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotBlank;

public class ThotOrNotConfiguration extends Configuration {
    // TODO: implement service configuration

    @NotBlank
    public String project;

    @NotBlank
    public String consumerTopic;
}
