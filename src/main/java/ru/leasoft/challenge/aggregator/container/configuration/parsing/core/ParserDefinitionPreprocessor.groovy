package ru.leasoft.challenge.aggregator.container.configuration.parsing.core

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.leasoft.challenge.aggregator.container.configuration.Configuration
import ru.leasoft.challenge.aggregator.container.configuration.parsing.structures.ParsersConfigStruct

class ParserDefinitionPreprocessor {

    private static final Logger log = LoggerFactory.getLogger(Configuration.class)

    static void appendParser(ParsersConfigStruct pcs, String name, String location) {
        def parsers = pcs.parsers;

        if (parsers.containsKey(name)) throw new ScriptProcessingException("Duplicate entry for script name $name")

        try {
            String code = readScriptCode(name, location, pcs.parsersDirRelativePath)
            parsers.put(name, code)

        } catch (WrongParserFileException wpEx) {
            log.warn(wpEx.getMessage())
        } catch (IOException ioEx) {
            log.warn("Failed to load $name parser: " + ioEx.getMessage())
        }
    }

    private static String readScriptCode(name, location, relativePath) {
        def parserPath = "$relativePath/$location"
        def cleanParserPath = FilenameUtils.separatorsToSystem(parserPath)

        log.info("Loading parser $name from $cleanParserPath")

        def file = new File(cleanParserPath)
        if (!isAccessible(file)) {
            String abs = file.absolutePath;
            throw new WrongParserFileException("File can't be read: $abs \n Check ");
        }

        return FileUtils.readFileToString(file)
    }

    private static boolean isAccessible(File file) {
        try {
            return file.exists() && !file.isDirectory() && file.canRead();
        } catch (SecurityException ex) { return false;}
    }

}
