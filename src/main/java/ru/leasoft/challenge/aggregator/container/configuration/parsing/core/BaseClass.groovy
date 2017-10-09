package ru.leasoft.challenge.aggregator.container.configuration.parsing.core

import org.apache.commons.lang.StringUtils
import ru.leasoft.challenge.aggregator.container.configuration.parsing.core.exceptions.ScriptProcessingException
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.ParsersConfigStruct
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.ParsingTargetStruct

abstract class BaseClass extends Script {

    public final static int minute = 60

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

    void target(Closure c) {
        ParsingTargetStruct pts = new ParsingTargetStruct()
        pts.with c

        if (StringUtils.isBlank(pts.name)) throw new ScriptProcessingException("'target' directive must contain non-empty argument 'name'")
        if (StringUtils.isBlank(pts.url)) throw new ScriptProcessingException("'target' directive must contain non-empty argument 'url'")
        if (StringUtils.isBlank(pts.parseWith)) throw new ScriptProcessingException("'target' directive must contain non-empty argument 'parseWith'");
        if (pts.pollingInterval < minute / 2) pts.pollingInterval = 5 * minute

        this.binding.targetsList.add pts
    }

}
