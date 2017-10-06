package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

public class FullConfigStruct {

    private DatabaseConfigStruct databaseConfig = new DatabaseConfigStruct();
    private WebserverConfigStruct webserverConfig = new WebserverConfigStruct();

    public DatabaseConfigStruct getDatabaseConfig() {
        return databaseConfig;
    }

    public WebserverConfigStruct getWebserverConfig() {
        return webserverConfig;
    }
}
