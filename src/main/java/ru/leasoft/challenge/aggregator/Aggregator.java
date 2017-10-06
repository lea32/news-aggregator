package ru.leasoft.challenge.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;
import ru.leasoft.challenge.aggregator.container.configuration.utils.StartupConfigLocator;
import ru.leasoft.challenge.aggregator.container.utils.ExecutionFlow;

import java.io.File;

public class Aggregator {

    private static final Logger log = LoggerFactory.getLogger(Aggregator.class);

    public static void main(String[] args) {
        log.info("Aggregator starts...");

        configureApplication(args);

        ExecutionFlow.awaitTermination();
        log.warn("Terminated");
    }

    private static void configureApplication(String[] args) {
        File configFile = StartupConfigLocator.obtainConfigFile(args);
        log.info("Loading config from " + configFile.getAbsolutePath());

        Configuration.bootstrapFromConfigFile(configFile);
    }

}
