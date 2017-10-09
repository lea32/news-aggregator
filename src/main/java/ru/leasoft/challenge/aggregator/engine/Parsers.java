package ru.leasoft.challenge.aggregator.engine;

import java.util.Collections;
import java.util.Map;

public class Parsers {

    private Map<String, String> parsersConfigured;

    public Parsers(Map<String, String> parsersConfigured) {
        this.parsersConfigured = Collections.synchronizedMap(parsersConfigured);
    }

    /**
     * Get parser script code by parser name
     * @param parserName name of parser declared in configuration
     * @return Parser script code or null if no parser with this parser name found
     */
    public String getParserCode(String parserName) {
        return parsersConfigured.get(parserName);
    }

    public boolean parserExist(String parserName) {
        return parsersConfigured.containsKey(parserName);
    }
}
