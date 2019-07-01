package thotornot.ports.incoming.sub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import io.dropwizard.lifecycle.Managed;
import org.apache.log4j.Logger;
import thotornot.application.ThotOrNotService;
import thotornot.domain.SubscriptionDto;

import java.util.concurrent.TimeUnit;

public class GcpSubscriber implements Managed {
    private static final Logger LOG = Logger.getLogger(GcpSubscriber.class.getName());
    private final String project;
    private final String topic;
    private final ThotOrNotService service;
    private final ObjectMapper objectMapper;
    private Subscriber subscriber;

    public GcpSubscriber(String project, String topic, ThotOrNotService service, ObjectMapper objectMapper) {
        this.project = project;
        this.topic = topic;
        this.service = service;
        this.objectMapper = objectMapper;
    }

    private void handleMessage(ByteString data) {
        SubscriptionDto subscriptionDto = objectMapper.convertValue(data.toStringUtf8(), SubscriptionDto.class);

        service.handle(subscriptionDto);
    }

    @Override
    public void start() throws Exception {
        ProjectSubscriptionName subscription = ProjectSubscriptionName.of(project, topic).toBuilder().build();
        MessageReceiver receiver = (message, consumer) -> {
            System.out.println("Got message: " + message.getData().toStringUtf8());
            handleMessage(message.getData());
            consumer.ack();
        };

        subscriber = Subscriber.newBuilder(subscription, receiver).build();

        subscriber.addListener(
                new Subscriber.Listener() {
                    @Override
                    public void failed(Subscriber.State from, Throwable failure) {
                        // Handle failure. This is called when the Subscriber encountered a fatal error and is shutting down.
                        LOG.error("Subscriber failed to receive message", failure);
                    }
                }, MoreExecutors.directExecutor());
    }

    @Override
    public void stop() throws Exception {
        subscriber.awaitTerminated(1000, TimeUnit.MILLISECONDS);
    }
}
