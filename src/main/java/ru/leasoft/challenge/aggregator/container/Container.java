package ru.leasoft.challenge.aggregator.container;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.cache.DirectBufferCache;
import io.undertow.server.handlers.resource.CachingResourceManager;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import io.undertow.servlet.api.ServletInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;
import ru.leasoft.challenge.aggregator.container.configuration.utils.ContextLoaderListenerInstanceFactory;
import ru.leasoft.challenge.aggregator.container.configuration.utils.DispatcherServletInstanceFactory;

import javax.servlet.ServletException;

public class Container {

    private Undertow server;

    private String host;
    private int port;

    private static final String STATIC_WEB_FILES_PATH = "ru.leasoft.challenge.aggregator.web/";
    private static final String WELCOME = "index.html";

    private static final int MAX_ITEMS_IN_CACHE = 100;
    private static final int MAX_CACHEABLE_FILE_SIZE = 1024 * 1024 * 10; //10 Mb
    private static final int NOT_EVICT = -1;

    private static final int SESSION_TIMEOUT = 5 * 60;

    private static final Logger log = LoggerFactory.getLogger(Container.class);

    private Container(String host, int port) throws ServletException {
        this.host = host;
        this.port = port;

        DeploymentManager deploymentManager = passDeploymentInfo();
        deploymentManager.deploy();
        HttpHandler servletsHandled = deploymentManager.start();

        PathHandler api = Handlers.path(Handlers.redirect("/")).addPrefixPath("/", servletsHandled);

        PathHandler fullHandler = Handlers
                .path(buildStaticWebFilesHandler()).addPrefixPath("/api", api);


        server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(fullHandler)
                .build();
    }

    private DeploymentManager passDeploymentInfo() {
        ResourceManager commonResourceManager = new ClassPathResourceManager(Container.class.getClassLoader());
        ListenerInfo springContextLoaderListener = createContextLoaderListener();

        DeploymentInfo deployment = Servlets.deployment()
                .setDeploymentName("aggregator")
                .setContextPath("/")
                .setClassLoader(Container.class.getClassLoader())
                .setResourceManager(commonResourceManager)
                .addListener(springContextLoaderListener)
                .addInitParameter("contextConfigLocation", "spring/aggregator.xml")
                .addServlet(buildDispatcherServlet())
                .setDefaultSessionTimeout(SESSION_TIMEOUT);

        return Servlets.defaultContainer().addDeployment(deployment);
    }

    private ListenerInfo createContextLoaderListener() {
        return new ListenerInfo(ContextLoaderListener.class, new ContextLoaderListenerInstanceFactory());
    }

    private HttpHandler buildStaticWebFilesHandler() {
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

    private ServletInfo buildDispatcherServlet() {
        ServletInfo servletInfo = Servlets
                .servlet("APIDispatcher", DispatcherServlet.class, new DispatcherServletInstanceFactory())
                .setLoadOnStartup(1);

        servletInfo.setAsyncSupported(true);
        servletInfo.addMapping("/*");
        servletInfo.addInitParam("contextConfigLocation", "spring/aggregator-api.xml");
        return servletInfo;
    }

    public static Container buildContainer() {
        Configuration config = Configuration.getInstance();
        try {
            return new Container(
                    config.getWebserverHost(),
                    config.getWebserverPort()
            );
        } catch (ServletException ex) {
            log.error("Servlet creation exception: " + ex.getMessage());
            throw new Error(ex);
        }
    }

    public void start() {
        server.start();
        log.info("News Aggregator server started at " + host + ":" + port);
    }

    public void stop() {
        server.stop();
        log.info("Server stopped");
    }

}
