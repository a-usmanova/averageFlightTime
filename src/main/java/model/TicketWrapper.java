package model;

import lombok.Data;

import java.util.List;

@Data
public class TicketWrapper {
    private List<Ticket> tickets;
}
