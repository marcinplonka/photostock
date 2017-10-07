
package pl.com.bottega.photostock.sales.misc;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class EmployeesJDBCWrite {

    private static final Date TO_DATE_MAX = Date.valueOf("9999-01-01");
    private static final String INSERT_EMPLOYEE_SQL = "INSERT INTO employees VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GENERATE_EMP_NO = "SELECT max(emp_no) + 1 FROM employees";
    private static final String INSERT_SALARY = "INSERT INTO salaries VALUES (?, ?, ?, ?)";
    private static final java.lang.String INSERT_DEPT_EMP = "INSERT INTO dept_emp VALUES (?, ?, ?, ?)";
    private static final java.lang.String GET_DEPT_NO = "SELECT dept_no FROM departments WHERE dept_name = ?";
    private static final java.lang.String INSERT_DEPARTMENT = "INSERT INTO departments VALUES (?, ?)";
    private static final String GET_MAX_DEPT = "SELECT max(dept_no) FROM departments";

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String firstName = getFirstName(scanner);
        String lastName = getLastName(scanner);
        LocalDate hireDate = LocalDate.now();
        String gender = getGender(scanner);
        LocalDate birthDate = getBirthDate(scanner);
        Integer salary = getSalary(scanner);
        String departmentName = getDepartmentName(scanner);
        //String title = getTitle(scanner);

        Connection connection = getConnection();
        connection.setAutoCommit(false);

        Long empNo = insertEmployee(connection, firstName, lastName, hireDate, gender, birthDate);
        insertSalary(connection, empNo, salary);
        insertDepartment(connection, empNo, departmentName);
        //insertTitle(connection, empNo, title);

        connection.commit();
    }

    private static void insertTitle(Connection connection, Long empNo, String title) {

    }

    private static void insertDepartment(Connection connection, Long empNo, String departmentName) throws SQLException {
        String deptNo = getOrCreateDepartment(connection, departmentName);
        PreparedStatement ps = connection.prepareStatement(INSERT_DEPT_EMP);
        ps.setLong(1, empNo);
        ps.setString(2, deptNo);
        ps.setDate(3, Date.valueOf(LocalDate.now()));
        ps.setDate(4, TO_DATE_MAX);
        ps.executeUpdate();
    }

    private static String getOrCreateDepartment(Connection connection, String departmentName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(GET_DEPT_NO);
        ps.setString(1, departmentName);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next())
            return resultSet.getString(1);
        else
            return createDepartment(connection, departmentName);
    }

    private static String createDepartment(Connection connection, String departmentName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_DEPARTMENT);
        String deptNo = getNextDepartmentNumber(connection);
        ps.setString(1, deptNo);
        ps.setString(2, departmentName);
        ps.executeUpdate();
        return deptNo;
    }

    private static String getNextDepartmentNumber(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(GET_MAX_DEPT);
        rs.next();
        String maxDeptNo = rs.getString(1).replace("d", "");
        int nextNo = Integer.valueOf(maxDeptNo) + 1;
        return String.format("d%03d", nextNo);
    }

    private static void insertSalary(Connection connection, Long empNo, Integer salary) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SALARY);
        ps.setLong(1, empNo);
        ps.setLong(2, salary);
        ps.setDate(3, Date.valueOf(LocalDate.now()));
        ps.setDate(4, TO_DATE_MAX);
        ps.executeUpdate();
    }

    private static Long insertEmployee(Connection connection, String firstName, String lastName, LocalDate hireDate, String gender, LocalDate birthDate) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery(GENERATE_EMP_NO);
        rs.next();
        Long id = rs.getLong(1);
        PreparedStatement stmt = connection.prepareStatement(INSERT_EMPLOYEE_SQL);
        stmt.setLong(1, id);
        stmt.setDate(2, Date.valueOf(birthDate));
        stmt.setString(3, firstName);
        stmt.setString(4, lastName);
        stmt.setString(5, gender);
        stmt.setDate(6, Date.valueOf(hireDate));
        stmt.executeUpdate();
        return id;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/employees?" +
                "user=root&password=mysql0000&useSSL=false");
    }

    private static String getLastName(Scanner sc) {
        System.out.println("Podaj nazwisko: ");
        return sc.nextLine();
    }

    private static String getFirstName(Scanner scanner) {
        System.out.println("Podaj imię:");
        return scanner.nextLine();
    }

    private static Integer getSalary(Scanner scanner) {
        System.out.println("Wpisz zarobki: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private static String getTitle(Scanner scanner) {
        System.out.println("Podaj stanowisko: ");
        return scanner.nextLine();
    }

    private static String getDepartmentName(Scanner scanner) {
        System.out.println("Podaj nazwę działu: ");
        return scanner.nextLine();
    }

    private static LocalDate getBirthDate(Scanner scanner) {
        System.out.println("Podaj datę urodzenia: ");
        return LocalDate.parse(scanner.nextLine());
    }

    private static String getGender(Scanner scanner) {
        System.out.println("Podaj płeć");
        return scanner.nextLine();
    }

}