package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FullConfigStruct implements ConfigComposer {

    private DatabaseConfigStruct databaseConfig = new DatabaseConfigStruct();
    private WebserverConfigStruct webserverConfig = new WebserverConfigStruct();
    private ParsersConfigStruct parsersStruct = new ParsersConfigStruct();
    private List<ParsingTargetStruct> targetStructs = new ArrayList<>();

    public DatabaseConfigStruct getDatabaseConfig() {
        return databaseConfig;
    }

    public WebserverConfigStruct getWebserverConfig() {
        return webserverConfig;
    }

    public ParsersConfigStruct getParsersStruct() {
        return parsersStruct;
    }

    public List<ParsingTargetStruct> getTargetStructs() {
        return targetStructs;
    }

    @Override
    public void appendTo(Map<String, Object> configMap) {
        databaseConfig.appendTo(configMap);
        webserverConfig.appendTo(configMap);
    }
}
