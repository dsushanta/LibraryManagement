package com.bravo.johny.controller;

import com.bravo.johny.dto.IssuedBook;
import com.bravo.johny.service.BookIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/issuedbooks")
public class IssuedBooksController {

    @Autowired
    private BookIssueService bookIssueService;

    @GetMapping
    public List<IssuedBook> getAllIssuedBooks() {
        return bookIssueService.getListOfAllIssuedBooks();
    }
}
