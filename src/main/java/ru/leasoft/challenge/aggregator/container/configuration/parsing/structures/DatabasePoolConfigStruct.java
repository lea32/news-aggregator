package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

import java.util.Map;

public class DatabasePoolConfigStruct implements ConfigComposer {

    public long maxSize = 10;
    public long minSize = 1;
    public long maxIdle = 30 * 60;
    public long maxExcessIdle = 3 * 60;
    public long maxAge = 60 * 60;
    public long increment = 1;

    @Override
    public void appendTo(Map<String, Object> configMap) {
        insertTo(configMap,"database.pool.maxSize", maxSize);
        insertTo(configMap,"database.pool.minSize", minSize);
        insertTo(configMap,"database.pool.maxIdle", maxIdle);
        insertTo(configMap,"database.pool.maxExcessIdle", maxExcessIdle);
        insertTo(configMap,"database.pool.maxAge", maxAge);
        insertTo(configMap,"database.pool.increment", increment);
    }
}
