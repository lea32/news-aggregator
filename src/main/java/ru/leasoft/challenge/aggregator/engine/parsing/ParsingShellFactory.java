package ru.leasoft.challenge.aggregator.engine.parsing;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.leasoft.challenge.aggregator.engine.core.ParsingTarget;
import ru.leasoft.challenge.aggregator.engine.parsing.api.ParsingApi;

import java.util.HashMap;
import java.util.Map;

public class ParsingShellFactory {

    public static GroovyShell buildShell(ParsingTarget target) {
        Binding binding = makeBinding(target);

        CompilerConfiguration compiler = new CompilerConfiguration();
        compiler.setScriptBaseClass(BaseParsingScript.class.getName());

        return new GroovyShell(binding, compiler);
    }

    private static Binding makeBinding(ParsingTarget target) {
        Map<String, Object> bindingsMap = new HashMap<>();
        bindingsMap.put("baseUrl", target.getUrl());

        ParsingApi api = new ParsingApi(target);
        bindingsMap.put("api", api);

        return new Binding(bindingsMap);
    }

}
