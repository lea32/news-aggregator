package ru.leasoft.challenge.aggregator.container.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

public class ExecutionFlow {

    private static final Semaphore lock = new Semaphore(0);

    private static final Logger log = LoggerFactory.getLogger(ExecutionFlow.class);

    /**
     * Stops execution of current thread until termination signal (SIGINT, SIGTERM)
     */
    public static synchronized void awaitTermination() {
        registerShutdownHook();

        try {
            lock.acquire();
        } catch (InterruptedException e) {
            log.warn("Interrupted");
        }
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                lock.release();
            }
        });
    }

}
