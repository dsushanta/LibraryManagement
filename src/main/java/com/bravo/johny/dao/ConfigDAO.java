package com.bravo.johny.dao;

import com.bravo.johny.dto.LibraryConfig;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigDAO extends BaseDAO {

    private final String TABLE_NAME = "LI_CONFIG";

    public ConfigDAO() {
        super();
    }

    public void udateConfigInDatabase(LibraryConfig config) {

        String query = "UPDATE "+TABLE_NAME+" SET NoOfDaysAUserCanKeepABook=? ," +
                "FinePerDay=?, NoOfBooksPerUser=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1, config.getNO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK());
            preparedStatement.setInt(2, config.getFINE_PER_DAY());
            preparedStatement.setInt(3, config.getNUMBER_OF_BOOKS_PER_USER());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbService.write(preparedStatement);
        closeDBConnection();
    }

    public LibraryConfig getConfigFromDatabase() {

        String query = "SELECT * FROM "+TABLE_NAME;
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        ResultSet rs = dbService.read(preparedStatement);

        LibraryConfig config = new LibraryConfig();
        try {
            while(rs.next()) {
                config.setNO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK(rs.getInt("NoOfDaysAUserCanKeepABook"));
                config.setFINE_PER_DAY(rs.getInt("FinePerDay"));
                config.setNUMBER_OF_BOOKS_PER_USER(rs.getInt("NoOfBooksPerUser"));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return config;
    }


}
