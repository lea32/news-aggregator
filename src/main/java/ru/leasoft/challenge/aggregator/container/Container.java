package ru.leasoft.challenge.aggregator.container;

import io.undertow.Undertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;

public class Container {

    private Undertow server;

    private String host;
    private int port;

    private static final Logger log = LoggerFactory.getLogger(Container.class);

    private Container(String host, int port) {
        this.host = host;
        this.port = port;

        server = Undertow.builder()
                .addHttpListener(port, host)
                .build();
    }

    public static Container buildContainer() {
        Configuration config = Configuration.getInstance();

        return new Container(
                config.getWebserverHost(),
                config.getWebserverPort()
        );
    }

    public void start() {
        server.start();
        log.info("Server started at " + host + ":" + port);
    }

    public void stop() {
        server.stop();
        log.info("Server stopped");
    }

}
