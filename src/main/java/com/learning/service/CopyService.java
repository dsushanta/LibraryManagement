package com.learning.service;

import com.learning.dao.BookDAO;
import com.learning.dao.CopyDAO;
import com.learning.dto.Copy;
import com.learning.exception.DataNotFoundException;
import com.learning.webresource.filterbeans.CopyFilterBean;

import java.util.List;

public class CopyService {

    CopyDAO copyDAO;
    BookDAO bookDAO;

    public CopyService() {
        copyDAO = new CopyDAO();
        bookDAO = new BookDAO();
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
            throw new DataNotFoundException("No copy found with copy id : "+copyId);
        else
            return copy;
    }

    public Copy updateCopyDetails(Copy copy) {
        Copy copyToBeUpdated = copyDAO.getCopyWithCopyIdFromDatabase(copy.getCopyId());

        if(copyToBeUpdated.getCopyId() == 0)
            throw new DataNotFoundException("No copy found with copy id : "+copy.getCopyId());

        return copyDAO.udateCopyInDatabase(copy);
    }

    public void deleteCopyFromDatabase(int copyId) {

        Copy copy = copyDAO.getCopyWithCopyIdFromDatabase(copyId);

        if(copy.getCopyId() == 0)
            throw new DataNotFoundException("No copy found with copy id : "+copyId);
        else
            copyDAO.deleteCopyFromDatabase(copyId);
    }

    // ##################### PRIVATE METHODS ######################

}
