/*
 * LibraryModel.java
 * Author:
 * Created on:
 */



import javax.swing.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class LibraryModel {
    // For use in creating dialogs and making them modal
    private JFrame dialogParent;
    private Connection connection = null;
    private Statement statement;
    private ResultSet results;

    public LibraryModel(JFrame parent, String userid, String password) {
        dialogParent = parent;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql:" + "//localhost:5432/" + userid + "_jdbc";
            connection = DriverManager.getConnection(url, userid, password);
            connection.setReadOnly(true);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can not find" +
                    "the driver class: " +
                    "\nEither I have not installed it" +
                    "properly or \n postgresql.jar " +
                    " file is not in my CLASSPATH");
        } catch (SQLException sqlex) {
            System.out.println("Can not connect");
            System.out.println(sqlex.getMessage());
        }

    }

    /**
     * Gets a particular book from db
     * @return book data in a string
     */
    public String bookLookup(int isbn) {
        String result = "Unable to get books";

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM book WHERE isbn =" + isbn);

            result = this.getBooks(results);
            statement.close();
        } catch (SQLException sqlex) {
            result = "An exception" +
                    "while creating a statement," +
                    "probably means I am no longer" +
                    "connected:\n" + sqlex.getMessage();
        }

        return result;
    }

    /**
    * Gets all books from db
    * @return all books data in a string
    */
    public String showCatalogue() {
        String result = "Unable to get books";

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM book");

            result = this.getBooks(results);
            statement.close();
        } catch (SQLException sqlex) {
            result = "An exception" +
                    "while creating a statement," +
                    "probably means I am no longer" +
                    "connected:\n" + sqlex.getMessage();
        }

        return result;
    }

    /**
     * Shows all the books that are currently on loan
     * @return String containing all loaned book data
     */
    public String showLoanedBooks() {
        String result = "Unable to get books";

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM book AS b WHERE b.numofcop - b.numleft != 0");

            result = this.getBooks(results);
            statement.close();
        } catch (SQLException sqlex) {
            result = "An exception" +
                    "while creating a statement," +
                    "probably means I am no longer" +
                    "connected:\n" + sqlex.getMessage();
        }

        return result;
    }

    /**
     * Returns an author that corresponds to a specific customerID
     * @param authorID, to identify target author
     * @return Customer information
     */
    public String showAuthor(int authorID) {
        String result = "Unable to get author given that Id";

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM author WHERE authorid =" + authorID);

            result = this.getAuthor(results);
            statement.close();
        } catch (SQLException sqlex) {
            result = "An exception" +
                    "while creating a statement," +
                    "probably means I am no longer" +
                    "connected:\n" + sqlex.getMessage();
        }

        return result;
    }

    /**
     * Gets all authors from db
     * @return all authors data in a string
     */
    public String showAllAuthors() {
        String result = "Unable to get authors";

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM author");

            result = this.getAuthor(results);
            statement.close();
        } catch (SQLException sqlex) {
            result = "An exception" +
                    "while creating a statement," +
                    "probably means I am no longer" +
                    "connected:\n" + sqlex.getMessage();
        }

        return result;
    }

    /**
     * Returns a customer that corresponds to a specific customerID
     * @param customerID, to identify target customer
     * @return Customer information
     */
    public String showCustomer(int customerID) {
        String result = "Unable to get customer given that Id";

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM customer WHERE customerID =" + customerID);

            result = this.getCustomer(results);
            statement.close();
        } catch (SQLException sqlex) {
            result = "An exception" +
                    "while creating a statement," +
                    "probably means I am no longer" +
                    "connected:\n" + sqlex.getMessage();
        }

        return result;
    }

    /**
     * Shows all current customers
     * @return String of all customers
     */
    public String showAllCustomers() {
        String result = "Unable to get customers";

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM customer");

            result = this.getCustomer(results);
            statement.close();
        } catch (SQLException sqlex) {
            result = "An exception" +
                    "while creating a statement," +
                    "probably means I am no longer" +
                    "connected:\n" + sqlex.getMessage();
        }

        return result;
    }

    /**
     * Helper method that gets all results from a given result set and returns them as a string
     * @param results, ResultSet to get information from
     * @return String of all found information
     * @throws SQLException
     */
    public String getCustomer(ResultSet results) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (results.next()) {
            sb.append(results.getInt(1));
            sb.append(", ");
            sb.append(results.getString("f_name"));
            sb.append(", ");
            sb.append(results.getString("l_name"));
            sb.append(", ");
            sb.append(results.getString("city"));
            sb.append("\n");
        }

        String data = sb.toString();
        return (data.length() > 0) ? data : "Data does not exist";
    }

    /**
     * Helper method that gets all results from a given result set and returns them as a string
     * @param results, ResultSet to get information from
     * @return String of all found information
     * @throws SQLException
     */
    public String getAuthor(ResultSet results) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (results.next()) {
            sb.append(results.getInt(1));
            sb.append(", ");
            sb.append(results.getString("name"));
            sb.append(", ");
            sb.append(results.getString("surname"));
            sb.append("\n");
        }

        String data = sb.toString();
        return (data.length() > 0) ? data : "Data does not exist";
    }

    /**
     * Helper method that gets all results from a given result set and returns them as a string
     * @param results, ResultSet to get information from
     * @return String of all found information
     * @throws SQLException
     */
    public String getBooks(ResultSet results) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (results.next()) {
            sb.append(results.getInt(1));
            sb.append(", ");
            sb.append(results.getString("title"));
            sb.append(", ");
            sb.append(results.getInt(3));
            sb.append(", ");
            sb.append(results.getInt(4));
            sb.append(", ");
            sb.append(results.getInt(5));
            sb.append("\n");
        }

        String data = sb.toString();
        return (data.length() > 0) ? data : "No books found";
    }

    /**
     * Allows for borrowing books
     * @param isbn book id
     * @param customerID customer number
     * @param day day of return
     * @param month month of return
     * @param year year of return
     * @return String confirmation or error message
     */
    public String borrowBook(int isbn, int customerID, int day, int month, int year) {
        /*
        1. Check whether the customer exists (and lock him/her as if the delete option were available).
        2. Lock the book (if it exists, and if a copy is available).
        3. Insert an appropriate tuple in the Cust_Book table.
        4. Update the Book table, and
        5. Commit the transaction (if actions were all successful, otherwise rollback)
         */
        String findCustomer = "SELECT * FROM customer WHERE customerid =" + customerID;
        String findBook = "SELECT * FROM book WHERE isbn = " + isbn + " AND numleft > 0";
        String insertBook = "INSERT INTO cust_book (isbn, duedate, customerid) VALUES(?, ?, ?)";
        String updateBook = "UPDATE book SET numleft = numleft - 1 WHERE isbn = " + isbn;

        try {
            connection.setReadOnly(false);
            connection.setAutoCommit(false);
            PreparedStatement findStatement = connection.prepareStatement(findCustomer);

//            Ensure customer exists
            if(findStatement.execute()) {
//                Find book
                PreparedStatement findBookStatement = connection.prepareStatement(findBook);
                if(findBookStatement.execute()) {
//                    Insert
                    PreparedStatement insertBookStatment = connection.prepareStatement(insertBook);
                    insertBookStatment.setInt(1, isbn);
                    insertBookStatment.setDate(2, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day).getTime()));
                    insertBookStatment.setInt(3, customerID);
                    insertBookStatment.executeUpdate();

//                    Update book table
                    PreparedStatement updateBookStatement = connection.prepareStatement(updateBook);
                    updateBookStatement.executeUpdate();

//                    Commit transaction
                    connection.commit();
                    findStatement.close();
                    findBookStatement.close();
                    insertBookStatment.close();
                    return "Book is now borrowed";
                }
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Revert");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return e.getMessage();
        }

        return "Book was not able to be borrowed";
    }

    public String returnBook(int isbn, int customerid) {
        String findCustomer = "SELECT * FROM customer WHERE customerid =" + customerid;
        String findBook = "SELECT * FROM book WHERE isbn = " + isbn + " AND numleft > 0";
        String removeBooking = "DELETE ALL FROM cust_book WHERE isbn = " + isbn + " AND customerid = " + customerid;
        String updateBook = "UPDATE book SET numleft = numleft + 1 WHERE isbn = " + isbn;

        try {
            connection.setReadOnly(false);
            connection.setAutoCommit(false);
            PreparedStatement findCustomerStatement = connection.prepareStatement(findCustomer);

//            Ensure customer exists
            if(findCustomerStatement.execute()) {
//                Find book exists
                PreparedStatement findBookStatement = connection.prepareStatement(findBook);
                if(findBookStatement.execute()) {
//                    Remove
                    PreparedStatement insertBookStatment = connection.prepareStatement(removeBooking);
                    insertBookStatment.executeUpdate();

//                    Update book table
                    PreparedStatement updateBookStatement = connection.prepareStatement(updateBook);
                    updateBookStatement.executeUpdate();

//                    Commit transaction
                    connection.commit();
                    findCustomerStatement.close();
                    findBookStatement.close();
                    insertBookStatment.close();
                    return "Book is now borrowed";
                }
            }

        } catch (SQLException e) {
            System.out.println("Revert");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return e.getMessage();
        }

        return "Book was not able to be returned";
    }

    /**
     * Closes the database connection
     */
    public void closeDBConnection() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String deleteCus(int customerID) {
        return "";
    }

    public String deleteAuthor(int authorID) {
        return "Delete Author";
    }

    public String deleteBook(int isbn) {
        return "Delete Book";
    }

}