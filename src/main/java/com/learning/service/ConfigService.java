package com.learning.service;

import com.learning.dao.ConfigDAO;
import com.learning.dto.LibraryConfig;

public class ConfigService {

    ConfigDAO configDAO;

    public ConfigService() {
        configDAO = new ConfigDAO();
    }

    public LibraryConfig getLibraryConfigurations() {
        return configDAO.getConfigFromDatabase();
    }

    public void updateLibraryConfigurations(LibraryConfig config) {
        configDAO.udateConfigInDatabase(config);
    }
}
