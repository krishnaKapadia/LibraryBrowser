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

import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;
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
            //connection.setReadOnly(false);
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
            //connection.setReadOnly(true);
            Statement statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM book WHERE isbn =" + isbn);

            result = this.getBooks(results);
            statement.close();
            //connection.setReadOnly(false);
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
            //connection.setReadOnly(true);
            Statement statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM book");

            result = this.getBooks(results);
            statement.close();
            //connection.setReadOnly(false);
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
            //connection.setReadOnly(true);
            Statement statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM book AS b WHERE b.numofcop - b.numleft != 0");

            result = this.getBooks(results);
            statement.close();
            //connection.setReadOnly(false);
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
            //connection.setReadOnly(true);
            Statement statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM author WHERE authorid =" + authorID);

            result = this.getAuthor(results);
            statement.close();
            //connection.setReadOnly(false);
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
            //connection.setReadOnly(true);

            Statement statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM author");

            result = this.getAuthor(results);
            statement.close();
            //connection.setReadOnly(false);
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
            //connection.setReadOnly(true);
            Statement statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM customer WHERE customerID =" + customerID);

            result = this.getCustomer(results);
            statement.close();
            //connection.setReadOnly(false);
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
            //connection.setReadOnly(true);
            Statement statement = connection.createStatement();
            results = statement.executeQuery("SELECT * FROM customer");

            result = this.getCustomer(results);
            statement.close();
            //connection.setReadOnly(false);
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
        String returnString = "";
        String findCustomer = "SELECT * FROM customer WHERE customerid =" + customerID;
        String findBook = "SELECT * FROM book WHERE isbn = " + isbn + " AND numleft > 0";
        String insertBook = "INSERT INTO cust_book (isbn, duedate, customerid) VALUES(?, ?, ?)";
        String updateBook = "UPDATE book SET numleft = numleft - 1 WHERE isbn = " + isbn +  " AND numleft > 0";
        PreparedStatement findStatement = null, findBookStatement = null, insertBookStatment = null, updateBookStatement = null;

        try {
            //connection.setReadOnly(true);
            connection.setAutoCommit(false);
            findStatement = connection.prepareStatement(findCustomer);

//            Ensure customer exists
            if(findStatement.executeQuery().next()) {
//                Find book
                findBookStatement = connection.prepareStatement(findBook);
                if(findBookStatement.executeQuery().next()) {
//                    Insert
                    insertBookStatment = connection.prepareStatement(insertBook);
                    insertBookStatment.setInt(1, isbn);
                    insertBookStatment.setDate(2, new Date(new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day).getTime()));
                    insertBookStatment.setInt(3, customerID);

                    if(insertBookStatment.executeUpdate() == 0) {
                        returnString = "Could not borrow book";
                    } else {
                        int input = createConfirmDialog("Transaction intermission");
                        if(input == 0) {
                            //                    Update book table
                            updateBookStatement = connection.prepareStatement(updateBook);
                            if(updateBookStatement.executeUpdate() == 0) {
                                returnString = "Could not update book inventory";
                            } else {
                                //                    Commit transaction
                                connection.commit();
                                findStatement.close();
                                findBookStatement.close();
                                insertBookStatment.close();
                                return "Book is now borrowed";
                            }
                        }
                    }
                } else {
                    returnString = "Book not found";
                }
            } else {
                returnString = "Customer not found";
            }

        } catch (SQLException | ParseException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                if(findStatement != null) findStatement.close();
                if(findBookStatement != null) findBookStatement.close();
                if(insertBookStatment != null) insertBookStatment.close();
                if(updateBookStatement != null) updateBookStatement.close();
                //connection.setReadOnly(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return e.getMessage();
        }

