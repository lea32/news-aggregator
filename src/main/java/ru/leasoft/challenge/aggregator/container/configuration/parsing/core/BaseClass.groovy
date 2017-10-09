package ru.leasoft.challenge.aggregator.container.configuration.parsing.core

import org.apache.commons.lang.StringUtils
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.ParsersConfigStruct

abstract class BaseClass extends Script {

    static int minute = 60

    void webserver(Closure c) {
        this.binding.webserverConfig.with c
    }

    void database(Closure c) {
        this.binding.databaseConfig.with c
    }

    void parsersDir(String relativePath) {
        ParsersConfigStruct parsersConfig = this.binding.parsersStruct
        parsersConfig.parsersDirRelativePath = relativePath
    }

    void parser(Map map) {
        String parserName = map.get("name")
        if (StringUtils.isBlank(parserName)) throw new ScriptProcessingException("'parser' directive must contain non-empty argument 'name'")

        String parserLocation = map.get("src")
        if (StringUtils.isBlank(parserLocation)) throw new ScriptProcessingException("'parser' directive must contain non-empty argument 'src'")

        ParsersConfigStruct parsersConfig = this.binding.parsersStruct
        ParserDefinitionPreprocessor.appendParser(parsersConfig, parserName, parserLocation)
    }

}
