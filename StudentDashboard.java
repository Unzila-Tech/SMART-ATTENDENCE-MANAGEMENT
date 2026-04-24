package project;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class StudentDashboard extends JPanel {

    private SmartAttendanceApp mainApp;
    private JTextField codeField;
    private JLabel activeCodeDisplay;
    private final Color DARK_GREEN = new Color(10, 60, 40);
    private final Color GOLD = new Color(180, 150, 70);
    
    private Vector<Vector<String>> attendanceData = new Vector<>();

    public StudentDashboard(SmartAttendanceApp app) {
        this.mainApp = app;
        setLayout(new BorderLayout());
        setBackground(new Color(230, 235, 230));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(800, 100));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, GOLD));

        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        titleGroup.setBorder(new EmptyBorder(10, 20, 10, 0));
        
        JLabel mainTitle = new JLabel("Smart Attendance Management System");
        mainTitle.setFont(new Font("Serif", Font.BOLD, 22));
        mainTitle.setForeground(Color.WHITE);
        
        JLabel subTitle = new JLabel("STUDENT DASHBOARD");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subTitle.setForeground(new Color(200, 200, 200));
        
        titleGroup.add(mainTitle);
        titleGroup.add(subTitle);
        headerPanel.add(titleGroup, BorderLayout.WEST);

        JPanel profileGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 35));
        profileGroup.setOpaque(false);
        
        JButton profileBtn = new JButton("👤 My Profile");
        profileBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        profileBtn.setForeground(Color.WHITE);
        profileBtn.setContentAreaFilled(false);
        profileBtn.setBorder(null);
        profileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileBtn.addActionListener(e -> showStudentProfile());
        
        profileGroup.add(profileBtn);
        headerPanel.add(profileGroup, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- CENTER ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(450, 500)); 
        card.setMaximumSize(new Dimension(450, 500)); 
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GOLD, 1), 
            new EmptyBorder(25, 40, 25, 40)
        ));
       card.add(Box.createVerticalGlue()); // Ye top space fill karega
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel infoLbl = new JLabel("Live Class Code from Faculty:");
        infoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        
        activeCodeDisplay = new JLabel(SmartAttendanceApp.currentActiveCode.isEmpty() ? "---" : SmartAttendanceApp.currentActiveCode);
        activeCodeDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        activeCodeDisplay.setFont(new Font("Monospaced", Font.BOLD, 24));
        activeCodeDisplay.setForeground(new Color(150, 50, 50));
        activeCodeDisplay.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(5, 20, 5, 20)
        ));

        JLabel cardTitle = new JLabel("Mark Your Attendance");
        cardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        cardTitle.setForeground(DARK_GREEN);

        JPanel codeRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        codeRow.setOpaque(false);
        JLabel codeLbl = new JLabel("Enter Code:");
        codeField = new JTextField(10);
        codeField.setFont(new Font("Monospaced", Font.BOLD, 16));
        codeRow.add(codeLbl);
        codeRow.add(codeField);

        JButton subAtt = createStyledButton("SUBMIT ATTENDANCE");
        JButton viewAtt = createStyledButton("VIEW ATTENDENCE");
        JButton refreshBtn = createStyledButton("Refresh Code");

        subAtt.addActionListener(e -> {
            String inputCode = codeField.getText().trim();

            if (DbConnect.validateCode(inputCode)) {

                String subject = DbConnect.getSubject(inputCode);
                String studentName = SmartAttendanceApp.loggedInUser;
                String course = SmartAttendanceApp.studentCourse;

                DbConnect.markAttendance(studentName, subject, course);

                // Ui
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                Vector<String> newRecord = new Vector<>();
                newRecord.add(timeStamp);
                newRecord.add(subject);
                newRecord.add("Present");
                attendanceData.add(newRecord);

                JOptionPane.showMessageDialog(this, "Attendance Marked Successfully!");
                codeField.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Code!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> {
            activeCodeDisplay.setText(SmartAttendanceApp.currentActiveCode.isEmpty() ? "---" : SmartAttendanceApp.currentActiveCode);
        });

        viewAtt.addActionListener(e -> showMyAttendanceRecords());

        card.add(infoLbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(activeCodeDisplay);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(refreshBtn);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 0)));
        card.add(cardTitle);
        card.add(codeRow);
        card.add(subAtt);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(viewAtt);
        card.add(Box.createVerticalGlue()); 
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(card);
        add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(0, 0, 30, 0));
        JButton logout = createStyledButton("Logout");
        logout.addActionListener(e -> mainApp.switchPage("Login"));
        footer.add(logout);
        add(footer, BorderLayout.SOUTH);
    }

    private void showMyAttendanceRecords() {

        String studentName = SmartAttendanceApp.loggedInUser;

        java.util.List<String> records = DbConnect.getAttendanceByStudent(studentName);

        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No attendance found!");
            return;
        }

        String[] columns = {"Name", "Subject", "Course", "Date & Time", "Status"};
        Object[][] data = new Object[records.size()][5];

        for (int i = 0; i < records.size(); i++) {

            String[] parts = records.get(i).split(" \\| ");

            data[i][0] = studentName;
            data[i][1] = parts[0];
            data[i][2] = parts[1];
            data[i][3] = parts[2];
            data[i][4] = "Present";
        }

        JTable table = new JTable(data, columns);
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(600, 300));

        JOptionPane.showMessageDialog(this, pane, "My Attendance", JOptionPane.INFORMATION_MESSAGE);
    }
    private void showStudentProfile() {

        String[] data = DbConnect.getStudentDetails(SmartAttendanceApp.loggedInUser);

        String studentName = data[0];
        String rollNo = data[1];
        String course = data[2];
        String dept = data[3];

        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[][] details = {
            {"Name:", studentName},
            {"Roll No:", rollNo},
            {"Course:", course},
            {"Department:", dept},
            {"Role:", "Student User"}
        };

        for (String[] detail : details) {
            JLabel lbl = new JLabel("<html><b>" + detail[0] + "</b> " + detail[1] + "</html>");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lbl.setBorder(new EmptyBorder(5, 0, 5, 0));
            profilePanel.add(lbl);
        }

        JOptionPane.showMessageDialog(this, profilePanel, "My Profile Details", JOptionPane.PLAIN_MESSAGE);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(DARK_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}