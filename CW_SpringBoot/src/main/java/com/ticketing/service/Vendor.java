package com.ticketing.service;

import com.ticketing.configuration.SystemConfiguration;

public class Vendor extends User{
    private SystemConfiguration config;
    private TicketPool ticketpool;
    private String username;
    public Vendor(String username, TicketPool ticketpool, SystemConfiguration config) {
        super();
        this.username=username;
        this.config = config;
        this.ticketpool=ticketpool;
    }

    @Override
    protected void performTask(){
        if (config == null) {
            throw new IllegalStateException("Configuration is not initialized");
        }
        while (!Thread.currentThread().isInterrupted() && ticketpool.isSimulationRunning()) {
            try {
                ticketpool.addTickets(config.getTicketReleaseRate(), username);
                Thread.sleep(1000); // Simulate release rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // Stop task if interrupted
            }
        }
        System.out.println(username + " has stopped adding tickets as the simulation ended.");

        //                System.out.println(username + " added " + config.getTicketReleaseRate() + " tickets.");

//        ticketpool.addTickets(config.getTicketReleaseRate(), username);
//        System.out.println(username + " added "+config.getTicketReleaseRate()+" tickets.");
//        try {
//            Thread.sleep(1000); // Simulate release rate
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

    }
}
