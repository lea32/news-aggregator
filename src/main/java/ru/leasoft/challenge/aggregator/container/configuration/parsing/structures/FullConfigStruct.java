package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

import java.util.Map;

public class FullConfigStruct implements ConfigComposer {

    private DatabaseConfigStruct databaseConfig = new DatabaseConfigStruct();
    private WebserverConfigStruct webserverConfig = new WebserverConfigStruct();

    public DatabaseConfigStruct getDatabaseConfig() {
        return databaseConfig;
    }

    public WebserverConfigStruct getWebserverConfig() {
        return webserverConfig;
    }

    @Override
    public void appendTo(Map<String, Object> configMap) {
        databaseConfig.appendTo(configMap);
        webserverConfig.appendTo(configMap);
    }
}
