package com.bravo.johny.controller;

import com.bravo.johny.controller.filterbeans.BookIssueFilterBean;
import com.bravo.johny.dto.BookIssue;
import com.bravo.johny.dto.BookLifeCycleOperation;
import com.bravo.johny.service.BookIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/books/{bookId}/bookissue")
public class BookIssueController {

    @Autowired
    private BookIssueService bookIssueService;

    @GetMapping
    public List<BookIssue> getBookIssueEntries(@PathVariable("bookId") int bookId,
                                               BookIssueFilterBean bookIssueFilterBean) {

        bookIssueFilterBean.setBookId(bookId);
        List<BookIssue> bookIssues = bookIssueService.getBookIssueEntries(bookIssueFilterBean);

        for(BookIssue bookIssue : bookIssues) {
            bookIssue.add(linkTo(UserController.class).slash(bookIssue.getUserName()).withRel("UserEntity"));
        }
        return bookIssues;
    }

    @GetMapping("/{bookIssueId}")
    public BookIssue getBookIssueDetails(@PathVariable("bookId") int bookId,
                                         @PathVariable("bookIssueId") int bookIssueId) {

        BookIssue bookIssue = bookIssueService.getBookIssueDetails(bookIssueId);
        if(bookId != bookIssue.getBookId())
            throwBadRequestRuntimeException("BookEntity Id belonging to the book issue id is different from the book id present in the URL");
        bookIssue.add(linkTo(UserController.class).slash(bookIssue.getUserName()).withRel("UserEntity"));

        return bookIssue;
    }

    @PostMapping
    public Response issueACopyOfABook(@PathVariable("bookId") int bookId,
                                      @RequestBody BookIssue bookIssue) {

        bookIssue.setBookId(bookId);
        bookIssue = bookIssueService.issueABook(bookIssue);
        WebMvcLinkBuilder bookIssueLink = linkTo(UserController.class).slash(bookIssue.getUserName());
        bookIssue.add(bookIssueLink.withRel("UserEntity"));
        Response response = Response.created(bookIssueLink.toUri())
                .entity(bookIssue)
                .build();

        return response;
    }

    @PatchMapping("/{bookIssueId}")
    public BookIssue updateBookIssueDetails(@PathVariable("bookId") int bookId,
                                            @PathVariable("bookIssueId") int bookIssueId,
                                            @RequestBody BookIssue bookIssue) {

        bookIssue.setBookId(bookId);
        bookIssue.setBookIssueId(bookIssueId);
        BookLifeCycleOperation operation = bookIssue.getOperation();
        if(operation.equals(BookLifeCycleOperation.REISSUE)) {
            bookIssue = bookIssueService.reIssueABook(bookIssue);
        } else if(operation.equals(BookLifeCycleOperation.RETURN)) {
            bookIssue = bookIssueService.returnABook(bookIssue);
        } else
            throwBadRequestRuntimeException("BookEntity Life Cycle operation : "+operation.getOperation()+" is not a valid operation");

        bookIssue.add(linkTo(UserController.class).slash(bookIssue.getUserName()).withRel("UserEntity"));

        return bookIssue;
    }

    @DeleteMapping("/{bookIssueId}")
    public void deleteBookIssueEntry(@PathVariable("bookIssueId") int bookIssueId) {

        bookIssueService.removeBookIssueEntryFromDatabase(bookIssueId);
    }
}
