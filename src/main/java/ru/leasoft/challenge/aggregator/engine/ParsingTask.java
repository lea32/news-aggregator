package ru.leasoft.challenge.aggregator.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParsingTask implements Runnable {

    private String scriptCode;
    private ParsingTarget target;

    private static final Logger log = LoggerFactory.getLogger(ParsingTask.class);

    public ParsingTask(ParsingTarget target, String scriptCode) {
        this.scriptCode = scriptCode;
        this.target = target;
    }

    @Override
    public void run() {
        try {
            executeParsing();
        } catch (Throwable t) {
            log.warn("There was exception occurred while executing " + target.getName() + " task: " + t.getMessage());
        } finally {
            target.executionFinished();
        }
    }

    public void executeParsing() {
        System.out.println("Ho-ho, i'm run!");
    }

}
