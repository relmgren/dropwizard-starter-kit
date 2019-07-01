package thotornot.integration;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import thotornot.bootstrap.ThotOrNotApplication;
import thotornot.bootstrap.ThotOrNotConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static java.lang.String.format;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class HealthcheckIT {
    private static final DropwizardAppRule<ThotOrNotConfiguration> RULE =
            new DropwizardAppRule<>(ThotOrNotApplication.class, resourceFilePath("development.yml"));

    private Client client;

    @ClassRule
    public static final TestRule CHAIN = RuleChain.outerRule(RULE);

    @Before
    public void beforeClass() {
        client = new JerseyClientBuilder(RULE.getEnvironment())
                .withProperty(READ_TIMEOUT, 10000)
                .build(HealthcheckIT.class.getName());
    }

    @After
    public void afterClass() {
        client.close();
    }

    @Test
    public void shouldBeHealthy() {
        Response response = client.target(format("http://localhost:%d/healthcheck", RULE.getAdminPort()))
                .request()
                .get();

        assertThat(response.getStatus(), equalTo(200));
    }
}
