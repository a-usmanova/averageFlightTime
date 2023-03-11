package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import service.CustomDeserializer;

import java.time.ZonedDateTime;

@Data
public class Ticket {

    private Abbreviate origin;
    @JsonProperty("origin_name")
    private String originName;
    private Abbreviate destination;
    @JsonProperty("destination_name")
    private String destinationName;
    @JsonProperty("departure_date")
    @JsonDeserialize(using = CustomDeserializer.class)
    private ZonedDateTime departureDate;
    @JsonProperty("arrival_date")
    @JsonDeserialize(using = CustomDeserializer.class)
    private ZonedDateTime arrivalDate;
    private String carrier;
    private Integer stops;
    private Boolean price;

}
