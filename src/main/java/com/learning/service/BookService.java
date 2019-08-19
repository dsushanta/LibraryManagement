package com.learning.service;

import com.learning.dao.BookDAO;
import com.learning.dao.BookIssueDAO;
import com.learning.dao.CopyDAO;
import com.learning.dto.Book;
import com.learning.dto.Genre;
import com.learning.exception.CannotDeleteException;
import com.learning.exception.DataNotFoundException;
import com.learning.exception.FieldValueRequiredException;
import com.learning.webresource.filterbeans.BookFilterBean;

import java.util.List;

public class BookService {

    BookDAO bookDAO;
    CopyDAO copyDAO;
    BookIssueDAO bookIssueDAO;

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
            throw new DataNotFoundException("No book found with bookId : "+bookId);
        else
            return book;
    }

    public Book updateBookDetails(int bookId, Book book) {
        Book bookToBeUpdated = bookDAO.getBookWithBookIdFromDatabase(bookId);

        if(bookToBeUpdated.getBookId() == 0)
            throw new DataNotFoundException("No book found with bookId : "+bookId);
        else {
            book.setBookId(bookId);
            book = bookDAO.updateBookInDatabase(book);
        }
            return book;
    }

    public void deleteBook(int bookId) {
        Book book = bookDAO.getBookWithBookIdFromDatabase(bookId);

        if(book.getBookId() == 0)
            throw new DataNotFoundException("No book found with bookId : "+bookId);
        else if(copyDAO.checkIfAnyCopyOfABookIsIssued(bookId))
            throw new CannotDeleteException("Book entry can not be removed as the book has been issued to a customer !!");
        else {
            bookIssueDAO.deleteBookIssueEntriesForABookFromDatabase(bookId);
            copyDAO.deleteAllCopiesOfABookFromDatabase(bookId);
            bookDAO.deleteBookFromDatabase(bookId);
        }
    }

    public List<Genre> getAllGenre() {

        return bookDAO.getAllGenre();
    }

    // ##################### PRIVATE METHODS ######################

    private void nullFieldValueCheck(Book book) {
        if(book.getTitle() == null || book.getTitle().isEmpty())
            throw new FieldValueRequiredException("Value for Title field is either empty or null !!");
        if(book.getDescription() == null || book.getDescription().isEmpty())
            throw new FieldValueRequiredException("Value for Description field is either empty or null !!");
        if(book.getAuthor() == null || book.getAuthor().isEmpty())
            throw new FieldValueRequiredException("Value for Author field is either empty or null !!");
        if(book.getGenre() == null || book.getGenre().isEmpty())
            throw new FieldValueRequiredException("Value for Genre field is either empty or null !!");
    }
}
