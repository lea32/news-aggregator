package ru.leasoft.challenge.aggregator.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.engine.helpers.Parsers;

import java.util.List;
import java.util.concurrent.Executor;

public class ParsingScheduler implements Runnable {

    private static final long TIMEOUT = 1000;

    private Executor executor;
    private Parsers parsers;
    private List<ParsingTarget> targets;

    private volatile boolean enabled = false;
    private volatile Thread schedulerThread;

    private static final Logger log = LoggerFactory.getLogger(ParsingScheduler.class);

    public ParsingScheduler(List<ParsingTarget> targets, Executor executor, Parsers parsers) {
        this.executor = executor;
        this.parsers = parsers;
        this.targets = targets;
    }

    public void enable() {
        enabled = true;

        schedulerThread = prepareThread();
        schedulerThread.start();
    }

    public void disable() {
        enabled = false;
        if (schedulerThread != null) {
            schedulerThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (enabled) {

            targets.forEach(target -> {
                String parserScript = parsers.getParserCode(target.getParserName());
                if (parserScript == null) {
                    throw new RuntimeException("no parser script for " + target.getName());
                }

                ParsingTask task = target.schedule(parserScript);
                if (task != null) {
                    executor.execute(task);
                }
            });

            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                if (enabled) {
                    log.warn("Scheduler thread was interrupted, but scheduler still enabled. Restart.");
                    this.enable();
                }
            }
        }
    }

    private Thread prepareThread() {
        Thread schedulerThread = new Thread(this, "Parsing scheduler");
        schedulerThread.setUncaughtExceptionHandler((t, e) -> {
            log.error("Uncaught exception in scheduler thread! ", e);
        });
        return schedulerThread;
    }

}
