package com.bravo.johny.service;

import com.bravo.johny.controller.filterbeans.CopyFilterBean;
import com.bravo.johny.dto.Copy;
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
public class CopyService {

    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookLifeCycleRepository bookLifeCycleRepository;

    public Copy addNewCopy(int bookId) {

        BookEntity book = bookRepository.findFirstByBookId(bookId);
        CopyEntity copyEntity = new CopyEntity(book);

        return prepareCopyDTOFromCopyEntity(copyRepository.save(copyEntity));
    }

    public List<Copy> getCopies(CopyFilterBean copyBean) {

        List<CopyEntity> copyEntities;
        List<Copy> copies = new ArrayList<>();
        int bookId = copyBean.getBookId();
        int offset = copyBean.getOffset();
        int limit = copyBean.getLimit();

        BookEntity bookEntity = bookRepository.findFirstByBookId(bookId);

        if(offset >=0 && limit >0) {
            Pageable pageable = PageRequest.of(offset, limit);
            copyEntities = copyRepository.findByBook(bookEntity, pageable);
        } else {
            copyEntities = copyRepository.findByBook(bookEntity);
        }
        copyEntities.forEach(copyEntity -> copies.add(prepareCopyDTOFromCopyEntity(copyEntity)));

        return copies;
    }

    public Copy getCopyDetails(int copyId) {

        CopyEntity copyEntity = copyRepository.findFirstByCopyId(copyId);

        if(copyEntity == null)
            throwBadRequestRuntimeException("No copy found with copy id : "+copyId);
        else
            return prepareCopyDTOFromCopyEntity(copyEntity);

        return null;
    }

    public Copy updateCopyDetails(Copy copy) {

        CopyEntity copyEntity = copyRepository.findFirstByCopyId(copy.getCopyId());

        if(copyEntity == null)
            throwBadRequestRuntimeException("No copy found with copy id : "+copy.getCopyId());
        else {
            copyEntity.setIssued(copy.isIssued());
            //copyEntity.setIsIssued(copy.isIssued() ? 1 : 0);
            copyEntity = copyRepository.save(copyEntity);
        }

        return prepareCopyDTOFromCopyEntity(copyEntity);
    }

    public void deleteCopyFromDatabase(int copyId) {

        CopyEntity copyEntity = copyRepository.findFirstByCopyId(copyId);

        if(copyEntity == null)
            throwBadRequestRuntimeException("No copy found with copy id : "+copyId);
        else if(copyRepository.findFirstByCopyIdAndAndIsIssued(copyId, true) != null)
            throwBadRequestRuntimeException("CopyEntity entry can not be removed as copy of the book has been issued to a customer !!");
        else {
            bookLifeCycleRepository.deleteByCopy(copyEntity);
            copyRepository.deleteByCopyId(copyId);
        }
    }


    // ##################### PRIVATE METHODS ######################


    private Copy prepareCopyDTOFromCopyEntity(CopyEntity copyEntity) {

        Copy copy = new Copy();
        copy.setCopyId(copyEntity.getCopyId());
        copy.setBookId(copyEntity.getBook().getBookId());
        copy.setIssued(copyEntity.isIssued());
        //copy.setIssued(copyEntity.getIsIssued()==1 ? true : false);

        return copy;
    }
}
