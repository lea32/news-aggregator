package ru.leasoft.challenge.aggregator.engine;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ParsingTask implements Runnable {

    private String name;
    private String url;
    private String scriptCode;

    public ParsingTask(String name, String url, String scriptCode) {
        this.name = name;
        this.url = url;
        this.scriptCode = scriptCode;
    }

    @Override
    public void run() {
        System.out.println("Ho-ho, i'm run!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParsingTask that = (ParsingTask) o;
        return new EqualsBuilder()
                .append(name, that.name)
                .append(url, that.url)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(url)
                .toHashCode();
    }
}
