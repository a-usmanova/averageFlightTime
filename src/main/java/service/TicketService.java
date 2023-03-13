package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import model.Ticket;
import model.TicketWrapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Data
public class TicketService {
    private final String PATH = "src/main/resources/tickets.json";
    private List<Long> timeOfFlights = new ArrayList<>();

    public TicketService() {
        fullTimeOfFlights();
    }

    public String getAverageFlightTime() {
        if (timeOfFlights.isEmpty()) return "";
        long averageTime =  (long) timeOfFlights.stream()
                .mapToLong(a -> a)
                .average().orElse(0);
        return DateTimeFormatter.ofPattern("hh:mm")
                .format(ZonedDateTime.ofInstant(Instant.ofEpochSecond(averageTime), ZoneId.of("GMT")));

    }

    public String getPercentile(int percentile) {
        if (timeOfFlights.isEmpty()) return "";
        Collections.sort(timeOfFlights);
        int index = (int) Math.ceil(((double) percentile / 100) * timeOfFlights.size());

        return DateTimeFormatter.ofPattern("hh:mm")
                .format(ZonedDateTime.ofInstant(Instant.ofEpochSecond(timeOfFlights.get(index - 1)), ZoneId.of("GMT")));
    }

    private void fullTimeOfFlights() {
        String jsonString = getJSONFile();
        ObjectMapper objectMapper = new ObjectMapper().registerModules();

        try {
            TicketWrapper wrapper = objectMapper.readValue(jsonString, TicketWrapper.class);
            List<Ticket> tickets = wrapper.getTickets();
            tickets.forEach(t -> timeOfFlights.add(t.getArrivalDate().toEpochSecond() - t.getDepartureDate().toEpochSecond()));

        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getJSONFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATH));
            lines.forEach(builder::append);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return builder.toString().replaceAll("\uFEFF", "")
                .replaceAll("\",    \"departure_time\": \"", " ")
                .replaceAll("\",    \"arrival_time\": \"", " ");
    }
}
