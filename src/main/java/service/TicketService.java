package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import model.Ticket;
import model.TicketWrapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
        int averageTime = (int) timeOfFlights.stream()
                .mapToLong(a -> a)
                .average().orElse(0);
        return new SimpleDateFormat("hh:mm").format(new Date(averageTime * 1000L));

    }

    public String getPercentile(int percentile) {
        if (timeOfFlights.isEmpty()) return "";
        Collections.sort(timeOfFlights);
        int index = (int) Math.ceil(((double) percentile / 100) * timeOfFlights.size());

        return new SimpleDateFormat("hh:mm").format(new Date(timeOfFlights.get(index - 1) * 1000));
    }

    private void fullTimeOfFlights() {
        String jsonString = getJSONFile();
        ObjectMapper objectMapper = new ObjectMapper().registerModules();

        try {
            TicketWrapper wrapper = objectMapper.readValue(jsonString, TicketWrapper.class);
            List<Ticket> tickets = wrapper.getTickets();
            tickets.forEach(t -> timeOfFlights.add(t.getArrivalDate().toEpochSecond() - t.getDepartureDate().toEpochSecond()));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
