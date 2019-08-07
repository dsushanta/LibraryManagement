package com.learning.dao;

import com.learning.dto.Book;
import com.learning.dto.Genre;
import com.learning.webresource.filterbeans.BookFilterBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO extends BaseDAO {

    private final String TABLE_NAME = "LI_BOOKS";

    public BookDAO() {
        super();
    }

    public Book addNewBookIntoDatabase(Book book) {
        String query = "INSERT INTO "+TABLE_NAME+"(Title, Description, Author, Genre) VALUES(?, ?, ?, ?)";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getDescription());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setString(4, book.getGenre());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dbService.write(preparedStatement);

        String getIdQuery = "SELECT LAST_INSERT_ID() AS ID";
        preparedStatement = dbService.getPreparedStatement(getIdQuery);
        ResultSet rs = dbService.read(preparedStatement);
        int newBookId=0;
        try {
            while(rs.next())
                newBookId = rs.getInt("ID");
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Book newBook = getBookWithBookIdFromDatabase(newBookId);

        closeDBConnection();

        return newBook;
    }

    public int deleteBookFromDatabase(int bookId) {
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

    public Book getBookWithBookIdFromDatabase(int bookId) {
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE BookId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setInt(1,bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        Book book = new Book();

        try {
            while(rs.next())
               setBookObject(rs, book);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return book;
    }

    public Book updateBookInDatabase(Book book) {
        Book bookToBeUpdated = getBookWithBookIdFromDatabase(book.getBookId());
        if(book.getTitle() != null && !book.getTitle().isEmpty())
            bookToBeUpdated.setTitle(book.getTitle());
        if(book.getDescription() != null && !book.getDescription().isEmpty())
            bookToBeUpdated.setDescription(book.getDescription());
        if(book.getAuthor() != null && !book.getAuthor().isEmpty())
            bookToBeUpdated.setAuthor(book.getAuthor());
        if(book.getGenre() != null && !book.getGenre().isEmpty())
            bookToBeUpdated.setGenre(book.getGenre());

        String query = "UPDATE "+TABLE_NAME+" SET Title=?, Description=?, Author=?, Genre=? WHERE BookId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,bookToBeUpdated.getTitle());
            preparedStatement.setString(2,bookToBeUpdated.getDescription());
            preparedStatement.setString(3,bookToBeUpdated.getAuthor());
            preparedStatement.setString(6,bookToBeUpdated.getGenre());
            preparedStatement.setInt(5, bookToBeUpdated.getBookId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbService.write(preparedStatement);

        book = getBookWithBookIdFromDatabase(bookToBeUpdated.getBookId());

        return book;
    }

    public List<Book> getBooksWithFilterFromDatabase(BookFilterBean bookBean) {
        List<Book> allBooks = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM "+TABLE_NAME+"");
        String title = bookBean.getTitle();
        String author = bookBean.getAuthor();
        String genre = bookBean.getGenre();
        int offset = bookBean.getOffset();
        int limit = bookBean.getLimit();

        boolean anyfilter = anyFilterInTheURL(bookBean);
        if(anyfilter) {
            query.append(" WHERE");
            if(title != null)
                query.append(" Title LIKE '%").append(title).append("%'").append(" AND");
            if(author != null)
                query.append(" Author LIKE '%").append(author).append("%'").append(" AND");
            if(genre != null)
                query.append(" Genre LIKE '%").append(genre).append("%'").append(" AND");

            query = query.delete(query.length()-4, query.length());
        }

        if(offset >=0 && limit >0)
            query.append(" LIMIT ").append(offset).append(",").append(limit);

        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query.toString());

        ResultSet rs = dbService.read(preparedStatement);

        try {
            while(rs.next()) {
                Book book = new Book();
                setBookObject(rs, book);

                allBooks.add(book);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        closeDBConnection();

        return allBooks;
    }

    public int getBookIdForTitleAndAuthorFromDatabase(String title, String author) {

        int bookId = 0;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE Title=? AND Author=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,author);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            while(rs.next())
                bookId = rs.getInt("BookId");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return bookId;
    }

    public boolean checkIfBookExistsInDatabase(String title, String author) {

        boolean bookExists = false;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE Title=? AND Author=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,author);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet rs = dbService.read(preparedStatement);
        try {
            if(rs.next())
                bookExists = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return bookExists;
    }

    public boolean checkIfBookExistsInDatabase(int bookId) {

        boolean bookExists = false;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE BookId=?";
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
                bookExists = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return bookExists;
    }

    public List<Genre> getAllGenre() {

        List<Genre> genres = new ArrayList();
        String query = "SELECT Genre FROM "+TABLE_NAME+" GROUP BY Genre";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        ResultSet rs = dbService.read(preparedStatement);
        try {
            while(rs.next()) {
                Genre genre = new Genre();
                genre.setGenre(rs.getString("Genre"));
                genres.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDBConnection();

        return genres;
    }

    public void updateBookAvailabilityStatus(int bookId, boolean availabilityStatus) {
        String query = "UPDATE "+TABLE_NAME+" SET Available=? WHERE BookId=?";
        dbService = getDBService();
        PreparedStatement preparedStatement = dbService.getPreparedStatement(query);

        try {
            preparedStatement.setBoolean(1,availabilityStatus);
            preparedStatement.setInt(2,bookId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbService.write(preparedStatement);

        closeDBConnection();
    }

    // ##################### PRIVATE METHODS ######################

    private boolean anyFilterInTheURL(BookFilterBean bookBean) {
        if(bookBean.getAuthor() != null || bookBean.getTitle() != null || bookBean.getGenre() != null)
            return true;
        else
            return false;
    }

    private void setBookObject(ResultSet rs, Book book){
        try {
            book.setBookId(rs.getInt("BookId"));
            book.setTitle(rs.getString("Title"));
            book.setDescription(rs.getString("Description"));
            book.setAuthor(rs.getString("Author"));
            book.setGenre(rs.getString("Genre"));
            book.setLikes(rs.getInt("Likes"));
            book.setAvailable(rs.getBoolean("Available"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
