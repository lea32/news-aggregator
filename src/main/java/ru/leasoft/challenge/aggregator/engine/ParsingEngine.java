package ru.leasoft.challenge.aggregator.engine;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.ParsingTargetStruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParsingEngine {

    private Parsers parsers;
    private List<ParsingTarget> targets = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(ParsingEngine.class);

    private ParsingEngine() {}

    public static ParsingEngine buildEngine() {
        ParsingEngine engine = new ParsingEngine();
        Map<String, String> parsersConfigured = Configuration.getInstance().getParsersMap();
        engine.parsers = new Parsers(parsersConfigured);

        List<ParsingTargetStruct> targetsConfig = Configuration.getInstance().getTargetsConfiguration();
        targetsConfig.forEach(engine::appendTarget);

        return engine;
    }

    private void appendTarget(ParsingTargetStruct targetConfigStructure) {
        ParsingTarget target = ParsingTarget.instantiateFromConfigStructure(targetConfigStructure);

        if (parsers.parserExist(target.getParserName())) {

            if (targets.contains(target)) {
                log.warn("Duplicate target [" + target.getName() + "]");
                return;
            }

            targets.add(target);
            log.info("Target [" + target.getName() + "] enabled");

        } else {
            log.warn("No parser with name " + target.getParserName() + " found. Target [" + target.getName() + "] will be disabled.");
        }
    }

    public void start() {
        log.info("Parsing engine starts...");
    }

}
