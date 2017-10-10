package ru.leasoft.challenge.aggregator.test.persistence.aspects;

import ru.leasoft.challenge.aggregator.persistence.aspects.PersistenceMethod;

public class MockObjectForAspectTest {

    @PersistenceMethod
    public void methodThatThrowsException() {
        throw new RuntimeException();
    }

    @PersistenceMethod
    public boolean methodNotThrowsException() {
        return true;
    }

}
