package com.bravo.johny.dao1;

import com.bravo.johny.dto.User;
import com.bravo.johny.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends BaseDAO {

    @Autowired
    private UserRepository repository;

    private final String TABLE_NAME = "LI_USERS";

    public UserDAO() {
        super();
    }

    public User addNewUserIntoDatabase(User user) {
        //repository.save(user);
        User newUser = getUsersWithUsernameFromDatabase(user.getUsername());

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
        User userToBeUpdated = getUsersWithUsernameFromDatabase(user.getUsername());
        if(user.getFirstname() != null && !user.getFirstname().isEmpty())
            userToBeUpdated.setFirstname(user.getFirstname());
        if(user.getLastname() != null && !user.getLastname().isEmpty())
            userToBeUpdated.setLastname(user.getLastname());
        if(user.getEmail() != null && !user.getEmail().isEmpty())
            userToBeUpdated.setEmail(user.getEmail());
        if(user.getPassword() != null && !user.getPassword().isEmpty())
            userToBeUpdated.setPassword(user.getPassword());

        String query = "UPDATE "+TABLE_NAME+" SET FirstName=?, LastName=?, Email=?, Pwd=SHA1(?) WHERE UserName=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,userToBeUpdated.getFirstname());
            preparedStatement.setString(2,userToBeUpdated.getLastname());
            preparedStatement.setString(3,userToBeUpdated.getEmail());
            preparedStatement.setString(4,userToBeUpdated.getPassword());
            preparedStatement.setString(5,userToBeUpdated.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbService.write(preparedStatement);

        user = getUsersWithUsernameFromDatabase(userToBeUpdated.getUsername());

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
            user.setUsername(rs.getString("UserName"));
            user.setFirstname(rs.getString("FirstName"));
            user.setLastname(rs.getString("LastName"));
            user.setEmail(rs.getString("Email"));
            user.setFavGenre(rs.getString("FavGenre"));
            user.setBookCount(rs.getInt("BookCount"));
            user.setFine(rs.getInt("Fine"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
