/*
 * LibraryModel.java
 * Author:
 * Created on:
 */



import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LibraryModel {
    // For use in creating dialogs and making them modal
    private JFrame dialogParent;
    private Connection connection = null;

    public LibraryModel(JFrame parent, String userid, String password) {
        dialogParent = parent;
        try{
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql:"+ "//localhost" + userid + "_jdbc";
            connection = DriverManager.getConnection(url, userid, password);

        } catch (ClassNotFoundException cnfe){
            System.out.println("Can not find"+
                    "the driver class: "+
                    "\nEither I have not installed it"+
                    "properly or \n postgresql.jar "+
                    " file is not in my CLASSPATH");
        }catch (SQLException sqlex){
            System.out.println("Can not connect");
            System.out.println(sqlex.getMessage());
        }

    }


    public String bookLookup(int isbn) {
        return "Lookup Book Stub";
    }

    public String showCatalogue() {
        return "Show Catalogue Stub";
    }

    public String showLoanedBooks() {
        return "Show Loaned Books Stub";
    }

    public String showAuthor(int authorID) {
        return "Show Author Stub";
    }

    public String showAllAuthors() {
        return "Show All Authors Stub";
    }

    public String showCustomer(int customerID) {
        return "Show Customer Stub";
    }

    public String showAllCustomers() {
        return "Show All Customers Stub";
    }

    public String borrowBook(int isbn, int customerID,
                             int day, int month, int year) {
        return "Borrow Book Stub";
    }

    public String returnBook(int isbn, int customerid) {
        return "Return Book Stub";
    }

    public void closeDBConnection() {
    }

    public String deleteCus(int customerID) {
        return "Delete Customer";
    }

    public String deleteAuthor(int authorID) {
        return "Delete Author";
    }

    public String deleteBook(int isbn) {
        return "Delete Book";
    }
}