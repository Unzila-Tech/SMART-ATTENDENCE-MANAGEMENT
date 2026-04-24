package project;
import javax.swing.*;
import java.awt.*;
public class SmartAttendanceApp extends JFrame{
 
    // Shared Data
    public static String currentActiveCode = "NOT_GEN";
    public static String activeSubject = "";
    public static String loggedInUser = "";
    public static String loggedInFacultyName = "";
    public static String studentRoll = "";
    public static String studentCourse = "";
    public static String studentDept = "";
    
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    public SmartAttendanceApp() {
        DbConnect.initDB();

        setTitle("Smart Attendance Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(new LandingPage(this), "Home");
        mainPanel.add(new LoginPage(this), "Login");
        mainPanel.add(new RegisterPage(this), "Register");
        mainPanel.add(new FacultyDashboard(this), "Faculty");
        mainPanel.add(new StudentDashboard(this), "Student");

        add(mainPanel);
        cardLayout.show(mainPanel, "Home");
    }
    public void switchPage(String pageName) {

        // When switching to Student → load data from DB
        if (pageName.equals("Student")) {

            String[] data = DbConnect.getStudentDetails(loggedInUser);

            if (data != null) {
                studentRoll = data[1];
                studentCourse = data[2];
                studentDept = data[3];
            }
        }

        cardLayout.show(mainPanel, pageName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SmartAttendanceApp().setVisible(true));
    }
}