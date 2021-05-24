package com.bravo.johny.service1;

import com.bravo.johny.dao.UserDAO;
import com.bravo.johny.dto.User;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;


//@Service
public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public boolean authenticateUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if(username == null)
            throwBadRequestRuntimeException("Value for Username field is either empty or null !!");
        if(password == null)
            throwBadRequestRuntimeException("Value for Password field is either empty or null !!");

        return userDAO.authenticateUserFromDatabase(username, password);
    }

    public User addNewUser(User user) {
        nullFieldValueCheck(user);

        boolean usernameExists = userDAO.checkIfUsernameExistsInDatabase(user.getUsername());
        if(usernameExists)
            throwBadRequestRuntimeException("Username : "+user.getUsername()+" already exists !!");

        boolean emailExists = userDAO.checkIfEmailExistsInDatabase(user.getEmail());
        if(emailExists)
            throwBadRequestRuntimeException("Email : "+user.getEmail()+" is already registered !!");

        return userDAO.addNewUserIntoDatabase(user);
    }

    public List<User> getUsers(String apartmentNo, int offset, int limit) {
        return userDAO.getUsersWithLastnameFilterAndPaginatedFromDatabase(apartmentNo, offset, limit);
    }

    public User getUserDetails(String username) {
        User user = userDAO.getUsersWithUsernameFromDatabase(username);

        if(user.getUsername() == null)
            throwBadRequestRuntimeException("No user found with username : "+username);
        else
            return user;

        return null;
    }

    public User updateUserDetails(String username, User user) {
        User userToBeUpdated = userDAO.getUsersWithUsernameFromDatabase(username);

        if(userToBeUpdated.getUsername() == null)
            throwBadRequestRuntimeException("No user found with username : "+username);
        else {
            user.setUsername(username);
            user = userDAO.udateUserInDatabase(user);
        }
            return user;
    }

    public void deleteUser(String username) {

        User user = userDAO.getUsersWithUsernameFromDatabase(username);

        if(user.getUsername() == null)
            throwBadRequestRuntimeException("No user found with username : "+username);
        else
            userDAO.deleteUserFromDatabase(username);
    }

    // ##################### PRIVATE METHODS ######################

    private void nullFieldValueCheck(User user) {
        if(user.getUsername() == null || user.getUsername().isEmpty())
            throwBadRequestRuntimeException("Value for Username field is either empty or null !!");
        if(user.getEmail() == null || user.getEmail().isEmpty())
            throwBadRequestRuntimeException("Value for Email field is either empty or null !!");
        if(user.getFirstname() == null || user.getFirstname().isEmpty())
            throwBadRequestRuntimeException("Value for First name field is either empty or null !!");
        if(user.getLastname() == null || user.getLastname().isEmpty())
            throwBadRequestRuntimeException("Value for Last name field is either empty or null !!");
        if(user.getPassword() == null || user.getPassword().isEmpty())
            throwBadRequestRuntimeException("Value for Password field is either empty or null !!");

    }
}
