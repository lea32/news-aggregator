package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

import java.util.Map;

public interface ConfigComposer {

    void appendTo(Map<String, Object> configMap);
}
