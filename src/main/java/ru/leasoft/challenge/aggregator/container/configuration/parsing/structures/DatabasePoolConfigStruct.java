package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

import java.util.Map;

public class DatabasePoolConfigStruct implements ConfigComposer {

    public long maxSize = 10;
    public long minSize = 1;
    public long maxIdle = 30 * 60;
    public long maxExcessIdle = 3 * 60;
    public long maxAge = 60 * 60;

    @Override
    public void appendTo(Map<String, Object> configMap) {
        configMap.put("database.pool.maxSize", maxSize);
        configMap.put("database.pool.minSize", minSize);
        configMap.put("database.pool.maxIdle", maxIdle);
        configMap.put("database.pool.maxExcessIdle", maxExcessIdle);
        configMap.put("database.pool.maxAge", maxAge);
    }
}
