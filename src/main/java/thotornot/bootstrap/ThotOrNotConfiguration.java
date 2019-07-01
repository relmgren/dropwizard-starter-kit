package thotornot.bootstrap;

import com.google.auth.oauth2.GoogleCredentials;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ThotOrNotConfiguration extends Configuration {
    // TODO: implement service configuration

    @NotBlank
    public String project;

    @NotBlank
    public String consumerTopic;

    @NotNull
    @Valid
    GoogleCredentials googleCredentials;
}
