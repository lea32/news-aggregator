package ru.leasoft.challenge.aggregator.persistence.util;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.internal.info.MigrationInfoDumper;
import org.flywaydb.core.internal.util.jdbc.DriverDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leasoft.challenge.aggregator.container.configuration.Configuration;

import javax.sql.DataSource;

public class DatabaseVersioningUtil {

    private Flyway flyway;
    private static final Logger log = LoggerFactory.getLogger(DatabaseVersioningUtil.class);
    private static final String BASELINE_VERSION = "1.0.0";

    public DatabaseVersioningUtil() {
        DataSource dataSource = prepareDatasource();

        flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineVersionAsString(BASELINE_VERSION);
        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("classpath:/db");
    }

    private DataSource prepareDatasource() {
        Configuration config = Configuration.getInstance();

        return new DriverDataSource(
                DatabaseVersioningUtil.class.getClassLoader(),
                "com.mysql.jdbc.Driver",
                config.getDatabaseUrl(),
                config.getDatabaseUser(),
                config.getDatabasePassword(),
                "SELECT 1"
        );
    }

    public void validateAndMigrate() {
        try {
            flyway.validate();
            log.info("Database schema validated");
        } catch (FlywayException fex) {
            log.info("Database schema is outdated and must be migrated...");
            migration();
            log.info("DB migration committed: ");
            MigrationInfoService info = flyway.info();
            log.info('\n' + "===== DATABASE MIGRATIONS =====" + '\n' + MigrationInfoDumper.dumpToAsciiTable(info.all()));
        }
    }

    private void migration() {
        try {
            flyway.migrate();
        } catch (FlywayException fex) {
            try {
                flyway.repair();
                flyway.migrate();
            } catch (FlywayException againFex) {
                log.error("DB migration error");
                fex.printStackTrace();
                System.exit(1); // better than pass Error through Spring
            }
        }
    }
}


