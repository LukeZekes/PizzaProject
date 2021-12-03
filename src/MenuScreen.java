import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Class representing the menu screen of the application. Contains all elements of the menu.
 */
public class MenuScreen extends JFrame {

    /**
     * Initializing GUI elements
     * GUI layout is handled by IntelliJ UI Designer. See $$$setupUI$$$() below.
     */
    private JPanel menuPanel, pizzaPanel, drinkPanel, pizzaToppingsPanel;
    private JLabel menuLabel, pizzaMenuLabel, pizzaSizeLabel, pizzaCrustLabel, pizzaToppingsLabel, drinkLabel, drinkVarietyLabel, drinkSizeLabel, drinkIceLabel, printLabel;
    private JComboBox pizzaSizeBox, pizzaCrustBox, drinkVarietyBox, drinkSizeBox, drinkIceBox;
    private JCheckBox pepperoniCheckBox, mushroomCheckBox, sausageCheckBox, greenPepperCheckBox, baconCheckBox, onionCheckBox, blackOlivesCheckBox, extraCheeseCheckBox;
    private JButton addPizzaToOrderButton, addDrinkToOrderButton, printToReceiptButton, clearAllSelectionsButton;
    private JScrollPane cartDisplayScrollPane;
    private JTextArea cartDisplayTextArea;

    /**
     * LinkedList used for storing all checked checkboxes. Used in itemStateChanged to limit number of checkboxes to 4.
     */
    java.util.Queue<JCheckBox> checkedBoxes = new LinkedList<>();

    /**
     * Method to write attributes of the added pizza to the cart display text area
     *
     * @param item PizzaItem created by user
     */
    private void addPizzaToCart(OrderMenu.PizzaItem item) {
        String toppingsText = item.getToppings().toString();
        toppingsText = item.getToppings().size() > 0 ? toppingsText.substring(1, toppingsText.length() - 1) : "None";
        String costText = NumberFormat.getCurrencyInstance(Locale.US).format(item.getCost());

        String txt = "Pizza:\n+Size: " + item.getSize() + "\n+Crust: " + item.getCrust() + "\n+Toppings: " + toppingsText + " \n+Cost: " + costText + "\n\n";
        cartDisplayTextArea.append(txt);
    }

    /**
     * Method to write attributes of the added drink to the cart display text area
     *
     * @param item DrinkItem created by user
     */
    private void addDrinkToCart(OrderMenu.DrinkItem item) {
        String costText = NumberFormat.getCurrencyInstance(Locale.US).format(item.getCost());

        String txt = "Drink:\n+Variety: " + item.getVariety() + "\n+Size: " + item.getSize() + "\n+Ice: " + item.getIce() + "\n+Cost: " + costText + "\n\n";
        cartDisplayTextArea.append(txt);
    }

