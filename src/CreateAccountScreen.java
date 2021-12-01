import javax.swing.*;

public class CreateAccountScreen extends JFrame{
    private JPanel createAccountPanel;
    private JLabel accountCreationLabel;
    private JTextField firstNameTextField;
    private JLabel firstNameLabel;
    private JTextField lastNameTextField;
    private JTextField phoneNumTextField;
    private JTextField emailTextField;
    private JTextField addressTextField;
    private JTextField cityTextField;
    private JTextField stateTextField;
    private JTextField zipTextField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton createAccountButton;
    private JButton returnToLoginScreenButton;
    private JLabel lastNameLabel;
    private JLabel phoneNumLabel;
    private JLabel emailLabel;
    private JLabel addressLabel;
    private JLabel cityLabel;
    private JLabel stateLabel;
    private JLabel zipLabel;
    private JLabel passLabel;
    private JLabel confirmPassLabel;

    public CreateAccountScreen(String title){
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(createAccountPanel);
        this.pack();
    }
}
