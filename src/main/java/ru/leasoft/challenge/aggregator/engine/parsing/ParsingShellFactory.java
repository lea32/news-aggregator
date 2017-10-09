package ru.leasoft.challenge.aggregator.engine.parsing;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.core.BaseClass;
import ru.leasoft.challenge.aggregator.engine.ParsingTarget;

import java.util.HashMap;
import java.util.Map;

public class ParsingShellFactory {

    public static GroovyShell buildShell(ParsingTarget target) {
        Binding binding = makeBinding(target);

        CompilerConfiguration compiler = new CompilerConfiguration();
        compiler.setScriptBaseClass(BaseClass.class.getName());

        return new GroovyShell(binding, compiler);
    }

    private static Binding makeBinding(ParsingTarget target) {
        Map<String, Object> bindingsMap = new HashMap<>();
        bindingsMap.put("baseUrl", target.getUrl());

        return new Binding(bindingsMap);
    }

}
