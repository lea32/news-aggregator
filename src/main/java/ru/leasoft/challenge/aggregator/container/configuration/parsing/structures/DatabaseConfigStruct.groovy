package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures

class DatabaseConfigStruct implements ConfigComposer {

    public String url = ""
    public String username = ""
    public String password = ""

    public DatabasePoolConfigStruct pool = new DatabasePoolConfigStruct()

    void pool(Closure c) {
        pool.with c
    }

    @Override
    void appendTo(Map<String, Object> configMap) {
        configMap.put("database.url", url)
        configMap.put("database.username", username)
        configMap.put("database.password", password)

        this.pool.appendTo(configMap)
    }
}
