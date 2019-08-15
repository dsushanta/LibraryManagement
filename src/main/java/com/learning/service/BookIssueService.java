package com.learning.service;

import com.learning.dao.*;
import com.learning.dto.Book;
import com.learning.dto.BookIssue;
import com.learning.dto.Copy;
import com.learning.dto.UserIssuedBook;
import com.learning.exception.DataNotFoundException;
import com.learning.exception.FieldValueRequiredException;
import com.learning.exception.ValueAlreadyExistsException;
import com.learning.webresource.filterbeans.BookIssueFilterBean;
import com.learning.webresource.filterbeans.UserIssuedBookFilterBean;

import java.util.List;

public class BookIssueService {
    CopyDAO copyDAO;
    BookDAO bookDAO;
    BookIssueDAO bookIssueDAO;
    UserDAO userDAO;
    ConfigDAO configDAO;

    public BookIssueService() {
        copyDAO = new CopyDAO();
        bookDAO = new BookDAO();
        bookIssueDAO = new BookIssueDAO();
        userDAO = new UserDAO();
        configDAO = new ConfigDAO();
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

        int bookCount = userDAO.getBookCountForAUser(bookIssue.getUserName());
        if(bookCount >= configDAO.getConfigFromDatabase().getNUMBER_OF_BOOKS_PER_USER())
            throw new ValueAlreadyExistsException("Sorry, You have already exceeded the maximum number of books a user can keep, please return a book to issue a new one !!");

        bookIssue.setCopyId(copyId);
        bookIssueDAO.clearAllDuesOfAUser(bookIssue.getUserName());
        userDAO.updateUserDues(bookIssue.getUserName(), 0);
        BookIssue newBookIssue = bookIssueDAO.issueABook(bookIssue);
        Copy copy = new Copy();
        copy.setCopyId(copyId);
        copy.setIssued(true);
        copyDAO.udateCopyInDatabase(copy);
        userDAO.updateBookCountForAUser(bookIssue.getUserName(), bookCount+1);

        copyId = isBookAvailable(bookIssue.getBookId());
        if(copyId == 0)
            bookDAO.updateBookAvailabilityStatus(bookIssue.getBookId(), false);

        return newBookIssue;
    }

    public BookIssue reIssueABook(BookIssue bookIssue) {
        int bookIssueId = bookIssue.getBookIssueId();
        BookIssue bookIssue1 = bookIssueDAO.getBookIssueDetailsFromDatabase(bookIssueId);
        bookIssue.setUserName(bookIssue1.getUserName());
        bookIssue.setExpectedReturnDate(bookIssue1.getExpectedReturnDate());
        boolean checkBookIssueId = bookIssueDAO.checkIfBookIssueEntryExists(bookIssueId);
        if(!checkBookIssueId)
            throw new DataNotFoundException("Book issue entry : "+bookIssueId+" does not exist");

        boolean checkBookId = bookDAO.checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throw new DataNotFoundException("Book with book-id : "+bookIssue.getBookId()+" does not exist");

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throw new DataNotFoundException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkReIssueStatus = bookIssueDAO.checkIfBookIsAlreadyReIssued(bookIssueId);
        if(checkReIssueStatus)
            throw new ValueAlreadyExistsException("Book is already re-issued");

//        bookIssueDAO.clearAllDuesOfAUser(bookIssue.getUserName());
        bookIssue = bookIssueDAO.reissueABook(bookIssue);

        int userdue = bookIssueDAO.getTotalDuesOfAUser(bookIssue.getUserName());
        userDAO.updateUserDues(bookIssue.getUserName(), userdue);

        return bookIssue;
    }

    public BookIssue returnABook(BookIssue bookIssue) {
        int bookIssueId = bookIssue.getBookIssueId();
        BookIssue bookIssue1 = bookIssueDAO.getBookIssueDetailsFromDatabase(bookIssueId);
        bookIssue.setUserName(bookIssue1.getUserName());
        bookIssue.setExpectedReturnDate(bookIssue1.getExpectedReturnDate());
        boolean checkBookIssueId = bookIssueDAO.checkIfBookIssueEntryExists(bookIssueId);
        if(!checkBookIssueId)
            throw new DataNotFoundException("Book issue entry : "+bookIssue.getBookIssueId()+" does not exist");

        boolean checkBookId = bookDAO.checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throw new DataNotFoundException("Book with book-id : "+bookIssue.getBookId()+" does not exist");

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throw new DataNotFoundException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkReturnStatus = bookIssueDAO.checkIfBookIsAlreadyReturned(bookIssueId);
        if(checkReturnStatus)
            throw new ValueAlreadyExistsException("Book is already returned");

        bookIssue = bookIssueDAO.returnABook(bookIssue);

        Copy copy = new Copy();
        copy.setCopyId(bookIssueDAO.getBookIssueDetailsFromDatabase(bookIssue.getBookIssueId()).getCopyId());
        copy.setIssued(false);
        copyDAO.udateCopyInDatabase(copy);
        bookDAO.updateBookAvailabilityStatus(bookIssue.getBookId(), true);
        int bookCount = userDAO.getBookCountForAUser(bookIssue.getUserName());
        userDAO.updateBookCountForAUser(bookIssue.getUserName(), bookCount-1);
        int userdue = bookIssueDAO.getTotalDuesOfAUser(bookIssue.getUserName());
        userDAO.updateUserDues(bookIssue.getUserName(), userdue);

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
        if(bookIssue.getBookId() == 0 )
            throw new FieldValueRequiredException("Value for Book Id field is either empty or null !!");
        if(bookIssue.getUserName() == null || bookIssue.getUserName().isEmpty())
            throw new FieldValueRequiredException("Value for Username field is either empty or null !!");
    }
}