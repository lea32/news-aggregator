package ru.leasoft.challenge.aggregator.container;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;

public class Container {

    private Undertow server;

    private String host;
    private int port;

    private static final String STATIC_WEB_FILES_PATH = "ru.leasoft.challenge.aggregator.web/";
    private static final String WELCOME = "index.html";

    private static final Logger log = LoggerFactory.getLogger(Container.class);

    private Container(String host, int port) {
        this.host = host;
        this.port = port;

        server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(makeStaticWebFilesHandler())
                .build();
    }

    private HttpHandler makeStaticWebFilesHandler() {
        ClassPathResourceManager staticWebFiles = new ClassPathResourceManager(
                Container.class.getClassLoader(),
                STATIC_WEB_FILES_PATH
        );
        return Handlers.resource(staticWebFiles).addWelcomeFiles(WELCOME);
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
