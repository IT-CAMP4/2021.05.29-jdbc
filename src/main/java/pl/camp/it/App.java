package pl.camp.it;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    static Connection connection = null;
    static PreparedStatement insertUserStatement = null;

    public static void main(String[] args) {
        connect();
        prepareStatements();

        //User user = new User(1, "Marek", "Malinowski", "mareczek123", "tajne123", User.Role.USER);

        //addUser(user);

        //updateUser(user);

        //User user1 = getUserById(2);

        //System.out.println(user1);

        List<User> list = getUsers();

        System.out.println(list);


        disconnect();
    }

    private static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&characterEncoding=utf8", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void disconnect() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void addUser(User user) {
        try {
            StringBuilder SQL = new StringBuilder();
            SQL.append("INSERT INTO tuser (name, surname, login, password, role) VALUES ('")
                    .append(user.getName()).append("',")
                    .append("'").append(user.getSurname()).append("',")
                    .append("'").append(user.getLogin()).append("',")
                    .append("'").append(user.getPassword()).append("',")
                    .append("'").append(user.getRole()).append("');");

            System.out.println(SQL.toString());

            Statement statement = connection.createStatement();
            statement.execute(SQL.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void updateUser(User user) {
        try {
            StringBuilder SQL = new StringBuilder();
            SQL.append("UPDATE tuser SET ")
                    .append("name='").append(user.getName()).append("',")
                    .append("surname='").append(user.getSurname()).append("',")
                    .append("login='").append(user.getLogin()).append("',")
                    .append("password='").append(user.getPassword()).append("',")
                    .append("role='").append(user.getRole()).append("'")
                    .append(" WHERE id=").append(user.getId()).append(";");

            System.out.println(SQL.toString());

            Statement statement = connection.createStatement();
            statement.execute(SQL.toString());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static User getUserById(int id) {
        try {
            String SQL = "SELECT * FROM tuser WHERE id=" + id;

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(SQL);

            if(!rs.next()) {
                return null;
            }

            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setRole(User.Role.valueOf(rs.getString("role")));

            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    private static User getUserById2(int id) {
        try {
            String SQL = "SELECT * FROM tuser WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if(!rs.next()) {
                return null;
            }

            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setRole(User.Role.valueOf(rs.getString("role")));

            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    private static List<User> getUsers() {
        List<User> users = new ArrayList<>();

        try {
            String SQL = "SELECT * FROM tuser;";

            PreparedStatement preparedStatement = connection.prepareStatement(SQL);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(User.Role.valueOf(rs.getString("role")));

                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    public static void addUser2(User user) {
        try {
            insertUserStatement.clearParameters();

            insertUserStatement.setString(1, user.getName());
            insertUserStatement.setString(2, user.getSurname());
            insertUserStatement.setString(3, user.getLogin());
            insertUserStatement.setString(4, user.getPassword());
            insertUserStatement.setString(5, user.getRole().toString());

            insertUserStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void prepareStatements() {
        try {
            String SQL = "INSERT INTO tuser (name, surname, login, password, role) VALUES (?, ?, ?, ?, ?);";
            insertUserStatement = connection.prepareStatement(SQL);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
