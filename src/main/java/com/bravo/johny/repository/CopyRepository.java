package com.bravo.johny.repository;

import com.bravo.johny.dto.IssuedBook;
import com.bravo.johny.entity.BookEntity;
import com.bravo.johny.entity.CopyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface CopyRepository extends JpaRepository<CopyEntity, Integer> {

    @Nullable
    CopyEntity findFirstByCopyId(int copyId);

    @Nullable
    List<CopyEntity> findByBook(BookEntity book);

    @Nullable
    List<CopyEntity> findByBook(BookEntity book, Pageable pageable);

    @Nullable
    CopyEntity findFirstByCopyIdAndAndIsIssued(int copyId, boolean isIssued);

    @Nullable
    CopyEntity findFirstByBookAndIsIssuedOrderByCopyId(BookEntity book, boolean isIssued);

    @Nullable
    long countByBookAndIsIssuedOrderByCopyId(BookEntity book, boolean isIssued);

    //CopyEntity findFirstBy(int copyId, boolean isIssued);

    CopyEntity deleteByCopyId(int copyId);

    CopyEntity deleteByBook(BookEntity bookEntity);

    @Query("SELECT new com.bravo.johny.dto.IssuedBook(b.bookId, b.title, b.author, b.genre, COUNT(c.isIssued)) " +
            "FROM CopyEntity c " +
            "JOIN c.book b " +
            "WHERE c.isIssued = 1" +
            "GROUP BY b.bookId, b.title, b.author, b.genre " +
            "HAVING COUNT(c.isIssued) > 0")
    List<IssuedBook> getAllIssuedBooks();
}
