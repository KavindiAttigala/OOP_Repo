package com.ticketing.service;

import com.ticketing.configuration.SystemConfiguration;

public abstract class User implements Runnable{
    protected String username;
    protected TicketPool ticketpool;
    protected SystemConfiguration config;
    private Thread thread;

    public User() {
    }
//    public User(TicketPool ticketPool, String username) {
//        this.ticketpool = ticketPool;
//        this.username = username;
//        this.thread = new Thread(this, username);
//        ticketPool.registerThread(thread);
//    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            performTask();
        }
    }

    protected abstract void performTask();

}
