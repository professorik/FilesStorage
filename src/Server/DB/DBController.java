package Server.DB;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Random;

/**
 * @author professorik
 * @created 08/04/2021 - 16:57
 * @project Server
 */
public class DBController {

    private Connection conn;

    public DBController() {
        conn = this.connect();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:D:/IdeaProjects/ServerProject/src/Server/DB/usersDB.sqlite";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean userExist(String name) {
        String sql = String.format("SELECT id FROM USERS WHERE nick = '%s'", name);
        try (Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            return res.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean userExist(String name, String password) {
        String sql = String.format("SELECT password, salt FROM USERS WHERE nick = '%s'", name);
        try (Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            return res.getString("password").equals(getPassword(password, res.getString("salt")));
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean registerUser(String nick, String password, String path) {
        String sql = "INSERT INTO USERS(nick,password,salt,path) VALUES(?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nick);
            String salt = getSalt();
            pstmt.setString(2, getPassword(password, salt));
            pstmt.setString(3, salt);
            pstmt.setString(4, path);
            pstmt.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private String getSalt() {
        final Random RANDOM = new SecureRandom();
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    private String getPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((password + salt).getBytes());
        byte[] digest = md.digest();
        return new String(digest, StandardCharsets.UTF_8);
    }

    public String getPath(String name){
        String sql = String.format("SELECT path FROM USERS WHERE nick = '%s'", name);
        try {Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql);
             return res.getString("path");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void updatePath(String nick, String path) {
        String sql = "UPDATE USERS SET path = ? WHERE nick = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            pstmt.setString(2, nick);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePassword(String nick, String password) {
        String sql = "UPDATE USERS SET password = ?, salt = ? WHERE nick = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String salt = getSalt();
            pstmt.setString(1, getPassword(password, salt));
            pstmt.setString(2, salt);
            pstmt.setString(3, nick);
            pstmt.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(String nick) {
        String sql = "DELETE FROM USERS WHERE nick = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nick);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void finish() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}