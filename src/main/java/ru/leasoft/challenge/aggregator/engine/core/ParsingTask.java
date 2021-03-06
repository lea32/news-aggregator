package ru.leasoft.challenge.aggregator.engine.core;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import groovy.lang.GroovyShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;
import ru.leasoft.challenge.aggregator.engine.parsing.ParsingShellFactory;

public class ParsingTask implements Runnable {

    private String scriptCode;
    private ParsingTarget target;

    private static final Logger log = LoggerFactory.getLogger(ParsingTask.class);
    private static final int MAX_EXECUTION_TIME = 1000 * 60; //1 minute for script to finish execution

    private boolean debugMode = Configuration.getInstance().isDebug();

    public ParsingTask(ParsingTarget target, String scriptCode) {
        this.scriptCode = scriptCode;
        this.target = target;
    }

    @Override
    public void run() {
        try {
            new ParsingShellCommand().execute();
        } catch (Throwable t) {
            if (t.getCause() != null) {
                log.warn("There was exception occurred while executing " + target.getName() + " task: " + t.getCause().getMessage());
                if (debugMode) {
                    t.getCause().printStackTrace();
                }
            } else {
                log.warn("Execution timeout [" + target.getName() + "]");
            }
        } finally {
            target.executionFinished();
        }
    }

    private void executeParsing() {
        GroovyShell shell = ParsingShellFactory.buildShell(target);
        shell.evaluate(scriptCode);
    }

    private class ParsingShellCommand extends HystrixCommand<Void> {

        ParsingShellCommand() {
            super(Setter.withGroupKey(
                    HystrixCommandGroupKey.Factory.asKey("ParsingShellCommand"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(MAX_EXECUTION_TIME))
            );
        }

        @Override
        protected Void run() throws Exception {
            ParsingTask.this.executeParsing();
            return null;
        }
    }

}
