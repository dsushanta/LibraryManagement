package com.learning.dao;

import com.learning.dto.Copy;
import com.learning.webresource.filterbeans.CopyFilterBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CopyDAO extends BaseDAO {

    private final String TABLE_NAME = "LI_BOOK_COPIES";

    public CopyDAO() {
        super();
    }

    public Copy addNewCopyIntoDatabase(int bookId) {
        String query = "INSERT INTO "+TABLE_NAME+"(BookId) VALUES(?)";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1, bookId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dbService.write(preparedStatement);

        String getIdQuery = "SELECT LAST_INSERT_ID() AS ID";
        preparedStatement = dbService.getPreparedStatement(getIdQuery);
        ResultSet rs = dbService.read(preparedStatement);
        int newCopyId=0;
        try {
            while(rs.next())
                newCopyId = rs.getInt("ID");
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Copy newCopy = getCopyWithCopyIdFromDatabase(newCopyId);

        return newCopy;
    }

    public int deleteCopyFromDatabase(int copyId) {
        int status;
        String query = "DELETE FROM "+TABLE_NAME+" WHERE CopyId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1, copyId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        status = dbService.write(preparedStatement);
        closeDBConnection();

        return status;
    }

    public int deleteAllCopiesOfABookFromDatabase(int bookId) {
        int status;
        String query = "DELETE FROM "+TABLE_NAME+" WHERE BookId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1, bookId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        status = dbService.write(preparedStatement);
        closeDBConnection();

        return status;
    }

    public Copy getCopyWithCopyIdFromDatabase(int copyId) {
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE CopyId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,copyId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        Copy copy = new Copy();

        try {
            while(rs.next())
                setCopyObject(rs, copy);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return copy;
    }

    public Copy udateCopyInDatabase(Copy copy) {
        Copy copyToBeUpdated = getCopyWithCopyIdFromDatabase(copy.getCopyId());
        if(copy.getBookId() != 0)
            copyToBeUpdated.setBookId(copy.getBookId());

        copyToBeUpdated.setIssued(copy.isIssued());

        String query = "UPDATE "+TABLE_NAME+" SET IsIssued=? WHERE CopyId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setBoolean(1,copyToBeUpdated.isIssued());
            preparedStatement.setInt(2,copyToBeUpdated.getCopyId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbService.write(preparedStatement);

        copy = getCopyWithCopyIdFromDatabase(copyToBeUpdated.getCopyId());

        return copy;
    }

    public List<Copy> getCopiesWithFilterFromDatabase(CopyFilterBean copyBean) {
        List<Copy> allCopies = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM "+TABLE_NAME+"");
        int bookId = copyBean.getBookId();
        int offset = copyBean.getOffset();
        int limit = copyBean.getLimit();

        boolean anyfilter = anyFilterInTheURL(copyBean);
        if(anyfilter) {
            query.append(" WHERE");
            if(bookId != 0)
                query.append(" BookId=").append(bookId);

        }

        if(offset >=0 && limit >0)
            query.append(" LIMIT ").append(offset).append(",").append(limit);

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query.toString());

        ResultSet rs = dbService.read(preparedStatement);

        try {
            while(rs.next()) {
                Copy copy = new Copy();
                setCopyObject(rs, copy);

                allCopies.add(copy);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return allCopies;
    }

    public int getAvailableCopyOfABook(int bookId) {
        String query = "SELECT CopyId FROM "+TABLE_NAME+" WHERE BookId=? AND IsIssued=False ORDER BY CopyId LIMIT 1";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        int availableCopyId = 0;

        try {
            while(rs.next())
                availableCopyId = rs.getInt("CopyId");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return availableCopyId;
    }

    public boolean checkIfAnyCopyOfABookIsIssued(int bookId) {
        boolean bookIssued = false;
        String query = "SELECT CopyId FROM "+TABLE_NAME+" WHERE BookId=? AND IsIssued=True";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);

        try {
            if(rs.next())
                bookIssued = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return bookIssued;
    }

    public boolean checkIfCopyOfABookIsIssued(int copyId) {
        boolean copyIssued = false;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE CopyId=? AND IsIssued=True";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,copyId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);

        try {
            if(rs.next())
                copyIssued = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return copyIssued;
    }

    // ##################### PRIVATE METHODS ######################

    private boolean anyFilterInTheURL(CopyFilterBean copyBean) {
        if(copyBean.getBookId() != 0)
            return true;
        else
            return false;
    }

    private void setCopyObject(ResultSet rs, Copy copy) {
        try {
            copy.setCopyId(rs.getInt("CopyId"));
            copy.setBookId(rs.getInt("BookId"));
            copy.setIssued(rs.getBoolean("IsIssued"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
