package com.learning.service;

import com.learning.dao.BookDAO;
import com.learning.dao.BookIssueDAO;
import com.learning.dao.CopyDAO;
import com.learning.dao.UserDAO;
import com.learning.dto.BookIssue;
import com.learning.dto.Copy;
import com.learning.dto.UserIssuedBook;
import com.learning.exception.DataNotFoundException;
import com.learning.exception.FieldValueRequiredException;
import com.learning.webresource.filterbeans.BookIssueFilterBean;
import com.learning.webresource.filterbeans.UserIssuedBookFilterBean;

import java.util.List;

public class BookIssueService {
    CopyDAO copyDAO;
    BookDAO bookDAO;
    BookIssueDAO bookIssueDAO;
    UserDAO userDAO;

    public BookIssueService() {
        copyDAO = new CopyDAO();
        bookDAO = new BookDAO();
        bookIssueDAO = new BookIssueDAO();
        userDAO = new UserDAO();
    }

    public BookIssue issueABook(BookIssue bookIssue) {
        nullFieldValueCheck(bookIssue);

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throw new DataNotFoundException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkBookId = bookDAO.checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throw new DataNotFoundException("Book with book-id : "+bookIssue.getBookId()+" does not exist");

        int copyId = isBookAvailable(bookIssue.getBookId());
        if(copyId == 0)
            throw new DataNotFoundException("Sorry, all copies of book with book-id : "+bookIssue.getBookId()+" have been issued");

        bookIssue.setCopyId(copyId);
        bookIssueDAO.clearAllDuesOfAUser(bookIssue.getUserName());
        BookIssue newBookIssue = bookIssueDAO.issueABook(bookIssue);
        Copy copy = new Copy();
        copy.setCopyId(copyId);
        copy.setIssued(true);
        copyDAO.udateCopyInDatabase(copy);

        copyId = isBookAvailable(bookIssue.getBookId());
        if(copyId == 0)
            bookDAO.updateBookAvailabilityStatus(bookIssue.getBookId(), false);

        return newBookIssue;
    }

    public BookIssue reIssueABook(BookIssue bookIssue) {
        boolean checkBookIssueId = bookIssueDAO.checkIfBookIssueEntryExists(bookIssue.getBookIssueId());
        if(!checkBookIssueId)
            throw new DataNotFoundException("Book issue entry : "+bookIssue.getBookIssueId()+" does not exist");

        boolean checkBookId = bookDAO.checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throw new DataNotFoundException("Book with book-id : "+bookIssue.getBookId()+" does not exist");

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throw new DataNotFoundException("Username : "+bookIssue.getUserName()+" does not exist");

        bookIssueDAO.clearAllDuesOfAUser(bookIssue.getUserName());
        bookIssue = bookIssueDAO.reissueABook(bookIssue);

        return bookIssue;
    }

    public BookIssue returnABook(BookIssue bookIssue) {
        boolean checkBookIssueId = bookIssueDAO.checkIfBookIssueEntryExists(bookIssue.getBookIssueId());
        if(!checkBookIssueId)
            throw new DataNotFoundException("Book issue entry : "+bookIssue.getBookIssueId()+" does not exist");

        boolean checkBookId = bookDAO.checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throw new DataNotFoundException("Book with book-id : "+bookIssue.getBookId()+" does not exist");

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throw new DataNotFoundException("Username : "+bookIssue.getUserName()+" does not exist");

        bookIssue = bookIssueDAO.returnABook(bookIssue);

        Copy copy = new Copy();
        copy.setCopyId(bookIssueDAO.getBookIssueDetailsFromDatabase(bookIssue.getBookIssueId()).getCopyId());
        copy.setIssued(false);
        copyDAO.udateCopyInDatabase(copy);

        bookDAO.updateBookAvailabilityStatus(bookIssue.getBookId(), true);

        return bookIssue;
    }

    public int isBookAvailable(int bookId) {
        return copyDAO.getAvailableCopyOfABook(bookId);
    }

    public void removeBookIssueEntryFromDatabase(int bookIssueId) {
        BookIssue bookIssue = bookIssueDAO.getBookIssueDetailsFromDatabase(bookIssueId);

        if(bookIssue.getBookIssueId() == 0)
            throw new DataNotFoundException("No Book Issue entry found with book-issue-id : "+bookIssueId);
        else
            bookIssueDAO.deleteBookIssueFromDatabase(bookIssueId);
    }

    public List<BookIssue> getBookIssueEntries(BookIssueFilterBean bookIssueFilterBean) {
        return bookIssueDAO.getBookIssueEntriesWithFilterFromDatabase(bookIssueFilterBean);
    }

    public BookIssue getBookIssueDetails(int bookIssueId) {
        BookIssue bookIssue = bookIssueDAO.getBookIssueDetailsFromDatabase(bookIssueId);

        if(bookIssue.getBookIssueId() == 0)
            throw new DataNotFoundException("No book issue entry found with book-issue-id : "+bookIssueId);
        else
            return bookIssue;
    }

    public List<UserIssuedBook> getListOfBooksIssuedToAUser(String username, UserIssuedBookFilterBean bean) {

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(username);
        if(!checkUsername)
            throw new DataNotFoundException("Username : "+username+" does not exist");

        return bookIssueDAO.getBooksIssuedByAUser(username, bean);
    }

    // ##################### PRIVATE METHODS ######################

    private void nullFieldValueCheck(BookIssue bookIssue) {
        if(bookIssue.getCopyId() == 0 )
            throw new FieldValueRequiredException("Value for Copy Id field is either empty or null !!");
        if(bookIssue.getBookId() == 0 )
            throw new FieldValueRequiredException("Value for Book Id field is either empty or null !!");
        if(bookIssue.getUserName() == null || bookIssue.getUserName().isEmpty())
            throw new FieldValueRequiredException("Value for Username field is either empty or null !!");
    }
}