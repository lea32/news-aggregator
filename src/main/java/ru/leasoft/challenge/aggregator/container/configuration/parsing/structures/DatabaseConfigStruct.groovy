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
        insertTo(configMap,"database.url", url)
        insertTo(configMap,"database.username", username)
        insertTo(configMap,"database.password", password)

        this.pool.appendTo(configMap)
    }
}
