package project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterPage extends JPanel {

    private SmartAttendanceApp mainApp;
    private final Color THEME_GREEN = new Color(0, 51, 32);
    private final Color BG_LIGHT_GRAY = new Color(245, 245, 245);
    private final Color DARK_GREEN = new Color(10, 60, 40);
    private final Color GOLD = new Color(180, 150, 70);

    public RegisterPage(SmartAttendanceApp app) {
        this.mainApp = app;
        
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT_GRAY);

        // --- 1. HEADER SECTION ---
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

        JLabel subTitle = new JLabel("CREATE NEW ACCOUNT");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subTitle.setForeground(new Color(200, 200, 200));

        titleGroup.add(mainTitle);
        titleGroup.add(subTitle);
        headerPanel.add(titleGroup, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. CENTER CONTENT 
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Fields ---
        JTextField nameF = new JTextField(15);
        JTextField rollF = new JTextField(15);
        JTextField courseF = new JTextField(15);
        JTextField deptF = new JTextField(15);
        JTextField userF = new JTextField(15);
        JPasswordField passF = new JPasswordField(15);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Faculty"});

        JLabel lRoll = new JLabel("Roll No:");
        JLabel lCourse = new JLabel("Course:");
        JLabel lDept = new JLabel("Department:");

        // Form Layout
        gbc.gridwidth = 1;
        gbc.gridy = 0; gbc.gridx = 0; centerPanel.add(new JLabel("Register As:"), gbc);
        gbc.gridx = 1; centerPanel.add(roleBox, gbc);

        gbc.gridy = 1; gbc.gridx = 0; centerPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; centerPanel.add(nameF, gbc);

        gbc.gridy = 2; gbc.gridx = 0; centerPanel.add(lRoll, gbc);
        gbc.gridx = 1; centerPanel.add(rollF, gbc);

        gbc.gridy = 3; gbc.gridx = 0; centerPanel.add(lCourse, gbc);
        gbc.gridx = 1; centerPanel.add(courseF, gbc);

        gbc.gridy = 4; gbc.gridx = 0; centerPanel.add(lDept, gbc);
        gbc.gridx = 1; centerPanel.add(deptF, gbc);

        gbc.gridy = 5; gbc.gridx = 0; centerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; centerPanel.add(userF, gbc);

        gbc.gridy = 6; gbc.gridx = 0; centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; centerPanel.add(passF, gbc);

        // --- Register Button ---
        JButton regBtn = new JButton("REGISTER");
        styleBtn(regBtn);
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(regBtn, gbc);
       // BACK BUTTON
        JButton backBtn = new JButton("BACK TO HOME");
        backBtn.setBackground(new Color(0, 51, 32));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 8; 
        gbc.insets = new Insets(15, 10, 10, 10); // Thoda extra top margin
        centerPanel.add(backBtn, gbc);

        // --- Dynamic UI ---
        roleBox.addActionListener(e -> {
            boolean isStudent = roleBox.getSelectedItem().equals("Student");
            rollF.setVisible(isStudent); lRoll.setVisible(isStudent);
            courseF.setVisible(isStudent); lCourse.setVisible(isStudent);
            deptF.setVisible(isStudent); lDept.setVisible(isStudent);
            revalidate(); repaint();
        });

        // --- Register Action ---
        regBtn.addActionListener(e -> {
            String role = roleBox.getSelectedItem().toString();
            String name = nameF.getText();
            String roll = rollF.getText();
            String course = courseF.getText();
            String dept = deptF.getText();
            String username = userF.getText();
            String password = new String(passF.getPassword());

            if(username.isEmpty() || password.isEmpty() || name.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            boolean ok;
            if(role.equals("Faculty")){
                ok = DbConnect.registerUser(username, password, role, name, "", "", "");
            } else {
                ok = DbConnect.registerUser(username, password, role, name, roll, course, dept);
            }

            if(ok){
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                mainApp.switchPage("Login");
            } else {
                JOptionPane.showMessageDialog(this, "Username exists or DB error!");
            }
        });

        backBtn.addActionListener(e -> mainApp.switchPage("Home"));
      
        add(centerPanel, BorderLayout.CENTER);
    }

    private void styleBtn(JButton btn) {
        btn.setBackground(THEME_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}