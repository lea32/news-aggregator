package ru.leasoft.challenge.aggregator.persistence.util;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

public class DatabaseConnectionTester {

    private String jdbcDriverClass;
    private String url;
    private String username;
    private String password;

    private static final int DB_TIMEOUT_SECONDS = 3;
    private static final int ATTEMPTS_ALLOWED = 3;
    private static final int PAUSE_BEFORE_NEXT_ATTEMPT_MILLIS = 5000;

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionTester.class);

    public DatabaseConnectionTester(String jdbcDriverClass, String url, String username, String password) {
        this.jdbcDriverClass = jdbcDriverClass;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void failFastIfNoConnectionToDatabase() {
        log.info("Checking database connectivity...");

        try {
            Driver driver = loadDriverOrDie();
            Connection conn = obtainConnectionOrDie(driver);
            if (!conn.isValid(DB_TIMEOUT_SECONDS)) {
                throw new Exception("Database returns invalid connections.");
            }

            conn.close();

            log.info("Connection established. Loading persistence layer...");

        } catch (Throwable anyError) {
            log.error(anyError.getMessage());
            System.exit(1); //stop propagating exception through all Spring ;)
        }
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

    private Connection obtainConnectionOrDie(Driver driver) {
        Properties connectionProps = new Properties();
        connectionProps.put("user", username);
        connectionProps.put("password", password);

        int attemptsMade = 0;
        while (true) {
            attemptsMade++;

            try {

                ConnectionCommand cc = new ConnectionCommand(driver, connectionProps);
                return cc.execute();

            } catch (Exception ex) {
                log.error(url);
                String message = (ex.getCause().getMessage() != null) ? ex.getCause().getMessage() : "timeout";
                log.error("Error connecting to database: " + message);
                if (attemptsMade >= ATTEMPTS_ALLOWED) break;
                log.error("Trying to reconnect...");
                try {
                    Thread.sleep(PAUSE_BEFORE_NEXT_ATTEMPT_MILLIS);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Checking database connectivity interrupted.");
                }
            }
        }

        throw new Error("Unable to establish a connection to database. Aborting.");
    }

    private class ConnectionCommand extends HystrixCommand<Connection> {

        private Driver driver;
        private Properties connectionProps;

        ConnectionCommand(Driver driver, Properties connectionProps) {
            super(Setter.withGroupKey(
                    HystrixCommandGroupKey.Factory.asKey("ConnectionCommand"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionTimeoutInMilliseconds(DB_TIMEOUT_SECONDS * 1000))
            );
            this.driver = driver;
            this.connectionProps = connectionProps;
        }

        @Override
        protected Connection run() throws Exception {
            return driver.connect(url, connectionProps);
        }
    }

}
