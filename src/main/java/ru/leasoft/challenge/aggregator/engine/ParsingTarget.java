package ru.leasoft.challenge.aggregator.engine;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.ParsingTargetStruct;

public class ParsingTarget {

    private String url;
    private String name;
    private String parserName;
    private long pollingInterval;
    private volatile boolean scheduled = false;

    private volatile long lastPollTime = 0;

    public ParsingTarget(String url, String name, String parserName, long pollingInterval) {
        this.url = url;
        this.name = name;
        this.parserName = parserName;
        this.pollingInterval = pollingInterval;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getParserName() {
        return parserName;
    }

    public void executionFinished() {
        synchronized (this) {
            this.lastPollTime = System.currentTimeMillis();
            this.scheduled = false;
        }
    }

    public ParsingTask schedule(String parserCode) {
        synchronized (this) {
            if (scheduled || System.currentTimeMillis() - lastPollTime < pollingInterval) return null;

            scheduled = true;
        }

        return new ParsingTask(this, parserCode);
    }

    public static ParsingTarget instantiateFromConfigStructure(ParsingTargetStruct pts) {
        return new ParsingTarget(pts.url, pts.name, pts.parseWith, pts.pollingInterval * 1000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsingTarget target = (ParsingTarget) o;

        return new EqualsBuilder()
                .append(url, target.url)
                .append(name, target.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(url)
                .append(name)
                .toHashCode();
    }
}
