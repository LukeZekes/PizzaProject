import javax.swing.*;
import java.awt.*;

public class Main{
    public static void main(String[] args){
        String title = "Mom and Pop's Pizzeria";

        JFrame frame = new LoginScreen(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.pack();
        frame.setVisible(true);
    }
}
