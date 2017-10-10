package ru.leasoft.challenge.aggregator.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.ParsingTargetStruct;
import ru.leasoft.challenge.aggregator.engine.core.ParsingTarget;
import ru.leasoft.challenge.aggregator.engine.helpers.Parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ParsingEngine {

    private Parsers parsers;
    private List<ParsingTarget> targets = new ArrayList<>();

    private ThreadPoolExecutor executor;
    private ParsingScheduler scheduler;

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
        executor = initThreadExecutor();
        scheduler = new ParsingScheduler(targets, executor, parsers);
        scheduler.enable();
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.disable();
        }

        if (executor != null) {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                log.warn("Interrupted on thread executor termination... Some tasks may be failed");
            }
        }
    }

    private ThreadPoolExecutor initThreadExecutor() {
        int nThreads = Runtime.getRuntime().availableProcessors();
        if (nThreads < 2) {
            nThreads = 2;
        }

        return new ThreadPoolExecutor(
                2,
                nThreads,
                10,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(targets.size() + 1),
                (r, executor1) -> log.warn("Rejected execution of task")
        );
    }

}
