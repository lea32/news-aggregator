package ru.leasoft.challenge.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.Container;
import ru.leasoft.challenge.aggregator.container.ContextAccessPoint;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;
import ru.leasoft.challenge.aggregator.container.configuration.utils.StartupConfigLocator;
import static ru.leasoft.challenge.aggregator.container.utils.ExecutionFlow.awaitTerminationThenDo;

import java.io.File;

public class Aggregator {

    private static final Logger log = LoggerFactory.getLogger(Aggregator.class);

    private static Container container;

    public static void main(String[] args) {
        log.info("Aggregator initialization starts...");

        configureApplication(args);
        initAndStartContainer();
        awaitTerminationThenDo(Aggregator::stopContainer);
    }

    private static void initAndStartContainer() {
        container = Container.buildContainer();
        container.start();
    }

    private static void stopContainer() {
        container.stop();
        ContextAccessPoint.destroyApplicationContext();
    }

    private static void configureApplication(String[] args) {
        File configFile = StartupConfigLocator.obtainConfigFile(args);
        log.info("Loading config from " + configFile.getAbsolutePath());

        Configuration.bootstrapFromConfigFile(configFile);
    }

}