    /**
     * MenuScreen constructor
     * Also contains ActionListener and ItemListener methods, allowing buttons and checkboxes to function.
     *
     * @param title retrieves title set in Main
     */
    public MenuScreen(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(menuPanel);
        this.setSize(800, 600);
        this.pack();

        /**
         * Add ItemListener to each pizza topping checkbox
         * If any checkboxes are added or removed from the UI, they need to be added/removed from this array
         */
        JCheckBox[] checkBoxes = {pepperoniCheckBox, mushroomCheckBox, sausageCheckBox, greenPepperCheckBox, baconCheckBox, onionCheckBox, blackOlivesCheckBox, extraCheeseCheckBox};
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.addItemListener(new ItemListener() {
                /**
                 * ItemListener method to limit number of selected checkboxes to 4.
                 * @param e
                 */
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (checkedBoxes.size() == 4) {
                            JCheckBox removeBox = checkedBoxes.remove();
                            removeBox.setSelected(false);
                        }
                        checkedBoxes.add((JCheckBox) e.getItemSelectable());
                    } else {
                        checkedBoxes.remove((JCheckBox) e.getItemSelectable());
                    }
                }
            });
        }

        printToReceiptButton.addActionListener(new ActionListener() {
            /**
             * ActionListener that calls ReceiptPrinter.printReceipt(), using current customer info.
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("PRINTING RECEIPT");
                try {
                    ReceiptPrinter.printReceipt(Customer.currentCustomer, "receipt.txt");
                    printLabel.setForeground(Color.GREEN);
                    printLabel.setText("Receipt successfully printed!");
                } catch (IOException ioException) {
                    printLabel.setForeground(Color.RED);
                    printLabel.setText(ioException.getMessage());
                    ioException.printStackTrace();
                }
            }
        });

        addPizzaToOrderButton.addActionListener(new ActionListener() {
            /**
             * ActionListener used to get size, crust, and toppings chosen by user
             * Adds a new PizzaItem to OrderMenu.orderItemArray
             * Calls addPizzaToCart method to add it to the cart display text area
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String size = String.valueOf(pizzaSizeBox.getSelectedItem());
                String crust = String.valueOf(pizzaCrustBox.getSelectedItem());
                ArrayList<String> toppings = new ArrayList<>();
                for (JCheckBox selectedTopping : checkedBoxes) {
                    toppings.add(selectedTopping.getText());
                }
                OrderMenu.AddPizza(size, crust, toppings);
                addPizzaToCart((OrderMenu.PizzaItem) OrderMenu.orderItemArray.get(OrderMenu.orderItemArray.size() - 1));
            }
        });

        addDrinkToOrderButton.addActionListener(new ActionListener() {
            /**
             * ActionListener used to get size, ice, and variety of drink chosen by user
             * Adds a new DrinkItem to OrderMenu.orderItemArray
             * Calls addDrinkToCart method to add it to the cart display text area
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String size = String.valueOf(drinkSizeBox.getSelectedItem());
                String ice = String.valueOf(drinkIceBox.getSelectedItem());
                String variety = String.valueOf(drinkVarietyBox.getSelectedItem());
                OrderMenu.AddDrink(size, ice, variety);
                addDrinkToCart((OrderMenu.DrinkItem) OrderMenu.orderItemArray.get(OrderMenu.orderItemArray.size() - 1));
            }
        });

        clearAllSelectionsButton.addActionListener(new ActionListener() {
            /**
             * ActionListener that clears all items from OrderMenu.orderItemArray, as well as the cart display text area.
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderMenu.orderItemArray.clear();
                cartDisplayTextArea.setText("");
            }
        });

        /*pepperoniCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    if(checkedBoxes.size() == 4) {
                        JCheckBox removeBox = checkedBoxes.remove();
                        removeBox.setSelected(false);
                    }
                    checkedBoxes.add((JCheckBox) e.getItemSelectable());
                } else {
                    checkedBoxes.remove((JCheckBox) e.getItemSelectable());
                }
            }
        });*/
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayoutManager(5, 2, new Insets(10, 10, 10, 10), -1, -1));
        menuPanel.setAlignmentX(0.5f);
        menuPanel.setAlignmentY(0.5f);
        menuLabel = new JLabel();
        menuLabel.setText("Mom and Pop's Pizzeria: Menu");
        menuPanel.add(menuLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pizzaPanel = new JPanel();
        pizzaPanel.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        menuPanel.add(pizzaPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pizzaPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        pizzaMenuLabel = new JLabel();
        pizzaMenuLabel.setText("Pizza");
        pizzaPanel.add(pizzaMenuLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pizzaSizeLabel = new JLabel();
        pizzaSizeLabel.setText("Size");
        pizzaPanel.add(pizzaSizeLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pizzaSizeBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Small");
        defaultComboBoxModel1.addElement("Medium");
        defaultComboBoxModel1.addElement("Large");
        defaultComboBoxModel1.addElement("Extra-Large");
        pizzaSizeBox.setModel(defaultComboBoxModel1);
        pizzaPanel.add(pizzaSizeBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pizzaCrustLabel = new JLabel();
        pizzaCrustLabel.setText("Crust");
        pizzaPanel.add(pizzaCrustLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pizzaCrustBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Hand-Tossed");
        defaultComboBoxModel2.addElement("Stuffed Crust");
        pizzaCrustBox.setModel(defaultComboBoxModel2);
        pizzaPanel.add(pizzaCrustBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pizzaToppingsLabel = new JLabel();
        pizzaToppingsLabel.setText("Toppings");
        pizzaPanel.add(pizzaToppingsLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pizzaToppingsPanel = new JPanel();
        pizzaToppingsPanel.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        pizzaPanel.add(pizzaToppingsPanel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pepperoniCheckBox = new JCheckBox();
        pepperoniCheckBox.setText("Pepperoni");
        pizzaToppingsPanel.add(pepperoniCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mushroomCheckBox = new JCheckBox();
        mushroomCheckBox.setText("Mushroom");
        pizzaToppingsPanel.add(mushroomCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sausageCheckBox = new JCheckBox();
        sausageCheckBox.setText("Sausage");
        pizzaToppingsPanel.add(sausageCheckBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        greenPepperCheckBox = new JCheckBox();
        greenPepperCheckBox.setText("Green Pepper");
        pizzaToppingsPanel.add(greenPepperCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        baconCheckBox = new JCheckBox();
        baconCheckBox.setText("Bacon");
        pizzaToppingsPanel.add(baconCheckBox, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        onionCheckBox = new JCheckBox();
        onionCheckBox.setText("Onion");
        pizzaToppingsPanel.add(onionCheckBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        blackOlivesCheckBox = new JCheckBox();
        blackOlivesCheckBox.setText("Black Olives");
        pizzaToppingsPanel.add(blackOlivesCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        extraCheeseCheckBox = new JCheckBox();
        extraCheeseCheckBox.setText("Extra Cheese");
        pizzaToppingsPanel.add(extraCheeseCheckBox, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addPizzaToOrderButton = new JButton();
        addPizzaToOrderButton.setText("Add Pizza to Order");
        pizzaPanel.add(addPizzaToOrderButton, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        drinkPanel = new JPanel();
        drinkPanel.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        menuPanel.add(drinkPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        drinkPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        drinkLabel = new JLabel();
        drinkLabel.setText("Drinks");
        drinkPanel.add(drinkLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        drinkVarietyLabel = new JLabel();
        drinkVarietyLabel.setText("Variety");
        drinkPanel.add(drinkVarietyLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        drinkVarietyBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("Coke");
        defaultComboBoxModel3.addElement("Diet Coke");
        defaultComboBoxModel3.addElement("Sprite");
        defaultComboBoxModel3.addElement("Dr. Pepper");
        defaultComboBoxModel3.addElement("Water");
        drinkVarietyBox.setModel(defaultComboBoxModel3);
        drinkPanel.add(drinkVarietyBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        drinkSizeLabel = new JLabel();
        drinkSizeLabel.setText("Size");
        drinkPanel.add(drinkSizeLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        drinkSizeBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("Small");
        defaultComboBoxModel4.addElement("Medium");
        defaultComboBoxModel4.addElement("Large");
        defaultComboBoxModel4.addElement("Extra-Large");
        drinkSizeBox.setModel(defaultComboBoxModel4);
        drinkPanel.add(drinkSizeBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        drinkIceLabel = new JLabel();
        drinkIceLabel.setText("Ice");
        drinkPanel.add(drinkIceLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        drinkIceBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel5 = new DefaultComboBoxModel();
        defaultComboBoxModel5.addElement("Regular");
        defaultComboBoxModel5.addElement("Light");
        drinkIceBox.setModel(defaultComboBoxModel5);
        drinkPanel.add(drinkIceBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addDrinkToOrderButton = new JButton();
        addDrinkToOrderButton.setText("Add Drink to Order");
        drinkPanel.add(addDrinkToOrderButton, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        printToReceiptButton = new JButton();
        printToReceiptButton.setText("Print to Receipt");
        menuPanel.add(printToReceiptButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cartDisplayScrollPane = new JScrollPane();
        menuPanel.add(cartDisplayScrollPane, new GridConstraints(1, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cartDisplayTextArea = new JTextArea();
        cartDisplayTextArea.setEditable(false);
        cartDisplayScrollPane.setViewportView(cartDisplayTextArea);
        clearAllSelectionsButton = new JButton();
        clearAllSelectionsButton.setText("Clear All Selections");
        menuPanel.add(clearAllSelectionsButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        printLabel = new JLabel();
        printLabel.setText("");
        menuPanel.add(printLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return menuPanel;
    }

}
