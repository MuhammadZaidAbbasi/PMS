package Database;

import java.sql.*;


public class DataBaseConnection {

        private static final String URL = "jdbc:mysql://localhost:3306/healthmanagement";
        private static final String USER = "root";
        private static final String PASSWORD = "M@n025oi26)1Az";

        public static Connection getConnection() throws SQLException {
                return DriverManager.getConnection(URL, USER, PASSWORD);
        }

}
