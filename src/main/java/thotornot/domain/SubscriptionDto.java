package thotornot.domain;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonSerializableSchema
public class SubscriptionDto {
    @NotNull
    @Valid
    public EventDto event;

    public class EventDto {
        @NotBlank
        public String name;
    }
}
