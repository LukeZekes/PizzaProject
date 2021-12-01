import javax.swing.*;

public class LoginScreen extends JFrame{
    private JPanel loginPanel;
    private JLabel shopName;
    private JTextField userTextField;
    private JLabel userLabel;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JButton createAccountButton;
    private JLabel passLabel;

    public LoginScreen(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(loginPanel);
        this.pack();
    }
}
