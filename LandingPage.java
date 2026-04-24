package project;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class LandingPage extends JPanel {
    private SmartAttendanceApp mainApp;
    private final Color THEME_GREEN = new Color(0, 51, 32);
    private final Color BG_LIGHT_GRAY = new Color(245, 245, 245);
    private final Color DARK_GREEN = new Color(10, 60, 40);
    private final Color GOLD = new Color(180, 150, 70);

    public LandingPage(SmartAttendanceApp app) {
        this.mainApp = app;
        
        setLayout(new GridBagLayout());
        setBackground(BG_LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();

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
        
        JLabel subTitle = new JLabel("WELCOME TO ATTENDANCE PORTAL");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subTitle.setForeground(new Color(200, 200, 200));
        
        titleGroup.add(mainTitle);
        titleGroup.add(subTitle);
        headerPanel.add(titleGroup, BorderLayout.WEST);

        // Header Top fix 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        add(headerPanel, gbc);

        // --- 2. CENTER CONTENT ---
        
        JPanel centerContent = new JPanel(new GridBagLayout());
        centerContent.setOpaque(false);
        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(20, 20, 20, 20);

        // --- Title ---
        JLabel title = new JLabel("ATTENDANCE PORTAL");
        title.setForeground(THEME_GREEN);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        centerContent.add(title, innerGbc);

        // --- Login Button ---
        JButton logBtn = new JButton("LOGIN");
        styleBtn(logBtn);
        innerGbc.gridy = 1;
        centerContent.add(logBtn, innerGbc);

        // --- Register Button ---
        JButton regBtn = new JButton("NEW USER? REGISTER HERE");
        styleBtn(regBtn);
        innerGbc.gridy = 2;
        centerContent.add(regBtn, innerGbc);

        
        gbc.gridy = 1;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(centerContent, gbc);

        // --- Actions ---
        logBtn.addActionListener(e -> mainApp.switchPage("Login"));
        regBtn.addActionListener(e -> mainApp.switchPage("Register"));
    }
    private void styleBtn(JButton btn) {
        btn.setBackground(THEME_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}