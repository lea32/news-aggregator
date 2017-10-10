package ru.leasoft.challenge.aggregator.engine.parsing

import ru.leasoft.challenge.aggregator.engine.parsing.api.ParsingApi

abstract class BaseParsingScript extends Script {

    @Delegate @Lazy ParsingApi api = this.binding.api

}
