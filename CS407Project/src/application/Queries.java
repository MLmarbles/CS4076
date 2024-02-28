package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Queries {

    private Connection conn() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Oscar\\Desktop\\database\\mydatabase.db");
        return conn;
    }

    public void q1() throws SQLException {
        try (Connection conn = this.conn(); Statement statement = conn.createStatement()) {
            // Create the 'users' table
            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL)";
            statement.executeUpdate(createUserTableSQL);
            
            // Create modules
            String createModulesTableSQL = "CREATE TABLE IF NOT EXISTS modules (" +
            		"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            		"module_code TEXT NOT NULL, " +
            		"user_id INTEGER, " +
            		"FOREIGN KEY (user_id) REFERENCES users(id))";
            statement.executeUpdate(createModulesTableSQL);

            // Create the 'classes' table
            String createClassesTableSQL = "CREATE TABLE IF NOT EXISTS classes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "day TEXT NOT NULL, " +
                    "start TEXT NOT NULL, " +
                    "end TEXT NOT NULL, " +
                    "room TEXT NOT NULL," +
                    "module_id INTEGER," +
                    "FOREIGN KEY (module_id) REFERENCES modules(id))";
            statement.executeUpdate(createClassesTableSQL);
        }
    }

    public boolean userExists(String username) throws SQLException {
        try (Connection conn = this.conn();
             PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
            // Set the username parameter
            statement.setString(1, username);

            // Execute the query and retrieve the result
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if the count is greater than 0
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        // Return false by default if an exception occurs
        return false;
    }

    public boolean loginMatch(String username, String password) throws SQLException {
        try (Connection conn = this.conn();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    return storedPassword.equals(password);
                }
            }
        }
        return false;
    }

    public void insertUser(String username, String password) throws SQLException {
        try (Connection conn = this.conn();
             PreparedStatement statement = conn
                     .prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        }
    }
    
    public void addModules(String moduleCode, String username) throws SQLException {
        int userId = getUserIdByUsername(username);

        try (Connection conn = this.conn();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO modules (module_code, user_id) VALUES (?, ?)")) {
            statement.setString(1, moduleCode);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }
    
    public void removeModule(String moduleCode, String username) throws SQLException {
        int userId = getUserIdByUsername(username);
        
        try (Connection conn = this.conn();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM modules WHERE module_code = ? AND user_id = ?")) {
            statement.setString(1, moduleCode);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }
    
    private int getUserIdByUsername(String username) throws SQLException {
        int userId = -1; // Initialize with a default value (-1) indicating no user found

        try (Connection conn = this.conn();
                PreparedStatement statement = conn.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            statement.setString(1, username);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("id"); // Retrieve the user_id from the result set
                }
            }
        }
        
        return userId;
    }

    public HashMap<Integer, ArrayList<Module>> getModulesWithData() throws SQLException {
        HashMap<Integer, ArrayList<Module>> modulesMap = new HashMap<>();
        try (Connection conn = this.conn();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM modules")) {
            while (resultSet.next()) {
                int moduleId = resultSet.getInt("id");
                String moduleCode = resultSet.getString("module_code");
                Module module = new Module(moduleCode);

                // Fetch classes for this module
                ArrayList<Class> classes = getClassesForModule(conn, moduleId);
                module.setClasses(classes);

                if (!modulesMap.containsKey(moduleId)) {
                    modulesMap.put(moduleId, new ArrayList<>());
                }
                modulesMap.get(moduleId).add(module);
            }
        }
        return modulesMap;
    }

    private ArrayList<Class> getClassesForModule(Connection conn, int moduleId) throws SQLException {
        ArrayList<Class> classes = new ArrayList<>();
        String query = "SELECT * FROM classes WHERE module_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, moduleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String day = resultSet.getString("day");
                    String start = resultSet.getString("start");
                    String end = resultSet.getString("end");
                    String room = resultSet.getString("room");
                    int userId = resultSet.getInt("user_id");

                    // Create Class object and add to the list
                    classes.add(new Class(day, start, end, room, userId));
                }
            }
        }
        return classes;
    }
}
