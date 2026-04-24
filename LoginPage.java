package project;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginPage extends JPanel {

    private SmartAttendanceApp mainApp;
    private JTextField nameField;
    private JPasswordField pField;
    private JComboBox<String> loginRole;
    private JLabel nameLbl, courseLbl;
    private JTextField courseField;
    
    // Theme Colors
    private final Color DARK_GREEN = new Color(10, 60, 40);
    private final Color GOLD = new Color(180, 150, 70);

    public LoginPage(SmartAttendanceApp app) {
        this.mainApp = app;
        setLayout(new BorderLayout());
        setBackground(new Color(230, 235, 230));

        // --- 1. HEADER (FIXED & CORRECTED) ---
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
        
        JLabel subTitle = new JLabel("SECURE LOGIN ACCESS");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subTitle.setForeground(new Color(200, 200, 200));
        
        titleGroup.add(mainTitle);
        titleGroup.add(subTitle);
        headerPanel.add(titleGroup, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. CENTER LOGIN CARD 
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GOLD, 1),
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Role Selection
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(new JLabel("Login As:"), gbc);
        loginRole = new JComboBox<>(new String[]{"Student", "Faculty"});
        gbc.gridx = 1;
        card.add(loginRole, gbc);

        // Name Field
        gbc.gridx = 0; gbc.gridy = 1;
        nameLbl = new JLabel("Student Name:");
        card.add(nameLbl, gbc);
        nameField = new JTextField(15);
        gbc.gridx = 1;
        card.add(nameField, gbc);

        // Course Field
        gbc.gridx = 0; gbc.gridy = 2;
        courseLbl = new JLabel("Course:");
        card.add(courseLbl, gbc);
        courseField = new JTextField(15);
        gbc.gridx = 1;
        card.add(courseField, gbc);

        // Password Field
        gbc.gridx = 0; gbc.gridy = 3;
        card.add(new JLabel("Password:"), gbc);
        pField = new JPasswordField(15);
        gbc.gridx = 1;
        card.add(pField, gbc);

        // Login Button
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(DARK_GREEN);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        card.add(loginBtn, gbc);

        // BACK BUTTON
        JButton backBtn = new JButton("BACK TO HOME");
        backBtn.setBackground(new Color(0, 51, 32));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        card.add(backBtn, gbc);

        //  ROLE SWITCH 
        loginRole.addActionListener(e -> {
            boolean isStudent = loginRole.getSelectedItem().equals("Student");
            nameLbl.setText(isStudent ? "Student Name:" : "Faculty Name:");
            courseLbl.setVisible(isStudent);
            courseField.setVisible(isStudent);
            card.revalidate();
            card.repaint();
        });

        // --- LOGIN ACTION LOGIC ---
        loginBtn.addActionListener(e -> {
            String role = (String) loginRole.getSelectedItem();
            String nameInput = nameField.getText();
            String passInput = new String(pField.getPassword());

            if (nameInput.isEmpty() || passInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean ok = DbConnect.loginUser(nameInput, passInput, role);
            if(ok){
                SmartAttendanceApp.loggedInUser = nameInput;
                if (role.equals("Faculty")) {
                    SmartAttendanceApp.loggedInFacultyName = nameInput;
                    mainApp.switchPage("Faculty");
                } else {
                    mainApp.switchPage("Student");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        });

        backBtn.addActionListener(e -> mainApp.switchPage("Home"));

        centerPanel.add(card);
        add(centerPanel, BorderLayout.CENTER);
        
        JLabel footer = new JLabel("© 2026 Smart Attendance System", JLabel.CENTER);
        footer.setFont(new Font("SansSerif", Font.ITALIC, 10));
        footer.setBorder(new EmptyBorder(10,0,10,0));
        add(footer, BorderLayout.SOUTH);
    }
}