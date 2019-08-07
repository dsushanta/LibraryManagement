package com.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;

public class LoadEnvironment {

    public static Logger LOGGER;

    protected File databaseConfigFile;
    protected DatabaseConfig DATABASE_CONFIG;
    protected ObjectMapper mapper;

    public void load() {

        // Configure Log 4j
        System.setProperty("user.dir", ProjectConfig.PROJECT_DIRECTORY);
        LOGGER = Logger.getLogger(ProjectConfig.LOG4J_FILE_REFERENCE);
        PropertyConfigurator.configure(System.getProperty("user.dir")+ "/" +ProjectConfig.LOG4J_PROPERTIES_FILE);

        // Read Database Connection details
        databaseConfigFile = new File(System.getProperty("user.dir")+ "/src/main/resources/"+ ProjectConfig.DATABASE_PROPERTIES_FILE);
        mapper = new ObjectMapper(new YAMLFactory());
        try {
            DATABASE_CONFIG = mapper.readValue(databaseConfigFile, DatabaseConfig.class);
        } catch (JsonParseException e) {
            LOGGER.error(e.getStackTrace());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            LOGGER.error(e.getStackTrace());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("File : "+databaseConfigFile+" not found");
        }
    }
}
