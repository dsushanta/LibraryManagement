package com.bravo.johny.service;

import com.bravo.johny.config.ProjectConfig;
import com.bravo.johny.controller.filterbeans.BookIssueFilterBean;
import com.bravo.johny.controller.filterbeans.UserIssuedBookFilterBean;
import com.bravo.johny.dto.BookIssue;
import com.bravo.johny.dto.IssuedBook;
import com.bravo.johny.dto.UserIssuedBook;
import com.bravo.johny.entity.*;
import com.bravo.johny.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;


@Service
public class BookIssueService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookLifeCycleRepository bookLifeCycleRepository;
    @Autowired
    private ConfigRepository configRepository;

    public BookIssue issueABook(BookIssue bookIssue) {

        nullFieldValueCheck(bookIssue);

        boolean checkUsername = checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throwBadRequestRuntimeException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkBookId = checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throwBadRequestRuntimeException("BookEntity with book-id : "+bookIssue.getBookId()+" does not exist");

        int copyId = isBookAvailable(bookIssue.getBookId());
        if(copyId == 0)
            throwBadRequestRuntimeException("Sorry, all copies of book with book-id : "+bookIssue.getBookId()+" have been issued");

        int bookCount = userRepository.findFirstByUserName(bookIssue.getUserName()).getBookCount();
        if(bookCount >= configRepository.findAll().get(0).getNoOfBooksPerUser())
            throwBadRequestRuntimeException("Sorry, You have already exceeded the maximum number of books a user can keep, please return a book to issue a new one !!");

        bookIssue.setCopyId(copyId);
        clearAllDuesOfAUser(bookIssue.getUserName());
        updateUserDues(bookIssue.getUserName(), 0);
        BookIssue newBookIssue = issue(bookIssue);
        CopyEntity copyEntity = copyRepository.findFirstByCopyId(copyId);
        copyEntity.setIssued(true);
        //copyEntity.setIsIssued(1);
        copyRepository.save(copyEntity);
        UserEntity userEntity = userRepository.findFirstByUserName(bookIssue.getUserName());
        userEntity.setBookCount(bookCount+1);
        userRepository.save(userEntity);

        copyId = isBookAvailable(bookIssue.getBookId());
        if(copyId == 0) {
            BookEntity bookEntity = bookRepository.findFirstByBookId(bookIssue.getBookId());
            bookEntity.setAvailable(false);
            bookRepository.save(bookEntity);
        }
        return newBookIssue;
    }

    public BookIssue reIssueABook(BookIssue bookIssue) {

        int bookIssueId = bookIssue.getBookIssueId();

        BookLifeCycleEntity bookLifeCycleEntity = bookLifeCycleRepository.findFirstByBookIssueId(bookIssueId);
        bookIssue.setUserName(bookLifeCycleEntity.getUser().getUserName());
        bookIssue.setExpectedReturnDate(bookLifeCycleEntity.getExpectedReturnDate());
        boolean checkBookIssueId = checkIfBookIssueEntryExists(bookIssueId);
        if(!checkBookIssueId)
            throwBadRequestRuntimeException("BookEntity issue entry : "+bookIssueId+" does not exist");

        boolean checkBookId = checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throwBadRequestRuntimeException("BookEntity with book-id : "+bookIssue.getBookId()+" does not exist");

        boolean checkUsername = checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throwBadRequestRuntimeException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkReIssueStatus = checkIfBookIsAlreadyReIssued(bookIssueId);
        if(checkReIssueStatus)
            throwBadRequestRuntimeException("BookEntity is already re-issued");

//        bookIssueDAO.clearAllDuesOfAUser(bookIssue.getUserName());
        bookIssue = reIssue(bookIssue);

        UserEntity userEntity = userRepository.findFirstByUserName(bookIssue.getUserName());

        int userdue = bookLifeCycleRepository.getTotalDuesOfAUser(bookIssue.getUserName());

        userEntity.setFine(userdue);
        userRepository.save(userEntity);

        return bookIssue;
    }


    public BookIssue returnABook(BookIssue bookIssue) {

        int bookIssueId = bookIssue.getBookIssueId();

        BookLifeCycleEntity bookLifeCycleEntity = bookLifeCycleRepository.findFirstByBookIssueId(bookIssueId);
        bookIssue.setUserName(bookLifeCycleEntity.getUser().getUserName());
        bookIssue.setExpectedReturnDate(bookLifeCycleEntity.getExpectedReturnDate());
        boolean checkBookIssueId = checkIfBookIssueEntryExists(bookIssueId);
        if(!checkBookIssueId)
            throwBadRequestRuntimeException("BookEntity issue entry : "+bookIssueId+" does not exist");

        boolean checkBookId = checkIfBookExistsInDatabase(bookIssue.getBookId());
        if(!checkBookId)
            throwBadRequestRuntimeException("BookEntity with book-id : "+bookIssue.getBookId()+" does not exist");

        boolean checkUsername = checkIfUsernameExistsInDatabase(bookIssue.getUserName());
        if(!checkUsername)
            throwBadRequestRuntimeException("Username : "+bookIssue.getUserName()+" does not exist");

        boolean checkReturnStatus = checkIfBookIsAlreadyReturned(bookIssueId);
        if(checkReturnStatus)
            throwBadRequestRuntimeException("BookEntity is already returned");

        bookIssue = bookReturn(bookIssue);

        CopyEntity copyEntity = copyRepository.findFirstByCopyId(bookIssue.getCopyId());
        copyEntity.setIssued(false);
        //copyEntity.setIsIssued(0);
        copyRepository.save(copyEntity);

        BookEntity bookEntity = bookRepository.findFirstByBookId(bookIssue.getBookId());
        bookEntity.setAvailable(true);
        bookRepository.save(bookEntity);

        UserEntity userEntity = userRepository.findFirstByUserName(bookIssue.getUserName());

        int bookCount = userEntity.getBookCount();

        int userdue = bookLifeCycleRepository.getTotalDuesOfAUser(bookIssue.getUserName());

        userEntity.setBookCount(bookCount-1);
        userEntity.setFine(userdue);
        userRepository.save(userEntity);

        return bookIssue;
    }

    public int isBookAvailable(int bookId) {

        boolean bookIssueStatus = false;
        BookEntity bookEntity = bookRepository.findFirstByBookId(bookId);
        CopyEntity copyEntity = copyRepository.findFirstByBookAndIsIssuedOrderByCopyId(bookEntity, bookIssueStatus);
        return copyEntity.getCopyId();
    }

    public void removeBookIssueEntryFromDatabase(int bookIssueId) {

        BookLifeCycleEntity bookLifeCycleEntity = bookLifeCycleRepository.findFirstByBookIssueId(bookIssueId);

        if(bookLifeCycleEntity == null)
            throwBadRequestRuntimeException("No BookEntity Issue entry found with book-issue-id : "+bookIssueId);
        else
            bookLifeCycleRepository.deleteByBookIssueId(bookIssueId);
    }

    public List<BookIssue> getBookIssueEntries(BookIssueFilterBean bookIssueFilterBean) {

        List<BookIssue> allBookIssueEntries = new ArrayList<>();
        List<BookLifeCycleEntity> bookLifeCycleEntities = null;

        int bookId = bookIssueFilterBean.getBookId();
        String username = bookIssueFilterBean.getUsername();
        int offset = bookIssueFilterBean.getOffset();
        int limit = bookIssueFilterBean.getLimit();

        BookEntity bookEntity = bookRepository.findFirstByBookId(bookId);
        UserEntity userEntity = userRepository.findFirstByUserName(username);

        Pageable pageable;
        boolean anyfilter = anyFilterInTheURL(bookIssueFilterBean);

        if(anyfilter) {
            if(bookId !=0 && username != null) {
                if (offset >= 0 && limit > 0) {
                    pageable = PageRequest.of(offset, limit);
                    bookLifeCycleEntities = bookLifeCycleRepository.findByBookAndUser(bookEntity, userEntity, pageable);
                }
                else
                    bookLifeCycleEntities = bookLifeCycleRepository.findByBookAndUser(bookEntity, userEntity);
            } else if(bookId != 0) {
                if(offset >= 0 && limit > 0) {
                    pageable = PageRequest.of(offset, limit);
                    bookLifeCycleEntities = bookLifeCycleRepository.findByBook(bookEntity, pageable);
                }
                else
                    bookLifeCycleEntities = bookLifeCycleRepository.findByBook(bookEntity);
            } else if(username != null) {
                if(offset >= 0 && limit > 0) {
                    pageable = PageRequest.of(offset, limit);
                    bookLifeCycleEntities = bookLifeCycleRepository.findByUser(userEntity, pageable);
                }
                else
                    bookLifeCycleEntities = bookLifeCycleRepository.findByUser(userEntity);
            }
        }
        bookLifeCycleEntities.forEach(bookLifeCycleEntity -> allBookIssueEntries.add(prepareBookIssueDTOFromBookLifeCycleEntity(bookLifeCycleEntity)));

        return allBookIssueEntries;
    }

    public BookIssue getBookIssueDetails(int bookIssueId) {

        BookLifeCycleEntity bookLifeCycleEntity = bookLifeCycleRepository.findFirstByBookIssueId(bookIssueId);

        if(bookLifeCycleEntity == null)
            throwBadRequestRuntimeException("No book issue entry found with book-issue-id : "+bookIssueId);
        else
            return prepareBookIssueDTOFromBookLifeCycleEntity(bookLifeCycleEntity);

        return null;
    }

    public List<UserIssuedBook> getListOfBooksIssuedToAUser(String username, UserIssuedBookFilterBean bean) {

        boolean checkUsername = checkIfUsernameExistsInDatabase(username);
        if(!checkUsername)
            throwBadRequestRuntimeException("Username : "+username+" does not exist");

        List<UserIssuedBook> userIssuedBooks = null;

        int bookId = bean.getBookId();
        int offset = bean.getOffset();
        int limit = bean.getLimit();

        Pageable pageable;

        if(bookId != 0) {
            if(offset >=0 && limit >0) {
                pageable = PageRequest.of(offset, limit);
                userIssuedBooks = bookLifeCycleRepository.getBooksIssuedByAUser(username, bookId, pageable);
            } else
                userIssuedBooks = bookLifeCycleRepository.getBooksIssuedByAUser(username, bookId);
        } else {
            if(offset >=0 && limit >0) {
                pageable = PageRequest.of(offset, limit);
                userIssuedBooks = bookLifeCycleRepository.getBooksIssuedByAUser(username, pageable);
            }
            else
                userIssuedBooks = bookLifeCycleRepository.getBooksIssuedByAUser(username);
        }

        userIssuedBooks.forEach(userIssuedBook -> {
            if(userIssuedBook.getActualReturnDate() == null)
                userIssuedBook.setReturned(false);
            else
                userIssuedBook.setReturned(true);
        });

        return userIssuedBooks;
    }

    public List<IssuedBook> getListOfAllIssuedBooks() {

        return copyRepository.getAllIssuedBooks();
    }



    // ##################### PRIVATE METHODS ######################


    private BookIssue issue(BookIssue bookIssue) {

        ConfigEntity configEntity = configRepository.findAll().get(0);
        UserEntity userEntity = userRepository.findFirstByUserName(bookIssue.getUserName());
        CopyEntity copyEntity = copyRepository.findFirstByCopyId(bookIssue.getCopyId());
        BookEntity bookEntity = bookRepository.findFirstByBookId(bookIssue.getBookId());

        long millis=System.currentTimeMillis();
        Date currentDate = new Date(millis);
        Date expectedReturnDate = addDaysToADate(currentDate, configEntity.getNoOfDaysAUserCanKeepABook());

        BookLifeCycleEntity bookLifeCycleEntity = new BookLifeCycleEntity(
                userEntity, bookEntity, copyEntity, currentDate, expectedReturnDate
        );

        BookLifeCycleEntity newBookLifeCycleEntity = bookLifeCycleRepository.save(bookLifeCycleEntity);

        return prepareBookIssueDTOFromBookLifeCycleEntity(newBookLifeCycleEntity);
    }

    private BookIssue reIssue(BookIssue bookIssue) {

        ConfigEntity configEntity = configRepository.findAll().get(0);
        BookLifeCycleEntity bookLifeCycleEntity = bookLifeCycleRepository.findFirstByBookIssueId(bookIssue.getBookIssueId());

        int numberOfDaysAfterExpectedReturnDate = getDelayInDaysFromCurrentDate(bookIssue.getExpectedReturnDate());
        int fine = bookLifeCycleEntity.getFine();
        if(numberOfDaysAfterExpectedReturnDate > 0)
            fine += numberOfDaysAfterExpectedReturnDate * configEntity.getFinePerDay();

        long millis=System.currentTimeMillis();
        Date currentDate = new Date(millis);
        Date expectedReturnDate = addDaysToADate(currentDate, configEntity.getNoOfDaysAUserCanKeepABook());

        bookLifeCycleEntity.setRenewed(true);
        bookLifeCycleEntity.setFineCleared(false);
        bookLifeCycleEntity.setFine(fine);
        bookLifeCycleEntity.setRenewDate(currentDate);
        bookLifeCycleEntity.setExpectedReturnDate(expectedReturnDate);

        bookLifeCycleEntity = bookLifeCycleRepository.save(bookLifeCycleEntity);

        return prepareBookIssueDTOFromBookLifeCycleEntity(bookLifeCycleEntity);
    }

    private BookIssue bookReturn(BookIssue bookIssue) {

        ConfigEntity configEntity = configRepository.findAll().get(0);
        BookLifeCycleEntity bookLifeCycleEntity = bookLifeCycleRepository.findFirstByBookIssueId(bookIssue.getBookIssueId());

        int numberOfDaysAfterExpectedReturnDate = getDelayInDaysFromCurrentDate(bookIssue.getExpectedReturnDate());
        int fine = bookLifeCycleEntity.getFine();

        if(numberOfDaysAfterExpectedReturnDate > 0)
            fine += numberOfDaysAfterExpectedReturnDate * configEntity.getFinePerDay();

        long millis=System.currentTimeMillis();
        Date currentDate = new Date(millis);

        bookLifeCycleEntity.setReturned(true);
        bookLifeCycleEntity.setFineCleared(false);
        bookLifeCycleEntity.setFine(fine);
        bookLifeCycleEntity.setActualReturnDate(currentDate);

        bookLifeCycleEntity = bookLifeCycleRepository.save(bookLifeCycleEntity);

        return prepareBookIssueDTOFromBookLifeCycleEntity(bookLifeCycleEntity);
    }

    private void clearAllDuesOfAUser(String userName) {

        UserEntity userEntity = userRepository.findFirstByUserName(userName);
        List<BookLifeCycleEntity> booksIssued = bookLifeCycleRepository.findByUser(userEntity);
        booksIssued.forEach(bookIssued -> bookIssued.setFineCleared(true));
        bookLifeCycleRepository.saveAll(booksIssued);
    }

    private void updateUserDues(String userName, int userDue) {

        UserEntity userEntity = userRepository.findFirstByUserName(userName);
        userEntity.setFine(userDue);
        userRepository.save(userEntity);
    }

    private boolean anyFilterInTheURL(BookIssueFilterBean bookIssueFilterBean) {
        if(bookIssueFilterBean.getBookId() != 0 || bookIssueFilterBean.getUsername() != null)
            return true;
        else
            return false;
    }

    private void nullFieldValueCheck(BookIssue bookIssue) {
        if(bookIssue.getBookId() == 0 )
            throwBadRequestRuntimeException("Value for BookEntity Id field is either empty or null !!");
        if(bookIssue.getUserName() == null || bookIssue.getUserName().isEmpty())
            throwBadRequestRuntimeException("Value for Username field is either empty or null !!");
    }

    private boolean checkIfUsernameExistsInDatabase(String username) {

        UserEntity userEntity = userRepository.findFirstByUserName(username);

        if (userEntity != null)
            return true;
        else
            return false;
    }

    private boolean checkIfBookIssueEntryExists(int bookIssueId) {

        long count = bookLifeCycleRepository.countByBookIssueId(bookIssueId);

        if (count == 0)
            return false;
        else
            return true;
    }

    private boolean checkIfBookIsAlreadyReturned(int bookIssueId) {

        BookLifeCycleEntity bookLifeCycleEntity = bookLifeCycleRepository.findFirstByBookIssueId(bookIssueId);

        return bookLifeCycleEntity.isReturned();
    }

    private boolean checkIfEmailExistsInDatabase(String email) {

        UserEntity userEntity = userRepository.findFirstByEmail(email);

        if (userEntity != null)
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

    private boolean checkIfBookExistsInDatabase(int bookId) {

        BookEntity bookEntities = bookRepository.findFirstByBookId(bookId);

        if(bookEntities != null)
            return true;
        else
            return false;
    }

    private boolean checkIfBookIsAlreadyReIssued(int bookIssueId) {

        BookLifeCycleEntity bookLifeCycleEntity = bookLifeCycleRepository.findFirstByBookIssueId(bookIssueId);

        return bookLifeCycleEntity.isRenewed();
    }

    private BookIssue prepareBookIssueDTOFromBookLifeCycleEntity(BookLifeCycleEntity bookLifeCycleEntity) {

        return new BookIssue(
                bookLifeCycleEntity.getBookIssueId(),
                bookLifeCycleEntity.getCopy().getCopyId(),
                bookLifeCycleEntity.getBook().getBookId(),
                bookLifeCycleEntity.getUser().getUserName(),
                bookLifeCycleEntity.isReturned(),
                bookLifeCycleEntity.isRenewed(),
                bookLifeCycleEntity.getIssueDate(),
                bookLifeCycleEntity.getExpectedReturnDate(),
                bookLifeCycleEntity.getActualReturnDate(),
                bookLifeCycleEntity.getRenewDate(),
                bookLifeCycleEntity.getFine(),
                bookLifeCycleEntity.isFineCleared(),
                null
        );
    }

    private Date addDaysToADate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);

        return new Date(c.getTimeInMillis());
    }

    private int getDelayInDaysFromCurrentDate(Date date) {
        long millis=System.currentTimeMillis();
        Date currentDate = new Date(millis);

        int days = (int) ((currentDate.getTime() - date.getTime()) / ProjectConfig.MILLISECONDS_IN_A_DAY);

        if(days < 0)
            days = 0;

        return days;
    }
}