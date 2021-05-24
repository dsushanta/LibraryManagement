package com.bravo.johny.service;

import com.bravo.johny.dto.User;
import com.bravo.johny.entity.UserEntity;
import com.bravo.johny.repository.UserRepository;
import com.bravo.johny.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public boolean authenticateUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if(username == null)
            throwBadRequestRuntimeException("Value for Username field is either empty or null !!");
        if(password == null)
            throwBadRequestRuntimeException("Value for Password field is either empty or null !!");

        UserEntity userEntity = userRepository.findFirstByUserName(username);

        return CommonUtils.passwordEncoder().matches(password, userEntity.getPassword());
    }

    public User addNewUser(User user) {

        nullFieldValueCheck(user);

        boolean usernameExists = checkIfUsernameExistsInDatabase(user.getUsername());
        if(usernameExists)
            throwBadRequestRuntimeException("Username : "+user.getUsername()+" already exists !!");

        boolean emailExists = checkIfEmailExistsInDatabase(user.getEmail());
        if(emailExists)
            throwBadRequestRuntimeException("Email : "+user.getEmail()+" is already registered !!");

        UserEntity userEntity = prepareUserEntityFromUserDTO(user);
        UserEntity newUserEntity = userRepository.save(userEntity);

        return prepareUserDTOFromUserEntity(newUserEntity);
    }

    public List<User> getAllUsers(int offset, int limit) {

        List<UserEntity> userEntities;
        if (offset >= 0 && limit > 0) {
            Pageable pageable = PageRequest.of(offset, limit);
            userEntities = (List<UserEntity>) userRepository.findAll(pageable);
        } else
            userEntities = userRepository.findAll();

        List<User> users = new ArrayList<>();
        userEntities.forEach(userEntity -> users.add(prepareUserDTOFromUserEntity(userEntity)));

        return users;
    }

    public List<User> getUsers(String lastName, int offset, int limit) {

        List<UserEntity> userEntities;
        if (offset >= 0 && limit > 0) {
            Pageable pageable = PageRequest.of(offset, limit);
            userEntities = userRepository.findByLastName(lastName, pageable);
        } else
            userEntities = userRepository.findByLastName(lastName);

        List<User> users = new ArrayList<>();
        userEntities.forEach(userEntity -> users.add(prepareUserDTOFromUserEntity(userEntity)));

        return users;
    }

    public User getUserDetails(String username) {

        UserEntity userEntity = userRepository.findFirstByUserName(username);
        User user = prepareUserDTOFromUserEntity(userEntity);

        if(user.getUsername() == null)
            throwBadRequestRuntimeException("No user found with username : "+username);
        else
            return user;

        return null;
    }

    public User updateUserDetails(String username, User user) {

        UserEntity userEntity = userRepository.findFirstByUserName(username);

        if(userEntity == null)
            throwBadRequestRuntimeException("No user found with username : "+username);
        else {
            if(user.getFirstname() != null && !user.getFirstname().isEmpty())
                userEntity.setFirstName(user.getFirstname());
            if(user.getLastname() != null && !user.getLastname().isEmpty())
                userEntity.setLastName(user.getLastname());
            if(user.getEmail() != null && !user.getEmail().isEmpty())
                userEntity.setEmail(user.getEmail());
            if(user.getPassword() != null && !user.getPassword().isEmpty())
                userEntity.setPassword(user.getPassword());

            userRepository.save(userEntity);
        }
            return user;
    }

    public void deleteUser(String username) {

        UserEntity userEntity = userRepository.findFirstByUserName(username);

        if(userEntity.getUserName() == null)
            throwBadRequestRuntimeException("No user found with username : "+username);
        else
            userRepository.deleteByUserName(userEntity.getUserName());
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

    private boolean checkIfUsernameExistsInDatabase(String username) {

        UserEntity userEntity = userRepository.findFirstByUserName(username);

        if (userEntity != null)
            return true;
        else
            return false;
    }

    private boolean checkIfEmailExistsInDatabase(String email) {

        UserEntity userEntity = userRepository.findFirstByEmail(email);

        if (userEntity != null)
            return true;
        else
            return false;
    }

    private UserEntity prepareUserEntityFromUserDTO(User user) {

        return new UserEntity(
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                CommonUtils.passwordEncoder().encode(user.getPassword()),
                user.getFavGenre(),
                user.getFine(),
                user.getBookCount(),
                user.isActive(),
                user.getRole()
        );
    }

    private User prepareUserDTOFromUserEntity(UserEntity userEntity) {

        User user = new User();
        user.setUsername(userEntity.getUserName());
        user.setFirstname(userEntity.getFirstName());
        user.setLastname(userEntity.getLastName());
        user.setEmail(userEntity.getEmail());
        user.setFavGenre(userEntity.getFavouriteGenre());
        user.setBookCount(userEntity.getBookCount());
        user.setFine(userEntity.getFine());
        user.setActive(userEntity.isActive());
        user.setRole(userEntity.getRole());

        return user;
    }
}
