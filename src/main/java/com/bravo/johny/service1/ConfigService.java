package com.bravo.johny.service1;

import com.bravo.johny.dao.ConfigDAO;
import com.bravo.johny.dto.LibraryConfig;
import org.springframework.stereotype.Service;


//@Service
public class ConfigService {

    private ConfigDAO configDAO;

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
