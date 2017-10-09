package ru.leasoft.challenge.aggregator.container.configuration.parsing;

import groovy.lang.GroovyShell;
import groovy.lang.MissingMethodException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.core.exceptions.ScriptProcessingException;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.core.ShellFactory;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.FullConfigStruct;

import java.io.File;
import java.io.IOException;

public class ConfigurationFileParser {

    private GroovyShell shell;
    private FullConfigStruct config;

    private static final Logger log = LoggerFactory.getLogger(ConfigurationFileParser.class);

    public ConfigurationFileParser() {
        ShellFactory.ShellWithConfig shellWithConfig = ShellFactory.buildShell();
        shell = shellWithConfig.shell;
        config = shellWithConfig.config;
    }

    public FullConfigStruct parse(File file) {
        try {
            shell.evaluate(file);
            return config;
        } catch (IOException ioException) {
            log.error("Configuration file error", ioException);
            throw new Error(ioException);
        } catch (CompilationFailedException | MissingMethodException | ScriptProcessingException compileError) {
            log.error("Syntax error in configuration file: " + compileError.getMessage());
            throw new Error();
        }
    }

}
