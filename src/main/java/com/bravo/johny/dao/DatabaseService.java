package com.bravo.johny.dao;

import java.sql.*;

public class DatabaseService {

    private String dbHost, dbPort, dbUser, dbPassword, database;
    private Connection dbConnection;

    DatabaseService(String host, String port, String user, String password, String db) {
        this.dbHost = host;
        this.dbPort = port;
        this.dbUser = user;
        this.dbPassword = password;
        this.database = db;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + database , dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet read(String query) {
        ResultSet rs = null;
        try {
            Statement statement = dbConnection.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public ResultSet read(PreparedStatement preparedStatement) {

        ResultSet rs = null;
        try {
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public int write(PreparedStatement preparedStatement) {
        int status = 0;
        try {
            status = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

    public void closeConnection() {
        try {
            dbConnection.close();
            dbConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement getPreparedStatement(String query) {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dbConnection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return preparedStatement;
    }
}

