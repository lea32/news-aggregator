package ru.leasoft.challenge.aggregator.container.configuration.parsing.core

abstract class BaseClass extends Script {

    void webserver(Closure c) {
        this.binding.webserverConfig.with c
    }

    void database(Closure c) {
        this.binding.databaseConfig.with c
    }

}
