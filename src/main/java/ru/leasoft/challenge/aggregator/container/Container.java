package ru.leasoft.challenge.aggregator.container;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.cache.DirectBufferCache;
import io.undertow.server.handlers.resource.CachingResourceManager;
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

    private static final int MAX_ITEMS_IN_CACHE = 100;
    private static final int MAX_CACHEABLE_FILE_SIZE = 1024 * 1024 * 10; //10 Mb
    private static final int NOT_EVICT = -1;

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
        ClassPathResourceManager staticWebFilesManager = new ClassPathResourceManager(
                Container.class.getClassLoader(),
                STATIC_WEB_FILES_PATH
        );

        CachingResourceManager cachingManager = new CachingResourceManager(
                MAX_ITEMS_IN_CACHE,
                MAX_CACHEABLE_FILE_SIZE,
                new DirectBufferCache(1024, 10, 1024*1024*1024),
                staticWebFilesManager,
                NOT_EVICT
        );

        return Handlers.resource(cachingManager).addWelcomeFiles(WELCOME);
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
