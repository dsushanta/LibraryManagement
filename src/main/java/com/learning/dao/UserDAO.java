package com.learning.dao;

import com.learning.dto.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BaseDAO {
    
    private final String TABLE_NAME = "LI_USERS";

    public UserDAO() {
        super();
    }

    public User addNewUserIntoDatabase(User user) {
        String query = "INSERT INTO "+TABLE_NAME+"(UserName, FirstName, LastName, Email, Pwd, FavGenre)"+
                " VALUES (?, ?, ?, ?, SHA1(?), ?)";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1, user.getusername());
            preparedStatement.setString(2, user.getfirstname());
            preparedStatement.setString(3, user.getlastname());
            preparedStatement.setString(4, user.getemail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setString(6, user.getFavGenre());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dbService.write(preparedStatement);
        User newUser = getUsersWithUsernameFromDatabase(user.getusername());

        return newUser;
    }

    public int deleteUserFromDatabase(String username) {
        int status;
        String query = "DELETE FROM "+TABLE_NAME+" WHERE UserName=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1, username);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        status = dbService.write(preparedStatement);
        closeDBConnection();

        return status;
    }

    public User getUsersWithUsernameFromDatabase(String username) {
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE UserName=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        User user = new User();

        try {
            while(rs.next())
               setUserObject(rs, user);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return user;
    }

    public User udateUserInDatabase(User user) {
        User userToBeUpdated = getUsersWithUsernameFromDatabase(user.getusername());
        if(user.getfirstname() != null && !user.getfirstname().isEmpty())
            userToBeUpdated.setfirstname(user.getfirstname());
        if(user.getlastname() != null && !user.getlastname().isEmpty())
            userToBeUpdated.setlastname(user.getlastname());
        if(user.getemail() != null && !user.getemail().isEmpty())
            userToBeUpdated.setemail(user.getemail());
        if(user.getPassword() != null && !user.getPassword().isEmpty())
            userToBeUpdated.setPassword(user.getPassword());

        String query = "UPDATE "+TABLE_NAME+" SET FirstName=?, LastName=?, Email=?, Pwd=SHA1(?) WHERE UserName=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,userToBeUpdated.getfirstname());
            preparedStatement.setString(2,userToBeUpdated.getlastname());
            preparedStatement.setString(3,userToBeUpdated.getemail());
            preparedStatement.setString(4,userToBeUpdated.getPassword());
            preparedStatement.setString(5,userToBeUpdated.getusername());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbService.write(preparedStatement);

        user = getUsersWithUsernameFromDatabase(userToBeUpdated.getusername());

        return user;
    }

    public List<User> getUsersWithLastnameFilterAndPaginatedFromDatabase(String lastName, int offset, int limit) {
        List<User> allUsers = new ArrayList<>();
        String query;
        int category;

        if(lastName == null) {
            if(offset >=0 && limit >0) {
                query = "SELECT * FROM "+TABLE_NAME+" LIMIT ?,?";
                category = 2;
            }
            else {
                query = "SELECT * FROM "+TABLE_NAME+"";
                category = 4;
            }
        } else {
            if(offset >=0 && limit >0) {
                query = "SELECT * FROM "+TABLE_NAME+" WHERE LastName=? LIMIT ?,?";
                category = 3;
            }
            else {
                query = "SELECT * FROM "+TABLE_NAME+" WHERE LastName=?";
                category = 1;
            }
        }
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        if(category == 1) {
            try {
                preparedStatement.setString(1,lastName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(category == 2) {
            try {
                preparedStatement.setInt(1,offset);
                preparedStatement.setInt(2,limit);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(category == 3) {
            try {
                preparedStatement.setString(1,lastName);
                preparedStatement.setInt(2,offset);
                preparedStatement.setInt(3,limit);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        ResultSet rs = dbService.read(preparedStatement);

        try {
            while(rs.next()) {
                User user = new User();
                setUserObject(rs, user);

                allUsers.add(user);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return allUsers;
    }

    public boolean authenticateUserFromDatabase(String username, String password) {

        boolean authenticationStatus = false;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE UserName=? AND Pwd=SHA1(?)";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            if(rs.next())
               authenticationStatus = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return authenticationStatus;
    }

    public boolean checkIfUsernameExistsInDatabase(String username) {

        boolean valueExists = false;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE UserName=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            if(rs.next())
                valueExists = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return valueExists;
    }

    public boolean checkIfEmailExistsInDatabase(String email) {

        boolean valueExists = false;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE Email=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,email);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            if(rs.next())
                valueExists = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return valueExists;
    }

    public int getBookCountForAUser(String username) {
        int bookCount = 0;
        String query = "SELECT BookCount FROM "+TABLE_NAME+" WHERE UserName=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);

        try {
            while(rs.next())
                bookCount = rs.getInt("BookCount");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return bookCount;
    }

    public void updateBookCountForAUser(String username, int count) {
        String query = "UPDATE "+TABLE_NAME+" SET BookCount=? WHERE UserName=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1, count);
            preparedStatement.setString(2, username);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dbService.write(preparedStatement);
        closeDBConnection();
    }

    public void updateUserDues(String userName, int userdue) {
        String query = "UPDATE "+TABLE_NAME+" SET Fine=? WHERE UserName=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1, userdue);
            preparedStatement.setString(2, userName);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dbService.write(preparedStatement);
        closeDBConnection();
    }

    // ##################### PRIVATE METHODS ######################

    private void setUserObject(ResultSet rs, User user) {
        try {
            user.setusername(rs.getString("UserName"));
            user.setfirstname(rs.getString("FirstName"));
            user.setlastname(rs.getString("LastName"));
            user.setemail(rs.getString("Email"));
            user.setFavGenre(rs.getString("FavGenre"));
            user.setBookCount(rs.getInt("BookCount"));
            user.setFine(rs.getInt("Fine"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
