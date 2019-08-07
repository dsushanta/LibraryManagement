package com.learning.service;

import com.learning.dao.UserDAO;
import com.learning.dto.User;
import com.learning.exception.DataNotFoundException;
import com.learning.exception.FieldValueRequiredException;
import com.learning.exception.ValueAlreadyExistsException;

import java.util.List;

public class UserService {

    UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public boolean authenticateUser(User user) {
        String username = user.getusername();
        String password = user.getPassword();
        if(username == null)
            throw new FieldValueRequiredException("Value for Username field is either empty or null !!");
        if(password == null)
            throw new FieldValueRequiredException("Value for Password field is either empty or null !!");

        return userDAO.authenticateUserFromDatabase(username, password);
    }

    public User addNewUser(User user) {
        nullFieldValueCheck(user);

        boolean usernameExists = userDAO.checkIfUsernameExistsInDatabase(user.getusername());
        if(usernameExists)
            throw new ValueAlreadyExistsException("Username : "+user.getusername()+" already exists !!");

        boolean emailExists = userDAO.checkIfEmailExistsInDatabase(user.getemail());
        if(emailExists)
            throw new ValueAlreadyExistsException("Email : "+user.getemail()+" is already registered !!");

        return userDAO.addNewUserIntoDatabase(user);
    }

    public List<User> getUsers(String apartmentNo, int offset, int limit) {
        return userDAO.getUsersWithLastnameFilterAndPaginatedFromDatabase(apartmentNo, offset, limit);
    }

    public User getUserDetails(String username) {
        User user = userDAO.getUsersWithUsernameFromDatabase(username);

        if(user.getusername() == null)
            throw new DataNotFoundException("No user found with username : "+username);
        else
            return user;
    }

    public User updateUserDetails(String username, User user) {
        User userToBeUpdated = userDAO.getUsersWithUsernameFromDatabase(username);

        if(userToBeUpdated.getusername() == null)
            throw new DataNotFoundException("No user found with username : "+username);
        else {
            user.setusername(username);
            user = userDAO.udateUserInDatabase(user);
        }
            return user;
    }

    public void deleteUser(String username) {

        User user = userDAO.getUsersWithUsernameFromDatabase(username);

        if(user.getusername() == null)
            throw new DataNotFoundException("No user found with username : "+username);
        else
            userDAO.deleteUserFromDatabase(username);
    }

    // ##################### PRIVATE METHODS ######################

    private void nullFieldValueCheck(User user) {
        if(user.getusername() == null || user.getusername().isEmpty())
            throw new FieldValueRequiredException("Value for Username field is either empty or null !!");
        if(user.getemail() == null || user.getemail().isEmpty())
            throw new FieldValueRequiredException("Value for Email field is either empty or null !!");
        if(user.getfirstname() == null || user.getfirstname().isEmpty())
            throw new FieldValueRequiredException("Value for First name field is either empty or null !!");
        if(user.getlastname() == null || user.getlastname().isEmpty())
            throw new FieldValueRequiredException("Value for Last name field is either empty or null !!");
        if(user.getPassword() == null || user.getPassword().isEmpty())
            throw new FieldValueRequiredException("Value for Password field is either empty or null !!");

    }
}
