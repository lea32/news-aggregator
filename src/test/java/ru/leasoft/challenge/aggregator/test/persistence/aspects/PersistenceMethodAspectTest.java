package ru.leasoft.challenge.aggregator.test.persistence.aspects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.leasoft.challenge.aggregator.persistence.PersistenceLevelException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/aggregator-test.xml")
public class PersistenceMethodAspectTest {

    @Autowired
    private MockObjectForAspectTest mock;

    @Test(expected = PersistenceLevelException.class)
    public void testMethodWithException() {
        mock.methodThatThrowsException();
    }

    @Test
    public void testMethodWithoutException() {
        mock.methodNotThrowsException();
    }

}
