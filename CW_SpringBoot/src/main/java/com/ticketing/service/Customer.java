package com.ticketing.service;

import com.ticketing.configuration.SystemConfiguration;

public class Customer extends User{
    private SystemConfiguration config;
    private TicketPool ticketpool;
    private String username;
    public Customer(String username, TicketPool ticketPool, SystemConfiguration config) {
        super();
        this.username=username;
        this.config = config;
        this.ticketpool=ticketPool;
    }

    @Override
    protected void performTask(){
        if (config == null) {
            throw new IllegalStateException("Configuration is not initialized for " + username);
        }
        while (Thread.currentThread().isInterrupted() == false && ticketpool.isSimulationRunning()) {
            ticketpool.retrieveTickets(config.getCustomerRetrievalRate(), username);
            System.out.println(username + " retrieved " + config.getCustomerRetrievalRate() + " tickets.");
            try {
                ticketpool.retrieveTickets(config.getCustomerRetrievalRate(), username);
//                System.out.println(username + " retrieved " + config.getCustomerRetrievalRate() + " tickets.");
                Thread.sleep(1000); // Simulate retrieval rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // Stop task if interrupted
            }
        }
//        ticketpool.retrieveTickets(config.getCustomerRetrievalRate(), username);
//        System.out.println(username + " retrieved "+config.getCustomerRetrievalRate()+" tickets.");
//        try {
//            Thread.sleep(1000); // Simulate release rate
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
    }
}
