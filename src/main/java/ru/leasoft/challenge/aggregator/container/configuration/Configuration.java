package ru.leasoft.challenge.aggregator.container.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.ConfigurationFileParser;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.FullConfigStruct;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configuration extends PropertyPlaceholderConfigurer {

    private static Configuration instance;

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private FullConfigStruct config;

    private Configuration(File configFile) {
        ConfigurationFileParser parser = new ConfigurationFileParser();
        this.config = parser.parse(configFile);
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
        return config.getWebserverConfig().host;
    }

    public int getWebserverPort() {
        return config.getWebserverConfig().port;
    }

    public String getDatabaseUrl() {
        return config.getDatabaseConfig().url;
    }

    public String getDatabaseUser() {
        return config.getDatabaseConfig().username;
    }

    public String getDatabasePassword() {
        return config.getDatabaseConfig().password;
    }

    @Override
    protected void loadProperties(Properties props) throws IOException {
        super.loadProperties(props);

        Map<String, Object> propsFromConfig = new HashMap<>();
        config.appendTo(propsFromConfig);
        propsFromConfig.forEach(props::put);
    }
}
