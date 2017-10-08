package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

import java.util.Map;

public interface ConfigComposer {

    void appendTo(Map<String, Object> configMap);

    default void insertTo(Map<String, Object> configMap, String paramName, Object value) {
        configMap.put(paramName, value.toString());
    }
}
