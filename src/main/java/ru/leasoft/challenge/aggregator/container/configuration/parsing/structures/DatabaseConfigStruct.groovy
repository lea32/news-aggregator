package ru.leasoft.challenge.aggregator.container.configuration.parsing.structures;

class DatabaseConfigStruct {

    public String url = "";
    public String username = "";
    public String password = "";

    public DatabasePoolConfigStruct pool = new DatabasePoolConfigStruct();

    void pool(Closure c) {
        pool.with c
    }

}
