package ru.leasoft.challenge.aggregator.container.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.ConfigurationFileParser;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.FullConfigStruct;

import java.io.File;

public class Configuration {

    private static Configuration instance;

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private String webserverHost;
    private int webserverPort;

    private Configuration(File configFile) {
        ConfigurationFileParser parser = new ConfigurationFileParser();
        FullConfigStruct config = parser.parse(configFile);
        this.webserverHost = config.getWebserverConfig().host;
        this.webserverPort = config.getWebserverConfig().port;
    }

    public static Configuration getInstance() {
        if (instance == null) {
            log.error("Configuration object accessed, but not initialized");
            throw new RuntimeException();
        }

        return instance;
    }

    /**
     * Bootstraps configuration and placeholders from DSL-based configuration file.
     * Not thread-safe.
     * @param configFile configuration file     *
     */
    public static synchronized void bootstrapFromConfigFile(File configFile) {
        if (instance != null) {
            log.error("Configuration already initialized");
            throw new RuntimeException();
        }

        instance = new Configuration(configFile);
    }

    public String getWebserverHost() {
        return webserverHost;
    }

    public int getWebserverPort() {
        return webserverPort;
    }
}
