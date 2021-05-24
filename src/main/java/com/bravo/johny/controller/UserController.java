package com.bravo.johny.controller;

import com.bravo.johny.controller.filterbeans.UserFilterBean;
import com.bravo.johny.controller.filterbeans.UserIssuedBookFilterBean;
import com.bravo.johny.dto.User;
import com.bravo.johny.dto.UserIssuedBook;
import com.bravo.johny.service.BookIssueService;
import com.bravo.johny.service.UserService;
import com.bravo.johny.utils.CommonUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BookIssueService bookIssueService;

    private Logger logger;

    public UserController() {
        logger = CommonUtils.getLoggerInstance(this.getClass());
    }

    @GetMapping
    public List<User> getUsers(UserFilterBean filterBean) {

        List<User> users;
        if(filterBean.getLastName() == null) {
            users = userService.getAllUsers(filterBean.getOffset(), filterBean.getLimit());
            logger.info("returning all the Users");
        } else {
            users = userService.getUsers(filterBean.getLastName(), filterBean.getOffset(), filterBean.getLimit());
            logger.info("returning all the Users with last name : "+filterBean.getLastName());
        }

        for(User user : users) {
            user.add(linkTo(methodOn(UserController.class).getUsers(filterBean)).slash(user.getUsername()).withSelfRel());
        }

        return users;
    }

    @GetMapping ("/{username}")
    public User getUserDetails(@PathVariable("username") String username) {
        User user = userService.getUserDetails(username);
        user.add(linkTo(methodOn(UserController.class).getUserDetails(username)).withSelfRel());

        return user;
    }

    @PutMapping("/{username}")
    public User updateUserDetails(@RequestBody User user,
                                  @PathVariable("username") String username) {

        user = userService.updateUserDetails(username, user);
        user.add(linkTo(methodOn(UserController.class).updateUserDetails(user, username)).withSelfRel());

        return user;
    }

    @PostMapping
    public Response addNewUser(@RequestBody User user) {

        User newUser = userService.addNewUser(user);
        WebMvcLinkBuilder userLink = linkTo(methodOn(UserController.class).addNewUser(user)).slash(newUser.getUsername());
        newUser.add(userLink.withSelfRel());
        Response response = Response.created(userLink.toUri())
                .entity(newUser)
                .build();

        return response;
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable("username") String username) {

        userService.deleteUser(username);
    }

    @GetMapping("/{username}/books")
    public List<UserIssuedBook> getUserIssuedBooks(@PathVariable("username") String username,
                                                   UserIssuedBookFilterBean bean) {

        return bookIssueService.getListOfBooksIssuedToAUser(username, bean);
    }
}
