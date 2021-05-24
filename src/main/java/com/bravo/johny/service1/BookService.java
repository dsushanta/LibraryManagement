package com.bravo.johny.service1;

import com.bravo.johny.controller.filterbeans.BookFilterBean;
import com.bravo.johny.dao.BookDAO;
import com.bravo.johny.dao.BookIssueDAO;
import com.bravo.johny.dao.CopyDAO;
import com.bravo.johny.dto.Book;
import com.bravo.johny.dto.Genre;
import com.bravo.johny.dto.User;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;


//@Service
public class BookService {

    private BookDAO bookDAO;
    private CopyDAO copyDAO;
    private BookIssueDAO bookIssueDAO;

    public BookService() {
        bookDAO = new BookDAO();
        copyDAO = new CopyDAO();
        bookIssueDAO = new BookIssueDAO();
    }

    public Book addNewBook(Book book) {
        nullFieldValueCheck(book);
        Book newBook;

        boolean bookExists = bookDAO.checkIfBookExistsInDatabase(book.getTitle(), book.getAuthor());
        if(bookExists) {
            int bookId = bookDAO.getBookIdForTitleAndAuthorFromDatabase(book.getTitle(), book.getAuthor());
            bookDAO.updateBookAvailabilityStatus(bookId, true);
            newBook = bookDAO.getBookWithBookIdFromDatabase(bookId);
        } else
            newBook = bookDAO.addNewBookIntoDatabase(book);
        return newBook;
    }

    public List<Book> getBooks(BookFilterBean bookBean) {
        return bookDAO.getBooksWithFilterFromDatabase(bookBean);
    }

    public Book getBookDetails(int bookId) {
        Book book = bookDAO.getBookWithBookIdFromDatabase(bookId);

        if(book.getBookId() == 0)
            throwBadRequestRuntimeException("No book found with bookId : "+bookId);
        else
            return book;

        return null;
    }

    public Book updateBookDetails(int bookId, Book book) {
        Book bookToBeUpdated = bookDAO.getBookWithBookIdFromDatabase(bookId);

        if(bookToBeUpdated.getBookId() == 0)
            throwBadRequestRuntimeException("No book found with bookId : "+bookId);
        else {
            book.setBookId(bookId);
            book = bookDAO.updateBookInDatabase(book);
        }
            return book;
    }

    public void deleteBook(int bookId) {
        Book book = bookDAO.getBookWithBookIdFromDatabase(bookId);

        if(book.getBookId() == 0)
            throwBadRequestRuntimeException("No book found with bookId : "+bookId);
        else if(copyDAO.checkIfAnyCopyOfABookIsIssued(bookId))
            throwBadRequestRuntimeException("BookEntity entry can not be removed as the book has been issued to a customer !!");
        else {
            bookIssueDAO.deleteBookIssueEntriesForABookFromDatabase(bookId);
            copyDAO.deleteAllCopiesOfABookFromDatabase(bookId);
            bookDAO.deleteBookFromDatabase(bookId);
        }
    }

    public List<Genre> getAllGenre() {

        return bookDAO.getAllGenre();
    }

    public List<User> getIssuedUsers(String bookId) {
        return bookIssueDAO.getIssuedUsers(bookId);
    }

    // ##################### PRIVATE METHODS ######################

    private void nullFieldValueCheck(Book book) {
        if(book.getTitle() == null || book.getTitle().isEmpty())
            throwBadRequestRuntimeException("Value for Title field is either empty or null !!");
        if(book.getDescription() == null || book.getDescription().isEmpty())
            throwBadRequestRuntimeException("Value for Description field is either empty or null !!");
        if(book.getAuthor() == null || book.getAuthor().isEmpty())
            throwBadRequestRuntimeException("Value for Author field is either empty or null !!");
        if(book.getGenre() == null || book.getGenre().isEmpty())
            throwBadRequestRuntimeException("Value for Genre field is either empty or null !!");
    }
}
