package project;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DbConnect {
    private static final String URL = "jdbc:sqlite:D:\\project\\attendance_db.db";
    // CONNECT
    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        Statement stmt = conn.createStatement();
        stmt.execute("PRAGMA busy_timeout = 3000");
        return conn;
    }
    // INIT DATABASE
    public static void initDB() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // FACULTY TABLE
            stmt.execute("CREATE TABLE IF NOT EXISTS faculty (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT," +
                    "full_name TEXT)");

            // STUDENT TABLE
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT," +
                    "full_name TEXT," +
                    "roll_no TEXT," +
                    "course TEXT," +
                    "department TEXT)");

         // ATTENDANCE TABLE (FIXED)
            stmt.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_name TEXT," +
                    "subject TEXT," +
                    "course TEXT," +
                    "date TEXT)");

            // CODE TABLE
            stmt.execute("CREATE TABLE IF NOT EXISTS attendance_code (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subject TEXT," +
                    "code TEXT," +
                    "created_at TEXT)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REGISTER
    public static boolean registerUser(String u, String p, String role,
                                       String name, String roll, String course, String dept) {

        try (Connection conn = connect()) {

            PreparedStatement ps;

            if (role.equals("Faculty")) {

                ps = conn.prepareStatement(
                        "INSERT INTO faculty(username,password,full_name) VALUES(?,?,?)");

                ps.setString(1, u);
                ps.setString(2, p);
                ps.setString(3, name);

            } else {

                ps = conn.prepareStatement(
                        "INSERT INTO students(username,password,full_name,roll_no,course,department) VALUES(?,?,?,?,?,?)");

                ps.setString(1, u);
                ps.setString(2, p);
                ps.setString(3, name);
                ps.setString(4, roll);
                ps.setString(5, course);
                ps.setString(6, dept);
            }
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {

            if (e.getMessage().contains("UNIQUE")) {
                System.out.println("Username already exists");
            } else if (e.getMessage().contains("locked")) {
                System.out.println("Database busy");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
    // LOGIN
    public static boolean loginUser(String name, String pass, String role) {
        try (Connection conn = connect()) {
            PreparedStatement ps;
            if (role.equals("Faculty")) {
                ps = conn.prepareStatement(
                       "SELECT * FROM faculty WHERE full_name=? AND password=?");

            } else {

                ps = conn.prepareStatement(
                        "SELECT * FROM students WHERE full_name=? AND password=?");
            }
            ps.setString(1, name);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // GENERATE CODE
    public static String generateCode(String subject) {

        subject = subject.trim().toUpperCase();

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        while (code.length() < 8)
            code.append(chars.charAt(r.nextInt(chars.length())));

        String finalCode = code.toString();

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date());

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO attendance_code VALUES(null,?,?,?)")) {

            ps.setString(1, subject);
            ps.setString(2, finalCode);
            ps.setString(3, date);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalCode;
    }

    // VALIDATE CODE
    public static boolean validateCode(String code) {

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM attendance_code WHERE code=?")) {

            ps.setString(1, code);
            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }

    // GET SUBJECT
    public static String getSubject(String code) {

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT subject FROM attendance_code WHERE code=?")) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getString("subject");

        } catch (Exception e) {}

        return "";
    }

    public static void markAttendance(String student, String subject, String course) {

        subject = subject.trim().toUpperCase(); 

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date());

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO attendance(student_name,subject,course,date) VALUES(?,?,?,?)")) {

            ps.setString(1, student);
            ps.setString(2, subject);
            ps.setString(3, course);
            ps.setString(4, date);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> getAttendanceByStudent(String student) {

        List<String> list = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT subject, course, date FROM attendance WHERE student_name=?")) {

            ps.setString(1, student);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(
                        rs.getString("subject") + " | " +
                        rs.getString("course") + " | " +
                        rs.getString("date")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

 // GET STUDENT DETAILS
    public static String[] getStudentDetails(String name) {

        String[] data = {"", "", "", ""};

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM students WHERE full_name=?")) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                data[0] = rs.getString("full_name");
                data[1] = rs.getString("roll_no");
                data[2] = rs.getString("course");
                data[3] = rs.getString("department");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    // TOTAL STUDENTS (for absent calculation)
    public static int getTotalStudentsBySubject(String subject) {

        int count = 0;

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM students")) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
   
    public static List<String[]> getAttendanceBySubject(String subject) {

        List<String[]> list = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT student_name, subject, date FROM attendance")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                if (rs.getString("subject").equalsIgnoreCase(subject)) {

                    String[] row = new String[3];
                    row[0] = rs.getString("student_name");
                    row[1] = rs.getString("subject");
                    row[2] = rs.getString("date");

                    list.add(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

 // FILTER BY COURSE
    public static List<String> getAttendanceByCourse(String course) {

        List<String> list = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM attendance WHERE course=?")) {

            ps.setString(1, course);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(
                        rs.getString("student_name") + " | " +
                        rs.getString("subject") + " | " +
                        rs.getString("date")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
