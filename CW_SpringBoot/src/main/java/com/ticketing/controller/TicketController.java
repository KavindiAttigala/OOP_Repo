package com.ticketing.controller;

import com.ticketing.configuration.SystemConfiguration;
import com.ticketing.service.Customer;
import com.ticketing.service.TicketPool;
import com.ticketing.service.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ticketingSystem")
public class TicketController {

    @Autowired
    private TicketPool ticketPool;

    private SystemConfiguration configuration;
    private List<Thread> threads = new ArrayList<>();

    //    @PostMapping("/config")
//    public String systemConfigure(@RequestBody SystemConfiguration configuration) {
//        configuration = new SystemConfiguration();
//        ticketPool.configureSystem(configuration.getMaxTicketCapacity());
//        return "Configuration Successful!";
//
//    }
    @PostMapping("/config")
    public String configureSystem(@RequestParam int totalTickets, @RequestParam int ticketReleaseRate,
                                  @RequestParam int customerRetrievalRate, @RequestParam int maxTicketCapacity) {
        configuration = new SystemConfiguration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        ticketPool.configureSystem(configuration);
        return "Configuration successful!";
    }

    @PostMapping("/start/{numVendors}/{numCustomers}")
    public String startSystem(@PathVariable("numVendors") int numVendors,@PathVariable("numCustomers") int numCustomers) {
        if (configuration == null || ticketPool == null) {
            return "System configuration is not initialized. Please configure the system first.";
        }
        List<Thread> threads = new ArrayList<>();

        // Create Vendor threads
        for (int i = 1; i <= numVendors; i++) {
            Thread thread = new Thread(new Vendor("Vendor-" + i, ticketPool, configuration));
            threads.add(thread);
            thread.start();
        }

        // Create Customer threads
        for (int i = 1; i <= numCustomers; i++) {
            Thread thread = new Thread(new Customer("Customer-" + i, ticketPool, configuration));
            threads.add(thread);
            thread.start();
        }


        return "System started with " + numVendors + " vendors and " + numCustomers + " customers.";
    }

    @PostMapping("/stop")
    public String stopSystem() {
        threads.forEach(Thread::interrupt);
        threads.clear();
        return "System stopped successfully.";
    }
}
