package ru.leasoft.challenge.aggregator.container.configuration.utils;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class StartupConfigLocator {

    private static final String DEFAULT_CONFIG_LOCATION = "../conf/aggregator";
    private static final String CONFIG_PROPERTY = "aggregator.config";

    private static final Logger log = LoggerFactory.getLogger(StartupConfigLocator.class);

    /**
     * Search for configuration file in following locations in particular order:
     * <ol>
     *     <li>first command-line argument</li>
     *     <li>aggregator.config property</li>
     *     <li>default config location [dist. folder]/conf/aggregator</li>
     * </ol>
     * @param args main() function arguments
     * @return Existing file
     * @throws Error when file not found in neither location
     * */
    public static File obtainConfigFile(String[] args) {
        File result = parseFirstArg(args);
        if (result != null) {
            return result;
        }

        result = parseProperty();
        if (result != null) {
            return result;
        }

        result = searchInDefaultLocation();
        if (result != null) {
            return result;
        }

        log.error("Config file not found!");
        throw new Error();
    }

    private static File parseFirstArg(String[] args) {
        if (args.length > 0) {
            File configFile = asFile(args[0]);
            if (isAccessible(configFile)) return configFile;
        }

        return null;
    }

    private static File parseProperty() {
        String value = System.getProperty(CONFIG_PROPERTY);
        if (value != null) {
            File configFile = asFile(value);
            if (isAccessible(configFile)) return configFile;
        }

        return null;
    }

    private static File searchInDefaultLocation() {
        File configFile = asFile(DEFAULT_CONFIG_LOCATION);
        return isAccessible(configFile) ? configFile : null;
    }

    private static File asFile(String path) {
        String cleanPath = FilenameUtils.separatorsToSystem(path);
        return new File(cleanPath);
    }

    private static boolean isAccessible(File file) {
        try {
            return file.exists() && !file.isDirectory() && file.canRead();
        } catch (SecurityException ex) { return false;}
    }

}