//                    Commit transaction
        try {
            connection.rollback();
            if(findStatement != null) findStatement.close();
            if(findBookStatement != null) findBookStatement.close();
            if(insertBookStatment != null) insertBookStatment.close();
            if(updateBookStatement != null) updateBookStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnString;
    }

    public String returnBook(int isbn, int customerid) {
        String returnString = "";
        String findCustomer = "SELECT * FROM customer WHERE customerid =" + customerid;
        String findBook = "SELECT * FROM cust_book WHERE isbn = " + isbn;
        String removeBooking = "DELETE FROM cust_book WHERE isbn = " + isbn + " AND customerid = " + customerid;
        String updateBook = "UPDATE book SET numleft = numleft + 1 WHERE isbn = " + isbn + " AND numleft < numofcop";
        PreparedStatement findCustomerStatement = null, removeBookStatment = null, updateBookStatement = null;
        Statement findBookStatement = null;

        try {
            connection.setAutoCommit(false);
            findCustomerStatement = connection.prepareStatement(findCustomer);
//            Ensure customer exists
            if(findCustomerStatement.executeQuery().next()) {
//                Find book exists in bookings
                findBookStatement  = connection.createStatement();
                if(findBookStatement.executeQuery(findBook).next()) {
//                    Remove
                    removeBookStatment = connection.prepareStatement(removeBooking);
                    if(removeBookStatment.executeUpdate() == 0) {
                        returnString = "Could not return book";
                    }{
                        int input = createConfirmDialog("Transaction intermission");
                        if(input == 0) {
//                    Update book table
                            updateBookStatement = connection.prepareStatement(updateBook);
                            if(updateBookStatement.executeUpdate() == 0) {
                                returnString = "Could not update book inventory";
                            } else {

//                    Commit transaction
                                connection.commit();
                                findCustomerStatement.close();
                                findBookStatement.close();
                                removeBookStatment.close();
                                updateBookStatement.close();
                                return "Book is now returned";
                            }
                        }

                    }

                } else {
                    returnString = "Book not found";
                }
            } else {
                returnString = "Customer not found";
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                if(findCustomerStatement != null) findCustomerStatement.close();
                if(findBookStatement != null) findBookStatement.close();
                if(removeBookStatment != null) removeBookStatment.close();
                if(updateBookStatement != null) updateBookStatement.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            return e.getMessage();
        }

        try {
            connection.rollback();
            if(findCustomerStatement != null) findCustomerStatement.close();
            if(findBookStatement != null) findBookStatement.close();
            if(removeBookStatment != null) removeBookStatment.close();
            if(updateBookStatement != null) updateBookStatement.close();
            //connection.setReadOnly(true);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return returnString;
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

//    TODO CHECK
    public String deleteCus(int customerID) {
//        Delete from customer
//        Table 2 in assignment brief states that on deletion of a customer, the corresponding entry in cust_book is restricted therefore is not deleted
        String returnString = "";
        String findCustomer = "SELECT * FROM customer WHERE customerid =" + customerID;
        String deleteCustomer = "DELETE FROM customer WHERE customerid =" + customerID;
        PreparedStatement findStatement = null, deleteStatement = null;

        try {
            connection.setAutoCommit(false);
            findStatement = connection.prepareStatement(findCustomer);

//            Ensure customer exists
            if(findStatement.executeQuery().next()) {
                deleteStatement = connection.prepareStatement(deleteCustomer);

                if(deleteStatement.executeUpdate() == 0) {
                    returnString = "Could not delete customer";
                }else {
                    connection.commit();
                    findStatement.close();
                    deleteStatement.close();
                    return "Customer successfully deleted";
                }

            } else {
                returnString = "Customer does not exist";
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                if(findStatement != null) findStatement.close();
                if(deleteStatement != null) deleteStatement.close();
                //connection.setReadOnly(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return e.getMessage();
        }

//                    Commit transaction
        try {
            connection.rollback();
            if(findStatement != null) findStatement.close();
            if(deleteStatement != null) deleteStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnString;
    }

//    TODO CHECK
    public String deleteAuthor(int authorID) {
        //        Delete from Author
//        Table 2 in assignment brief states that on deletion of a author, the corresponding entry in cust_book is restricted therefore is not deleted
        String returnString = "";
        String findCustomer = "SELECT * FROM author WHERE authorid =" + authorID;
        String deleteCustomer = "DELETE FROM author WHERE authorid =" + authorID;
        PreparedStatement findStatement = null, deleteStatement = null;

        try {
            connection.setAutoCommit(false);
            findStatement = connection.prepareStatement(findCustomer);

//            Ensure Author exists
            if(findStatement.executeQuery().next()) {
                deleteStatement = connection.prepareStatement(deleteCustomer);

                if(deleteStatement.executeUpdate() == 0) {
                    returnString = "Could not delete author";
                }else {
                    connection.commit();
                    findStatement.close();
                    deleteStatement.close();
                    return "Author successfully deleted";
                }

            } else {
                returnString = "Author does not exist";
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                if(findStatement != null) findStatement.close();
                if(deleteStatement != null) deleteStatement.close();
                //connection.setReadOnly(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return e.getMessage();
        }

//                    Commit transaction
        try {
            connection.rollback();
            if(findStatement != null) findStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnString;
    }

//    TODO CHECK
    public String deleteBook(int isbn) {
//        Table 2 in assignment brief states that on deletion of a author, the corresponding entry in cust_book is restricted therefore is not deleted
        String returnString = "";
        String findBook = "SELECT * FROM book WHERE isbn =" + isbn;
        String deleteBook = "DELETE FROM book WHERE isbn =" + isbn;
        String deleteBook_Author = "DELETE FROM book_author WHERE isbn = " + isbn;

        PreparedStatement findStatement = null, deleteStatement = null, deleteBook_AuthorStatement = null;

        try {
            connection.setAutoCommit(false);
            findStatement = connection.prepareStatement(findBook);

//            Ensure book exists
            if(findStatement.executeQuery().next()) {
                deleteStatement = connection.prepareStatement(deleteBook);

                if(deleteStatement.executeUpdate() == 0) {
                    returnString = "Could not delete book";
                }else {
//                    Delete book from book_author
                    deleteBook_AuthorStatement = connection.prepareStatement(deleteBook_Author);

                    if(deleteBook_AuthorStatement.executeUpdate() == 0) {
                        returnString = "Could not delete book reference in book_author";
                    }else {
                        connection.commit();
                        findStatement.close();
                        deleteStatement.close();
                        deleteBook_AuthorStatement.close();
                        return "book successfully deleted";
                    }
                }

            } else {
                returnString = "book does not exist";
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
                if(findStatement != null) findStatement.close();
                if(deleteStatement != null) deleteStatement.close();
                if(deleteBook_AuthorStatement != null) deleteBook_AuthorStatement.close();
                //connection.setReadOnly(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return e.getMessage();
        }

//                    Commit transaction
        try {
            connection.rollback();
            if(findStatement != null) findStatement.close();
            if(deleteStatement != null) deleteStatement.close();
            if(deleteBook_AuthorStatement != null) deleteBook_AuthorStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnString;
    }

    /**
     * Displays a confirmation dialog box.
     * @param message to display to user
     * @return selection
     */
    public int createConfirmDialog(String message) {
        return showConfirmDialog(dialogParent, message, "Confirm", YES_OPTION);
    }
}