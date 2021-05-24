package com.bravo.johny.dao1;

import com.bravo.johny.config.ProjectConfig;
import com.bravo.johny.controller.filterbeans.BookIssueFilterBean;
import com.bravo.johny.controller.filterbeans.UserIssuedBookFilterBean;
import com.bravo.johny.dto.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookIssueDAO extends BaseDAO {

    private final String TABLE_NAME = "LI_BOOK_LIFE_CYCLE";
    LibraryConfig config;

    public BookIssueDAO() {
        super();
    }

    public BookIssue issueABook(BookIssue bookIssue) {
        String query = "INSERT INTO "+TABLE_NAME+"(" +
                "CopyId, BookId, UserName, IssueDate, ExpectedReturnDate) " +
                "VALUES(?, ?, ?, CURDATE(), ?)";

        config = new ConfigDAO().getConfigFromDatabase();
        long millis=System.currentTimeMillis();
        Date expectedReturnDate = new Date(millis);
        expectedReturnDate = addDaysToADate(expectedReturnDate, config.getNO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK());
        bookIssue.setExpectedReturnDate(expectedReturnDate);

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1, bookIssue.getCopyId());
            preparedStatement.setInt(2, bookIssue.getBookId());
            preparedStatement.setString(3, bookIssue.getUserName());
            preparedStatement.setDate(4, bookIssue.getExpectedReturnDate());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dbService.write(preparedStatement);

        String getIdQuery = "SELECT LAST_INSERT_ID() AS ID";
        preparedStatement = dbService.getPreparedStatement(getIdQuery);
        ResultSet rs = dbService.read(preparedStatement);
        int newBookIssueId=0;
        try {
            while(rs.next())
                newBookIssueId = rs.getInt("ID");
        }catch (SQLException e) {
            e.printStackTrace();
        }
        BookIssue newBookIssue = getBookIssueDetailsFromDatabase(newBookIssueId);
        config = null;

        return newBookIssue;
    }

    public int deleteBookIssueEntriesForABookFromDatabase(int bookId) {
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

    public int deleteBookIssueEntriesForACopyFromDatabase(int copyId) {
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

    public int deleteBookIssueFromDatabase(int bookIssueId) {
        int status;
        String query = "DELETE FROM "+TABLE_NAME+" WHERE BookIssueId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1, bookIssueId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        status = dbService.write(preparedStatement);
        closeDBConnection();

        return status;
    }

    public BookIssue getBookIssueDetailsFromDatabase(int bookIssueId) {
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE BookIssueId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,bookIssueId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        BookIssue bookIssue = new BookIssue();

        try {
            while(rs.next())
                setBookIssueObject(rs, bookIssue);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return bookIssue;
    }

    public BookIssue returnABook(BookIssue bookIssue) {
        String query = "UPDATE "+TABLE_NAME+" " +
                "SET IsReturned=True, ActualReturnDate=CURDATE(), Fine=?" + ", FineCleared=FALSE " +
                "WHERE BookIssueId=?";

        config = new ConfigDAO().getConfigFromDatabase();
        int numberOfDaysAfterExpectedReturnDate = getDelayInDaysFromCurrentDate(bookIssue.getExpectedReturnDate());
        int fine = getBookIssueDetailsFromDatabase(bookIssue.getBookIssueId()).getFine();

        if(numberOfDaysAfterExpectedReturnDate > 0)
            fine += numberOfDaysAfterExpectedReturnDate * config.getFINE_PER_DAY();
        bookIssue.setFine(fine);

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,bookIssue.getFine());
            preparedStatement.setInt(2,bookIssue.getBookIssueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbService.write(preparedStatement);

        bookIssue = getBookIssueDetailsFromDatabase(bookIssue.getBookIssueId());
        config = null;

        return bookIssue;
    }

    public BookIssue reissueABook(BookIssue bookIssue) {
        String query = "UPDATE "+TABLE_NAME+" " +
                "SET IsRenewed=True, RenewDate=CURDATE(), ExpectedReturnDate=?, Fine=?" + ", FineCleared=FALSE " +
                "WHERE BookIssueId=?";

        config = new ConfigDAO().getConfigFromDatabase();
        int numberOfDaysAfterExpectedReturnDate = getDelayInDaysFromCurrentDate(bookIssue.getExpectedReturnDate());
        int fine = getBookIssueDetailsFromDatabase(bookIssue.getBookIssueId()).getFine();
        if(numberOfDaysAfterExpectedReturnDate > 0)
            fine += numberOfDaysAfterExpectedReturnDate * config.getFINE_PER_DAY();
        bookIssue.setFine(fine);

        long millis=System.currentTimeMillis();
        Date expectedReturnDate = new Date(millis);
        expectedReturnDate = addDaysToADate(expectedReturnDate, config.getNO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK());
        bookIssue.setExpectedReturnDate(expectedReturnDate);

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setDate(1,bookIssue.getExpectedReturnDate());
            preparedStatement.setInt(2,bookIssue.getFine());
            preparedStatement.setInt(3, bookIssue.getBookIssueId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbService.write(preparedStatement);

        bookIssue = getBookIssueDetailsFromDatabase(bookIssue.getBookIssueId());
        config = null;

        return bookIssue;
    }

    public void clearAllDuesOfAUser(String username) {
        String query = "UPDATE "+TABLE_NAME+" " +
                "SET FineCleared=True WHERE UserName=?";

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1, username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbService.write(preparedStatement);
    }

    public List<BookIssue> getBookIssueEntriesWithFilterFromDatabase(BookIssueFilterBean bookIssueFilterBean) {
        List<BookIssue> allBookIssueEntries = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM "+TABLE_NAME);
        int bookId = bookIssueFilterBean.getBookId();
        String username = bookIssueFilterBean.getUsername();
        int offset = bookIssueFilterBean.getOffset();
        int limit = bookIssueFilterBean.getLimit();

        boolean anyfilter = anyFilterInTheURL(bookIssueFilterBean);
        if(anyfilter) {
            query.append(" WHERE");
            if(bookId != 0)
                query.append(" BookId='").append(bookId).append("' AND");
            if(username != null)
                query.append(" UserName='").append(username).append("' AND");

            query = query.delete(query.length()-4, query.length());
        }

        if(offset >=0 && limit >0)
            query.append(" LIMIT ").append(offset).append(",").append(limit);

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query.toString());

        ResultSet rs = dbService.read(preparedStatement);

        try {
            while(rs.next()) {
                BookIssue bookIssue = new BookIssue();
                setBookIssueObject(rs, bookIssue);

                allBookIssueEntries.add(bookIssue);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return allBookIssueEntries;
    }

    public boolean checkIfBookIssueEntryExists(int bookIssueId) {
        boolean bookIssueIdExists = false;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE BookIssueId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,bookIssueId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            if(rs.next())
                bookIssueIdExists = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return bookIssueIdExists;
    }

    public boolean checkIfBookIsAlreadyReIssued(int bookIssueId) {
        boolean bookReIssued = false;
        String query = "SELECT IsRenewed FROM "+TABLE_NAME+" WHERE BookIssueId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,bookIssueId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            while(rs.next()) {
                bookReIssued = rs.getBoolean("IsRenewed");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return bookReIssued;
    }

    public boolean checkIfBookIsAlreadyReturned(int bookIssueId) {
        boolean bookReturned = false;
        String query = "SELECT IsReturned FROM "+TABLE_NAME+" WHERE BookIssueId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,bookIssueId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            while(rs.next()) {
                bookReturned = rs.getBoolean("IsReturned");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return bookReturned;
    }

    public List<UserIssuedBook> getBooksIssuedByAUser(String username, UserIssuedBookFilterBean bean) {
        List<UserIssuedBook> allBookIssued = new ArrayList<>();

        int bookId = bean.getBookId();
        int offset = bean.getOffset();
        int limit = bean.getLimit();

        StringBuilder query = new StringBuilder("SELECT BookIssueId, LB.BookId AS BookId, Title, " +
                "Author, Genre, IssueDate, RenewDate, ExpectedReturnDate, ActualReturnDate, Fine, FineCleared ");
        query.append("FROM LI_BOOKS LB JOIN "+TABLE_NAME+" LBLC ");
        query.append("ON LB.BookId = LBLC.BookId ");
        query.append("WHERE UserName=?");

        if(bookId != 0) {
            query.append(" AND");
            if(bookId != 0)
                query.append(" BookId=").append(bookId);
        }

        if(offset >=0 && limit >0)
            query.append(" LIMIT ").append(offset).append(",").append(limit);

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query.toString());

        try {
            preparedStatement.setString(1,username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet rs = dbService.read(preparedStatement);

        try {
            while(rs.next()) {
                UserIssuedBook issuedBook = new UserIssuedBook();
                setUserIssuedBookObject(rs, issuedBook);
                if(issuedBook.getActualReturnDate() == null)
                    issuedBook.setReturned(false);
                else
                    issuedBook.setReturned(true);

                allBookIssued.add(issuedBook);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return allBookIssued;
    }

    public int getTotalDuesOfAUser(String userName) {
        int totalFine = 0;
        String query = "SELECT SUM(Fine) AS total_fine " +
                "FROM "+TABLE_NAME+" WHERE UserName=? AND FineCleared=False " +
                "GROUP BY UserName";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            while(rs.next()) {
                totalFine = rs.getInt("total_fine");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return totalFine;
    }

    public List<IssuedBook> getAllIssuedBooks() {
        List<IssuedBook> allIssuedBooks = new ArrayList<>();
        String query = "SELECT LB.BookId, LB.Title, LB.Author, LB.Genre, SUM(IsIssued) AS Count " +
                "FROM LI_BOOKS LB " +
                "JOIN LI_BOOK_COPIES LBC " +
                "on LB.BookId = LBC.BookId " +
                "GROUP BY LB.BookId, LB.Title, LB.Author, LB.Genre " +
                "HAVING SUM(IsIssued) > 0";

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query.toString());
        ResultSet rs = dbService.read(preparedStatement);
        try {
            while(rs.next()) {
                IssuedBook issuedBook = new IssuedBook();
                setIssuedBookObject(rs, issuedBook);
                allIssuedBooks.add(issuedBook);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return allIssuedBooks;
    }

    public List<User> getIssuedUsers(String bookId) {

        String query = "SELECT LU.UserName, FirstName, LastName " +
                "FROM LI_BOOK_LIFE_CYCLE LBLC " +
                "JOIN LI_USERS LU " +
                "ON LBLC.UserName = LU.UserName " +
                "WHERE BookId = ? " +
                "AND ActualReturnDate IS NULL";

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        List<User> users = new ArrayList<>();
        try {
            while(rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("UserName"));
                user.setFirstname(rs.getString("FirstName"));
                user.setLastname(rs.getString("LastName"));
                users.add(user);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return users;
    }

    // ##################### PRIVATE METHODS ######################

    private boolean anyFilterInTheURL(BookIssueFilterBean bookIssueFilterBean) {
        if(bookIssueFilterBean.getBookId() != 0 || bookIssueFilterBean.getUsername() != null)
            return true;
        else
            return false;
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

    private void setUserIssuedBookObject(ResultSet rs, UserIssuedBook issuedBook) {
        try {
            issuedBook.setBookIssueId(rs.getInt("BookIssueId"));
            issuedBook.setBookId(rs.getInt("BookId"));
            issuedBook.setTitle(rs.getString("Title"));
            issuedBook.setAuthor(rs.getString("Author"));
            issuedBook.setGenre(rs.getString("Genre"));
            issuedBook.setIssueDate(rs.getDate("IssueDate"));
            issuedBook.setReIssueDate(rs.getDate("RenewDate"));
            issuedBook.setExpectedReturnDate(rs.getDate("ExpectedReturnDate"));
            issuedBook.setActualReturnDate(rs.getDate("ActualReturnDate"));
            issuedBook.setFine(rs.getInt("Fine"));
            issuedBook.setFineCleared(rs.getBoolean("FineCleared"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setBookIssueObject(ResultSet rs, BookIssue bookIssue) {
        try {
            bookIssue.setBookIssueId(rs.getInt("BookIssueId"));
            bookIssue.setCopyId(rs.getInt("CopyId"));
            bookIssue.setBookId(rs.getInt("BookId"));
            bookIssue.setUserName(rs.getString("UserName"));
            bookIssue.setReturned(rs.getBoolean("IsReturned"));
            bookIssue.setReissued(rs.getBoolean("IsRenewed"));
            bookIssue.setIssueDate(rs.getDate("IssueDate"));
            bookIssue.setExpectedReturnDate(rs.getDate("ExpectedReturnDate"));
            bookIssue.setActualReturnDate(rs.getDate("ActualReturnDate"));
            bookIssue.setReIssueDate(rs.getDate("RenewDate"));
            bookIssue.setFine(rs.getInt("Fine"));
            bookIssue.setFineCleared(rs.getBoolean("FineCleared"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setIssuedBookObject(ResultSet rs, IssuedBook issuedBook) {
        try {
            issuedBook.setBookId(rs.getInt("BookId"));
            issuedBook.setTitle(rs.getString("Title"));
            issuedBook.setAuthor(rs.getString("Author"));
            issuedBook.setGenre(rs.getString("Genre"));
            issuedBook.setNumberOfCopiesIssued(rs.getInt("Count"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}