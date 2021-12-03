import javax.swing.*;

/**
 * Main class that launches the application.
 */
public class Main{
    /**
     * Main method. Sets title of the program and initializes a new LoginScreen.
     * @param args
     */
    public static void main(String[] args){
        String title = "Mom and Pop's Pizzeria";

        JFrame frame = new LoginScreen(title);
        frame.setVisible(true);
    }
}
