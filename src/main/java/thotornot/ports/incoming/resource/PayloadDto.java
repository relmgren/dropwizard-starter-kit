package thotornot.ports.incoming.resource;

import org.hibernate.validator.constraints.NotBlank;

public class PayloadDto {
    @NotBlank
    public String thot;
    @NotBlank
    public String thot2;
    @NotBlank
    public String vote;
}
