package com.learning.config;

import static com.learning.utils.CommonUtils.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.learning.dao.BaseDAO;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;

public class LoadEnvironment {

    public static Logger LOGGER;

    protected File databaseConfigFile;
    protected DatabaseConfig DATABASE_CONFIG = new DatabaseConfig();
    protected ObjectMapper mapper;

    public void load() {

        // Configure Log 4j
        LOGGER = Logger.getLogger(BaseDAO.class);
        PropertyConfigurator.configure(getResourceFullPath(this,
                ProjectConfig.LOG4J_PROPERTIES_FILE));

        // Read Database Connection details
        databaseConfigFile = getResourceAsFileObject(this,
                ProjectConfig.DATABASE_PROPERTIES_FILE);
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

    private void loadDBConfigurations() {

        DATABASE_CONFIG.setHost("localhost");
        DATABASE_CONFIG.setPort("3306");
        DATABASE_CONFIG.setDbuser("root");
        DATABASE_CONFIG.setDbpassword("trashbin");
        DATABASE_CONFIG.setDatabase("LIBRARY_MANAGEMENT");
    }
}
