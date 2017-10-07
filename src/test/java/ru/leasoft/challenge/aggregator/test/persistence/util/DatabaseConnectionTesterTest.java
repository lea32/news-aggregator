package ru.leasoft.challenge.aggregator.test.persistence.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.leasoft.challenge.aggregator.persistence.util.DatabaseConnectionTester;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/aggregator-test.xml")
public class DatabaseConnectionTesterTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testWrongDriverName() {
        DatabaseConnectionTester tester = new DatabaseConnectionTester(
                "somethingDefinitelyWrong",
                "", "", "");
        exit.expectSystemExitWithStatus(1);
        tester.failFastIfNoConnectionToDatabase();
    }

    @Test
    public void testNotDriver() {
        DatabaseConnectionTester tester = new DatabaseConnectionTester(
                "java.lang.Object",
                "", "", "");
        exit.expectSystemExitWithStatus(1);
        tester.failFastIfNoConnectionToDatabase();
    }

    @Test
    public void testWrongUrl() {
        DatabaseConnectionTester tester = new DatabaseConnectionTester(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:80/db", "123", "123");
        exit.expectSystemExitWithStatus(1);
        tester.failFastIfNoConnectionToDatabase();
    }

    @Test
    public void testGoodUrl() {
        DatabaseConnectionTester tester = new DatabaseConnectionTester(
                "org.h2.Driver",
                "jdbc:h2:mem:embeddedDb", "sa", "");
        tester.failFastIfNoConnectionToDatabase();
    }

}
