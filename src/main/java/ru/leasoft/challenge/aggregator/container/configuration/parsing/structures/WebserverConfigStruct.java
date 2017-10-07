package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

import java.util.Map;

public class WebserverConfigStruct implements ConfigComposer {

    public String host = "localhost";
    public int port = 8080;

    @Override
    public void appendTo(Map<String, Object> configMap) {
        configMap.put("webserver.host", host);
        configMap.put("webserver.port", port);
    }
}
