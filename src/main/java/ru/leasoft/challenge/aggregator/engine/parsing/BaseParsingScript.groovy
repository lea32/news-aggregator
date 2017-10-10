package ru.leasoft.challenge.aggregator.engine.parsing

abstract class BaseParsingScript extends Script {

    @Delegate @Lazy ParsingApi api = this.binding.api

}
