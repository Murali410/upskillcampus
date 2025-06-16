import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class User {
    String name;
    String userId;
    String password;
    double balance;

    User(String name, String userId, String password) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.balance = 0.0;
    }
}

public class BankingGUI extends JFrame {
    static final String USERS_FILE = "users.txt";
    static Map<String, User> users = new HashMap<>();
    static User currentUser = null;

    JPanel mainPanel;
    CardLayout cardLayout;

    public BankingGUI() {
        loadUsers();

        setTitle("Banking Information System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "Login");
        mainPanel.add(registerPanel(), "Register");
        mainPanel.add(dashboardPanel(), "Dashboard");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
        setVisible(true);
    }

    private JPanel loginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(220, 240, 255));

        JLabel title = new JLabel("User Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(150, 20, 200, 30);

        JLabel userLabel = new JLabel("User ID:");
        userLabel.setBounds(100, 70, 100, 25);
        JTextField userField = new JTextField();
        userField.setBounds(200, 70, 180, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(100, 110, 100, 25);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(200, 110, 180, 25);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(180, 160, 120, 30);
        JButton toRegister = new JButton("No account? Register");
        toRegister.setBounds(160, 210, 160, 25);

        loginBtn.addActionListener(e -> {
            String userId = userField.getText();
            String pass = new String(passField.getPassword());
            User u = users.get(userId);
            if (u != null && u.password.equals(pass)) {
                currentUser = u;
                cardLayout.show(mainPanel, "Dashboard");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        toRegister.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        panel.add(title);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginBtn);
        panel.add(toRegister);
        return panel;
    }

    private JPanel registerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 240, 220));

        JLabel title = new JLabel("User Registration", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(130, 20, 250, 30);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(100, 70, 100, 25);
        JTextField nameField = new JTextField();
        nameField.setBounds(200, 70, 180, 25);

        JLabel userLabel = new JLabel("User ID:");
        userLabel.setBounds(100, 110, 100, 25);
        JTextField userField = new JTextField();
        userField.setBounds(200, 110, 180, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(100, 150, 100, 25);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(200, 150, 180, 25);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(180, 200, 120, 30);
        JButton toLogin = new JButton("Already have an account? Login");
        toLogin.setBounds(150, 250, 200, 25);

        registerBtn.addActionListener(e -> {
            String name = nameField.getText();
            String userId = userField.getText();
            String pass = new String(passField.getPassword());
            if (users.containsKey(userId)) {
                JOptionPane.showMessageDialog(this, "User ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                users.put(userId, new User(name, userId, pass));
                JOptionPane.showMessageDialog(this, "Registered successfully! Now login.");
                cardLayout.show(mainPanel, "Login");
            }
        });

        toLogin.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        panel.add(title);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(registerBtn);
        panel.add(toLogin);
        return panel;
    }

    private JPanel dashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(new Color(230, 255, 230));

        JLabel title = new JLabel("Welcome to Dashboard", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton viewBal = new JButton("View Balance");
        JButton deposit = new JButton("Deposit");
        JButton withdraw = new JButton("Withdraw");
        JButton logout = new JButton("Logout");

        viewBal.addActionListener(e -> JOptionPane.showMessageDialog(this, "Balance: Rs. " + currentUser.balance));

        deposit.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
            try {
                double amt = Double.parseDouble(amtStr);
                currentUser.balance += amt;
                JOptionPane.showMessageDialog(this, "Amount deposited successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        withdraw.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
            try {
                double amt = Double.parseDouble(amtStr);
                if (amt > currentUser.balance) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    currentUser.balance -= amt;
                    JOptionPane.showMessageDialog(this, "Amount withdrawn successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logout.addActionListener(e -> {
            currentUser = null;
            saveUsers();
            cardLayout.show(mainPanel, "Login");
        });

        panel.add(title);
        panel.add(viewBal);
        panel.add(deposit);
        panel.add(withdraw);
        panel.add(logout);
        return panel;
    }

    void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    User u = new User(parts[0], parts[1], parts[2]);
                    u.balance = Double.parseDouble(parts[3]);
                    users.put(u.userId, u);
                }
            }
        } catch (IOException e) {
            // Ignore
        }
    }

    void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users.values()) {
                pw.println(u.name + "," + u.userId + "," + u.password + "," + u.balance);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving user data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankingGUI::new);
    }
}
