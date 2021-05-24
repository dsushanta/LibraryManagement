package com.bravo.johny.service;

import com.bravo.johny.controller.filterbeans.BookFilterBean;
import com.bravo.johny.dto.Book;
import com.bravo.johny.dto.Genre;
import com.bravo.johny.dto.User;
import com.bravo.johny.entity.BookEntity;
import com.bravo.johny.entity.CopyEntity;
import com.bravo.johny.repository.BookLifeCycleRepository;
import com.bravo.johny.repository.BookRepository;
import com.bravo.johny.repository.CopyRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;


@Service
@NoArgsConstructor
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private BookLifeCycleRepository bookLifeCycleRepository;

    public Book addNewBook(Book book) {

        nullFieldValueCheck(book);

        boolean bookExists = checkIfBookExistsInDatabase(book.getTitle(), book.getAuthor());
        BookEntity bookEntity;
        if(bookExists) {
            int bookId = bookRepository.findFirstByTitleAndAuthor(book.getTitle(), book.getAuthor()).getBookId();
            bookEntity = bookRepository.findFirstByBookId(bookId);
            bookEntity.setAvailable(true);
            bookRepository.save(bookEntity);
            bookEntity = bookRepository.findFirstByBookId(bookId);

        } else
            bookEntity = bookRepository.save(prepareBookEntityFromBookDTO(book));

        return prepareBookDTOFromBookEntity(bookEntity);
    }

    public List<Book> getBooks(BookFilterBean bookBean) {

        List<Book> allBooks = new ArrayList<>();
        List<BookEntity> bookEntities = null;

        String title = bookBean.getTitle();
        String author = bookBean.getAuthor();
        String genre = bookBean.getGenre();
        int offset = bookBean.getOffset();
        int limit = bookBean.getLimit();

        Pageable pageable = null;

        if(offset >= 0 && limit > 0)
            pageable = PageRequest.of(offset, limit);

        boolean anyfilter = anyFilterInTheURL(bookBean);
        if(anyfilter) {
            if (title != null && author != null && genre != null) {
                if (offset >= 0 && limit > 0)
                    bookEntities = bookRepository.findByTitleLikeAndAuthorLikeAndGenreLike(title, author, genre, pageable);
                else
                    bookEntities = bookRepository.findByTitleLikeAndAuthorLikeAndGenreLike(title, author, genre);
            } else if (title != null && author != null) {
                if (offset >= 0 && limit > 0)
                    bookEntities = bookRepository.findByTitleLikeAndAuthorLike(title, author, pageable);
                else
                    bookEntities = bookRepository.findByTitleLikeAndAuthorLike(title, author);
            } else if (author != null && genre != null) {
                if (offset >= 0 && limit > 0)
                    bookEntities = bookRepository.findByAuthorLikeAndGenreLike(author, genre, pageable);
                else
                    bookEntities = bookRepository.findByAuthorLikeAndGenreLike(author, genre);
            } else if (genre != null && title != null) {
                if (offset >= 0 && limit > 0)
                    bookEntities = bookRepository.findByGenreLikeAndTitleLike(genre, title, pageable);
                else
                    bookEntities = bookRepository.findByGenreLikeAndTitleLike(genre, title);
            } else if (title != null) {
                if (offset >= 0 && limit > 0)
                    bookEntities = bookRepository.findByTitleLike(title, pageable);
                else
                    bookEntities = bookRepository.findByTitleLike(title);
            } else if (author != null) {
                if (offset >= 0 && limit > 0)
                    bookEntities = bookRepository.findByAuthorLike(author, pageable);
                else
                    bookEntities = bookRepository.findByAuthorLike(author);
            } else if (genre != null) {
                if (offset >= 0 && limit > 0)
                    bookEntities = bookRepository.findByGenreLike(genre, pageable);
                else
                    bookEntities = bookRepository.findByGenreLike(genre);
            }
        } else {
            if (offset >= 0 && limit > 0)
                bookEntities = bookRepository.findAll(pageable).toList();
            else
                bookEntities = bookRepository.findAll();
        }
        bookEntities.forEach(bookEntity -> allBooks.add(prepareBookDTOFromBookEntity(bookEntity)));

        return allBooks;
    }

    public Book getBookDetails(int bookId) {

        BookEntity bookEntity = bookRepository.findFirstByBookId(bookId);

        if(bookEntity == null)
            throwBadRequestRuntimeException("No book found with bookId : "+bookId);
        else
            return prepareBookDTOFromBookEntity(bookEntity);

        return null;
    }

    public Book updateBookDetails(int bookId, Book book) {

        BookEntity bookEntityToBeUpdated = bookRepository.findFirstByBookId(bookId);
        BookEntity bookEntity = null;

        if(bookEntityToBeUpdated == null)
            throwBadRequestRuntimeException("No book found with bookId : "+bookId);
        else {
            if(book.getTitle() != null && !book.getTitle().isEmpty())
                bookEntityToBeUpdated.setTitle(book.getTitle());
            if(book.getDescription() != null && !book.getDescription().isEmpty())
                bookEntityToBeUpdated.setDescription(book.getDescription());
            if(book.getAuthor() != null && !book.getAuthor().isEmpty())
                bookEntityToBeUpdated.setAuthor(book.getAuthor());
            if(book.getGenre() != null && !book.getGenre().isEmpty())
                bookEntityToBeUpdated.setGenre(book.getGenre());

            bookEntity = bookRepository.save(bookEntityToBeUpdated);
        }
            return prepareBookDTOFromBookEntity(bookEntity);
    }

    public void deleteBook(int bookId) {

        BookEntity bookEntity = bookRepository.findFirstByBookId(bookId);

        if(bookEntity == null)
            throwBadRequestRuntimeException("No book found with bookId : "+bookId);
        else if(checkIfAnyCopyOfABookIsIssued(bookId))
            throwBadRequestRuntimeException("BookEntity entry can not be removed as the book has been issued to a customer !!");
        else {
            bookLifeCycleRepository.deleteByBook(bookEntity);
            copyRepository.deleteByBook(bookEntity);
            bookRepository.deleteByBookId(bookId);
        }
    }

    public List<Genre> getAllGenre() {

        return bookRepository.getAllGenre();
    }

    // TODO
    public List<User> getIssuedUsers(String bookId) {
        return bookLifeCycleRepository.getIssuedUsers(bookId);
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

    private boolean anyFilterInTheURL(BookFilterBean bookBean) {

        if(bookBean.getAuthor() != null || bookBean.getTitle() != null || bookBean.getGenre() != null)
            return true;
        else
            return false;
    }

    public boolean checkIfBookExistsInDatabase(String title, String author) {

        List<BookEntity> bookEntities = bookRepository.findByTitleAndAuthor(title, author);

        if(bookEntities.size() > 0)
            return true;
        else
            return false;
    }

    public boolean checkIfBookExistsInDatabase(int bookId) {

        BookEntity bookEntities = bookRepository.findFirstByBookId(bookId);

        if(bookEntities != null)
            return true;
        else
            return false;
    }

    public boolean checkIfAnyCopyOfABookIsIssued(int bookId) {

        BookEntity bookEntity = bookRepository.findFirstByBookId(bookId);
        long copyCount = copyRepository.countByBookAndIsIssuedOrderByCopyId(bookEntity, true);

        if(copyCount > 0)
            return true;
        else
            return false;
    }

    public boolean checkIfCopyOfABookIsIssued(int copyId) {

        CopyEntity copyEntity = copyRepository.findFirstByCopyIdAndAndIsIssued(copyId, true);

        if(copyEntity == null)
            return false;
        else
            return true;
    }

    private BookEntity prepareBookEntityFromBookDTO(Book book) {

        return new BookEntity(
                book.getTitle(),
                book.getDescription(),
                book.getAuthor(),
                book.getGenre()
        );
    }

    private Book prepareBookDTOFromBookEntity(BookEntity bookEntity) {

        Book book = new Book();
        book.setBookId(bookEntity.getBookId());
        book.setTitle(bookEntity.getTitle());
        book.setDescription(bookEntity.getDescription());
        book.setAuthor(bookEntity.getAuthor());
        book.setGenre(bookEntity.getGenre());
        book.setAvailable(bookEntity.isAvailable());
        book.setLikes(bookEntity.getLikes());

        return book;
    }
}
