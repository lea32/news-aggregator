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

    /**
     * Marks this target as available for scheduling. Updates last poll time to current time
     */
    public void executionFinished() {
        synchronized (this) {
            this.lastPollTime = System.currentTimeMillis();
            this.scheduled = false;
        }
    }

    /**
     * Marks this target as scheduled for execution and returns runnable parsing task
     * @param parserCode parser script
     * @return ParsingTask prepared for execution or null, if target cannot be scheduled
     */
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
