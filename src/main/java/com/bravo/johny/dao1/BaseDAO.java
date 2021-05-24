package com.bravo.johny.dao1;


import com.bravo.johny.config.LoadEnvironment;

public class BaseDAO extends LoadEnvironment {

    DatabaseService dbService;
    boolean propertiesLoaded = false;

    public BaseDAO() {
        if(!propertiesLoaded)
            loadProperties();
    }

    private void loadProperties() {
        load();
        propertiesLoaded = true;
    }

    protected DatabaseService getDBService() {
        if(dbService != null)
            return dbService;
        else
            return new DatabaseService(
                DATABASE_CONFIG.getHost(),
                DATABASE_CONFIG.getPort(),
                DATABASE_CONFIG.getDbuser(),
                DATABASE_CONFIG.getDbpassword(),
                DATABASE_CONFIG.getDatabase());
    }

    protected void closeDBConnection() {
        dbService.closeConnection();
        dbService = null;
    }
}
