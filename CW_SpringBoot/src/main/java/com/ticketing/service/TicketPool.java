package com.ticketing.service;

import com.ticketing.configuration.SystemConfiguration;
import com.ticketing.model.Transaction;
import com.ticketing.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketPool {
    private TransactionRepository transactionRepository;
    private int availableTickets;
    private SystemConfiguration config;
    private int maxCapacity;
    private int totalTickets=0;
    private final List<Thread> activeThreads = new ArrayList<>();
    private volatile boolean simulationRunning = true;  // Add this flag




    @Autowired
    public TicketPool(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
//    public TicketPool(SystemConfiguration config) {
//        this.config = config; // Ensure config is assigned during initialization
//    }
//    @Autowired
//    public void setSystemConfiguration(SystemConfiguration config) {
//        this.config = config;
//    }
//    @Autowired
//    public TicketPool(TransactionRepository transactionRepository, SystemConfiguration config) {
//        this.transactionRepository = transactionRepository;
//        this.config = config;
//    }

    public synchronized void addTickets(int amount, String username) {
        if (!simulationRunning) return;  // Check if simulation is stopped

        if (config == null) {
            System.err.println("Error: SystemConfiguration is null in TicketPool.");
            return;
        }
        while (availableTickets + amount >= config.getTotalTickets() && simulationRunning) {
            try {
                System.out.println("Vendor waiting for space in the ticket pool.");
                wait(); // Wait if there are too many tickets
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        if (!simulationRunning) return;

        // Add the tickets if space is available
//        availableTickets = Math.min(availableTickets + amount, config.getMaxTicketCapacity());
//        notifyAll();


        // If the pool is already full
        if (totalTickets  + amount >= config.getMaxTicketCapacity()) {
            System.out.println("Maximum capacity reached. Stopping the system");
            stopSimulation();  // Stop the simulation when max capacity is reached
            return;
//            for (int i = 0; i < amount; i++) {
////                Long ticketId = ticketIdGenerator.getAndIncrement();
//                transactionRepository.save(new Transaction(null, username));
//
        }
//        for (int i = 0; i < amount; i++) {
//            Transaction transaction = new Transaction();
//            transaction.setVendorId(username);
////                Long ticketId = ticketIdGenerator.getAndIncrement();
//            transactionRepository.save(transaction);
//        }

        // Add the tickets if space is available
        availableTickets = Math.min(availableTickets + amount, config.getMaxTicketCapacity());
        totalTickets = totalTickets+amount;

        for (int i = 0; i < amount; i++) {
            Transaction transaction = new Transaction();
            transaction.setVendorId(username);
//                Long ticketId = ticketIdGenerator.getAndIncrement();
            transactionRepository.save(transaction);
        }

        System.out.println("Vendor added " + amount + " tickets. Available tickets: " + availableTickets+", Total tickets sold: "+totalTickets);
//        transactionRepository.save(new Transaction("1", username));
        notifyAll(); // Notify any waiting customer threads that tickets are available
    }

    public synchronized void retrieveTickets(int amount, String username) {
        if (!simulationRunning) return;  // Check if simulation is stopped

        if (config == null) {
            System.err.println("Error: SystemConfiguration is null in TicketPool.");
            return;
        }
        while (availableTickets < amount && simulationRunning) {
            try {
                System.out.println("Customer waiting for tickets.");
                wait(); // Wait if there aren't enough tickets
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
//        availableTickets -= amount;
//        notifyAll();
//        while (availableTickets<=maxCapacity){
//            try {
//                System.out.println("Maximum capacity reached. Stopping the system");
//                System.exit(0); // Wait if there are too many tickets
//            } catch (Exception e) {
//                Thread.currentThread().interrupt();
//                return;
//            }
//
//        }
//        for (int i = 0; i < amount; i++) {
////                Long ticketId = ticketIdGenerator.getAndDecrement();
//            transactionRepository.save(new Transaction(username, "null"));
//        }
//        for (int i = 0; i < amount; i++) {
//            Long ticketId = ticketIdGenerator.getAndDecrement();
//            transactionRepository.save(new Transaction(ticketId, username.replace("Customer-", ""), null));
//        }
        if (!simulationRunning) return;
        availableTickets -= amount;

        for (int i = 0; i < amount; i++) {
//            Transaction transaction = new Transaction();
//            transaction.setCustomerId(username);
            transactionRepository.updateCustomerIdForVendorTransactions(username);
//                Long ticketId = ticketIdGenerator.getAndIncrement();
//            transactionRepository.save(transaction);
        }
//        transactionRepository.updateCustomerIdForVendorTransactions(username);
        System.out.println("Customer retrieved " + amount + " tickets. Remaining tickets: " + availableTickets);
        notifyAll(); // Notify any waiting customer threads that tickets are available
    }
    public void configureSystem(SystemConfiguration configuration) {
        this.config = configuration;
    }

    public void stopSimulation() {
        if (!simulationRunning) return;
        simulationRunning = false;
        synchronized (activeThreads) {
            for (Thread thread : activeThreads) {
                thread.interrupt();
            }

            for (Thread thread : activeThreads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            activeThreads.clear(); // Clear the list after stopping

        }
        notifyAll(); // Wake up any waiting threads to stop
        System.out.println("All threads have been interrupted and joined. Simulation stopped.");
    }

    public synchronized void registerThread(Thread thread) {
        activeThreads.add(thread);
    }
    public synchronized boolean isSimulationRunning() {
        return simulationRunning;
    }

//    public void registerThread(Thread thread) {
//        synchronized (activeThreads) {
//            activeThreads.add(thread);
//        }
//    }
//
//    private void interruptAndJoinThreads() {
//        synchronized (activeThreads) {
//            for (Thread thread : activeThreads) {
//                thread.interrupt();
//            }
//
//            for (Thread thread : activeThreads) {
//                try {
//                    thread.join();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        }
//        System.out.println("All threads have been interrupted and joined.");
//    }

}
