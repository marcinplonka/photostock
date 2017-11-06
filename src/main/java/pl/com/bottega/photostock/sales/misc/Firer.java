package pl.com.bottega.photostock.sales.misc;


import java.sql.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Firer {
    static String firstName;
    static String lastName;
    static Scanner scanner = new Scanner(System.in);
    private static final String FIRE_BY_ID = "UPDATE employees e JOIN salaries s ON e.emp_no = s.emp_no " +
            "JOIN titles t ON e.emp_no = t.emp_no LEFT JOIN dept_manager dm ON e.emp_no = dm.emp_no " +
            "SET s.to_date = now(), t.to_date = now(), dm.to_date = now() WHERE e.emp_no = ? " +
            "AND s.to_date > now() AND t.to_date > now() AND  dm.to_date > now()";
    private static final String SEARCH_BY_FLNAME = "SELECT e.emp_no AS 'id', e.first_name, e.last_name, e.birth_date " +
            "FROM employees e WHERE e.first_name LIKE ? AND e.last_name LIKE ?";



    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            getFLName(scanner);
            fireTheEmployee(connection, orElseGetId(getSearchResults(connection, firstName, lastName)));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static void getFLName(Scanner scanner) throws SQLException {
        System.out.println("Who do you want to fire?");
        System.out.println("first name: ");
        firstName = scanner.nextLine();
        System.out.println("last name: ");
        lastName = scanner.nextLine();

    }



    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/employees?" +
                "user=root&password=mysql0000&useSSL=false");
    }

    private static ResultSet getSearchResults(Connection connection, String firstName, String lastName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SEARCH_BY_FLNAME);
        ps.setString(1, firstName.trim());
        ps.setString(2, lastName.trim());
        return ps.executeQuery();

    }

    private static int orElseGetId(ResultSet resultSet) throws SQLException {
        resultSet.last();
        int resultsNumber = resultSet.getRow();
        resultSet.beforeFirst();
        if (resultsNumber == 0)
            return resultsNumber;
        if (resultsNumber == 1)
            return resultSet.getInt(1);
        if (resultsNumber > 1)
            System.out.println("There is more than one result.");
        printResults(resultSet);
        System.out.println("Select id: ");
        Set<Integer> ids = new HashSet<>();
        while (resultSet.next()) {
            ids.add(resultSet.getInt(1));
        }
        resultSet.beforeFirst();
        int id = scanner.nextInt();
        if (ids.contains(id))
            return id;
        System.out.println("Wrong id number, try again.");
        orElseGetId(resultSet);
        return 0;
    }

    private static void printResults(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            System.out.printf("id: %d, %s %s birth date: %s\n",
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4).toString()
            );
        }
        resultSet.beforeFirst();
    }

    private static void fireTheEmployee(Connection connection, int id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(FIRE_BY_ID);
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
