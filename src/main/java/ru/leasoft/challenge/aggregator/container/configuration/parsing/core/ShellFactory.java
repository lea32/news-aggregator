package ru.leasoft.challenge.aggregator.container.configuration.parsing.core;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.FullConfigStruct;

import java.util.HashMap;
import java.util.Map;

public class ShellFactory {

    public static class ShellWithConfig {

        public ShellWithConfig(GroovyShell shell, FullConfigStruct config) {
            this.shell = shell;
            this.config = config;
        }

        public GroovyShell shell;
        public FullConfigStruct config;
    }

    public static ShellWithConfig buildShell() {

        FullConfigStruct futureConfig = new FullConfigStruct();
        Binding binding = makeBinding(futureConfig);

        CompilerConfiguration compiler = new CompilerConfiguration();
        compiler.setScriptBaseClass(BaseClass.class.getName());

        return new ShellWithConfig(new GroovyShell(binding, compiler), futureConfig) ;
    }

    private static Binding makeBinding(FullConfigStruct configStruct) {
        Map<String, Object> bindingsMap = new HashMap<>();
        bindingsMap.put("webserverConfig", configStruct.getWebserverConfig());
        bindingsMap.put("databaseConfig", configStruct.getDatabaseConfig());

        return new Binding(bindingsMap);
    }

}
