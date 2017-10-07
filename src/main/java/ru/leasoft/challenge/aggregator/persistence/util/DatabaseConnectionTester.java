package ru.leasoft.challenge.aggregator.persistence.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Driver;

public class DatabaseConnectionTester {

    private String jdbcDriverClass;
    private String url;
    private String username;
    private String password;

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionTester.class);

    public DatabaseConnectionTester(String jdbcDriverClass, String url, String username, String password) {
        this.jdbcDriverClass = jdbcDriverClass;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void failFastIfNoConnectionToDatabase() {
        log.info("Checking database connectivity...");

        Driver driver = loadDriverOrDie();



    }

    private Driver loadDriverOrDie() {
        try {
            return (Driver) Class.forName(jdbcDriverClass).newInstance();
        } catch (ClassNotFoundException nfEx) {
            log.error("Driver class " + jdbcDriverClass + " not found");
            throw new Error(nfEx);
        } catch (ClassCastException castEx) {
            log.error("Not a driver class " + jdbcDriverClass);
            throw new Error(castEx);
        } catch (IllegalAccessException | InstantiationException ex) {
            log.error("Error while instantiating jdbc driver", ex);
            throw new Error(ex);
        }
    }

}
