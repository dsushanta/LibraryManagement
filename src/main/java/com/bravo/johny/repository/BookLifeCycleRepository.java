package com.bravo.johny.repository;

import com.bravo.johny.dto.IssuedBook;
import com.bravo.johny.dto.User;
import com.bravo.johny.dto.UserIssuedBook;
import com.bravo.johny.entity.BookEntity;
import com.bravo.johny.entity.BookLifeCycleEntity;
import com.bravo.johny.entity.CopyEntity;
import com.bravo.johny.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface BookLifeCycleRepository extends JpaRepository<BookLifeCycleEntity, Integer> {

    @Nullable
    BookLifeCycleEntity findFirstByBookIssueId(int bookIssueId);

    @Nullable
    long countByBookIssueId(int bookIssueId);

    BookLifeCycleEntity deleteByBookIssueId(int bookIssueId);

    BookLifeCycleEntity deleteByBook(BookEntity book);

    BookLifeCycleEntity deleteByCopy(CopyEntity copy);

    @Query("SELECT SUM(blce.fine) AS total_fine " +
            "FROM BookLifeCycleEntity blce " +
            "WHERE blce.user.userName=?1 AND blce.fineCleared=false " +
            "GROUP BY blce.user.userName")
    int getTotalDuesOfAUser(String userName);

    List<BookLifeCycleEntity> findByBookAndUser(BookEntity book, UserEntity user);

    List<BookLifeCycleEntity> findByBookAndUser(BookEntity book, UserEntity user, Pageable pageable);

    List<BookLifeCycleEntity> findByBook(BookEntity book);

    List<BookLifeCycleEntity> findByBook(BookEntity book, Pageable pageable);

    List<BookLifeCycleEntity> findByUser(UserEntity user);

    List<BookLifeCycleEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT new com.bravo.johny.dto.UserIssuedBook(blce.bookIssueId, b.bookId, b.title, b.author, b.genre, blce.issueDate, blce.expectedReturnDate, blce.actualReturnDate, blce.renewDate, blce.fine, blce.fineCleared) " +
            "FROM BookLifeCycleEntity blce " +
            "JOIN blce.book b " +
            "WHERE blce.user.userName = ?1 " +
            "AND b.bookId = ?2")
    List<UserIssuedBook> getBooksIssuedByAUser(String userName, int bookId, Pageable pageable);

    @Query("SELECT new com.bravo.johny.dto.UserIssuedBook(blce.bookIssueId, b.bookId, b.title, b.author, b.genre, blce.issueDate, blce.expectedReturnDate, blce.actualReturnDate, blce.renewDate, blce.fine, blce.fineCleared) " +
            "FROM BookLifeCycleEntity blce " +
            "JOIN blce.book b " +
            "WHERE blce.user.userName = ?1 " +
            "AND b.bookId = ?2")
    List<UserIssuedBook> getBooksIssuedByAUser(String userName, int bookId);

    @Query("SELECT new com.bravo.johny.dto.UserIssuedBook(blce.bookIssueId, b.bookId, b.title, b.author, b.genre, blce.issueDate, blce.expectedReturnDate, blce.actualReturnDate, blce.renewDate, blce.fine, blce.fineCleared) " +
            "FROM BookLifeCycleEntity blce " +
            "JOIN blce.book b " +
            "WHERE blce.user.userName = ?1 ")
    List<UserIssuedBook> getBooksIssuedByAUser(String userName, Pageable pageable);

    @Query("SELECT new com.bravo.johny.dto.UserIssuedBook(blce.bookIssueId, b.bookId, b.title, b.author, b.genre, blce.issueDate, blce.expectedReturnDate, blce.actualReturnDate, blce.renewDate, blce.fine, blce.fineCleared) " +
            "FROM BookLifeCycleEntity blce " +
            "JOIN blce.book b " +
            "WHERE blce.user.userName = ?1 ")
    List<UserIssuedBook> getBooksIssuedByAUser(String userName);

    @Query("SELECT new com.bravo.johny.dto.User(u.userName, u.firstName, u.lastName) " +
            "FROM BookLifeCycleEntity blce " +
            "JOIN blce.user u " +
            "WHERE blce.book.bookId = ?1 " +
            "AND blce.actualReturnDate IS NULL")
    List<User> getIssuedUsers(String bookId);
}
