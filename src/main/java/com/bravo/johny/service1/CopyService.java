package com.bravo.johny.service1;

import com.bravo.johny.controller.filterbeans.CopyFilterBean;
import com.bravo.johny.dao.BookIssueDAO;
import com.bravo.johny.dao.CopyDAO;
import com.bravo.johny.dto.Copy;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bravo.johny.utils.CommonUtils.throwBadRequestRuntimeException;


//@Service
public class CopyService {

    private CopyDAO copyDAO;
    private BookIssueDAO bookIssueDAO;

    public CopyService() {
        copyDAO = new CopyDAO();
        bookIssueDAO = new BookIssueDAO();
    }

    public Copy addNewCopy(int bookId) {

        return copyDAO.addNewCopyIntoDatabase(bookId);
    }

    public List<Copy> getCopies(CopyFilterBean copyBean) {
        return copyDAO.getCopiesWithFilterFromDatabase(copyBean);
    }

    public Copy getCopyDetails(int copyId) {
        Copy copy = copyDAO.getCopyWithCopyIdFromDatabase(copyId);

        if(copy.getCopyId() == 0)
            throwBadRequestRuntimeException("No copy found with copy id : "+copyId);
        else
            return copy;

        return null;
    }

    public Copy updateCopyDetails(Copy copy) {
        Copy copyToBeUpdated = copyDAO.getCopyWithCopyIdFromDatabase(copy.getCopyId());

        if(copyToBeUpdated.getCopyId() == 0)
            throwBadRequestRuntimeException("No copy found with copy id : "+copy.getCopyId());

        return copyDAO.udateCopyInDatabase(copy);
    }

    public void deleteCopyFromDatabase(int copyId) {

        Copy copy = copyDAO.getCopyWithCopyIdFromDatabase(copyId);

        if(copy.getCopyId() == 0)
            throwBadRequestRuntimeException("No copy found with copy id : "+copyId);
        else if(copyDAO.checkIfCopyOfABookIsIssued(copyId))
            throwBadRequestRuntimeException("CopyEntity entry can not be removed as copy of the book has been issued to a customer !!");
        else {
            bookIssueDAO.deleteBookIssueEntriesForACopyFromDatabase(copyId);
            copyDAO.deleteCopyFromDatabase(copyId);
        }
    }
}
