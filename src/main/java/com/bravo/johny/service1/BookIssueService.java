package com.bravo.johny.service1;

import com.bravo.johny.controller.filterbeans.BookIssueFilterBean;
import com.bravo.johny.controller.filterbeans.UserIssuedBookFilterBean;
import com.bravo.johny.dao.*;
import com.bravo.johny.dto.BookIssue;
import com.bravo.johny.dto.Copy;
import com.bravo.johny.dto.IssuedBook;
import com.bravo.johny.dto.UserIssuedBook;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;


//@Service
public class BookIssueService {

    private CopyDAO copyDAO;
    private BookDAO bookDAO;
    private BookIssueDAO bookIssueDAO;
    private UserDAO userDAO;
    private ConfigDAO configDAO;

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
            throwBadRequestRuntimeException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkBookId = bookDAO.checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throwBadRequestRuntimeException("BookEntity with book-id : "+bookIssue.getBookId()+" does not exist");

        int copyId = isBookAvailable(bookIssue.getBookId());
        if(copyId == 0)
            throwBadRequestRuntimeException("Sorry, all copies of book with book-id : "+bookIssue.getBookId()+" have been issued");

        int bookCount = userDAO.getBookCountForAUser(bookIssue.getUserName());
        if(bookCount >= configDAO.getConfigFromDatabase().getNUMBER_OF_BOOKS_PER_USER())
            throwBadRequestRuntimeException("Sorry, You have already exceeded the maximum number of books a user can keep, please return a book to issue a new one !!");

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
            throwBadRequestRuntimeException("BookEntity issue entry : "+bookIssueId+" does not exist");

        boolean checkBookId = bookDAO.checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throwBadRequestRuntimeException("BookEntity with book-id : "+bookIssue.getBookId()+" does not exist");

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throwBadRequestRuntimeException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkReIssueStatus = bookIssueDAO.checkIfBookIsAlreadyReIssued(bookIssueId);
        if(checkReIssueStatus)
            throwBadRequestRuntimeException("BookEntity is already re-issued");

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
            throwBadRequestRuntimeException("BookEntity issue entry : "+bookIssue.getBookIssueId()+" does not exist");

        boolean checkBookId = bookDAO.checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throwBadRequestRuntimeException("BookEntity with book-id : "+bookIssue.getBookId()+" does not exist");

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throwBadRequestRuntimeException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkReturnStatus = bookIssueDAO.checkIfBookIsAlreadyReturned(bookIssueId);
        if(checkReturnStatus)
            throwBadRequestRuntimeException("BookEntity is already returned");

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
            throwBadRequestRuntimeException("No BookEntity Issue entry found with book-issue-id : "+bookIssueId);
        else
            bookIssueDAO.deleteBookIssueFromDatabase(bookIssueId);
    }

    public List<BookIssue> getBookIssueEntries(BookIssueFilterBean bookIssueFilterBean) {
        return bookIssueDAO.getBookIssueEntriesWithFilterFromDatabase(bookIssueFilterBean);
    }

    public BookIssue getBookIssueDetails(int bookIssueId) {
        BookIssue bookIssue = bookIssueDAO.getBookIssueDetailsFromDatabase(bookIssueId);

        if(bookIssue.getBookIssueId() == 0)
            throwBadRequestRuntimeException("No book issue entry found with book-issue-id : "+bookIssueId);
        else
            return bookIssue;

        return null;
    }

    public List<UserIssuedBook> getListOfBooksIssuedToAUser(String username, UserIssuedBookFilterBean bean) {

        boolean checkUsername = userDAO.checkIfUsernameExistsInDatabase(username);
        if(!checkUsername)
            throwBadRequestRuntimeException("Username : "+username+" does not exist");

        return bookIssueDAO.getBooksIssuedByAUser(username, bean);
    }

    public List<IssuedBook> getListOfAllIssuedBooks() {
        return bookIssueDAO.getAllIssuedBooks();
    }

    // ##################### PRIVATE METHODS ######################

    private void nullFieldValueCheck(BookIssue bookIssue) {
        if(bookIssue.getBookId() == 0 )
            throwBadRequestRuntimeException("Value for BookEntity Id field is either empty or null !!");
        if(bookIssue.getUserName() == null || bookIssue.getUserName().isEmpty())
            throwBadRequestRuntimeException("Value for Username field is either empty or null !!");
    }
}