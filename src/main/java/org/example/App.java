package org.example;

import service.TicketService;

public class App
{
    public static void main( String[] args )
    {

        TicketService service = new TicketService();

        System.out.println("среднее время полета между городами Владивосток и Тель-Авив - " + service.getAverageFlightTime());
        System.out.println("90-й процентиль времени полета между городами Владивосток и Тель-Авив " + service.getPercentile(90));

    }
}
