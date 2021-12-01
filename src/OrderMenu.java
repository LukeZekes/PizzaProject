import java.util.ArrayList;
/**
 * Class to handle the addition of items to the order.
 * <p>Instead of instantiating an object of this class, the list of items and methods to add items are all <b>static</b>.</p>
 */
public class OrderMenu {
    /**
     * Contains all of the items in the order.
     */
    public static ArrayList<OrderItem> orderItemArray = new ArrayList<OrderItem>();

    /**
     * Adds a pizza object to the order with the specified customizaton.
     * @param size The size of the pizza. The valid sizes are (case insensitive): "SMALL", "MEDIUM", "LARGE", "EXTRA-LARGE".
     * Any size that does match one of those will default to "EXTRA LARGE" and print a warning message to the console.
     * @param crust The type of the crust.
     * @param toppings The toppings on the pizza.
     */
    public static void AddPizza(String size, String crust, String[] toppings){
        orderItemArray.add(new PizzaItem(size, crust, toppings));
    };
    /**
     * Adds a drink object to the order with the specified customization.
     * @param size The size of the drink. The valid sizes are (case insensitive): "SMALL", "MEDIUM", "LARGE", "EXTRA-LARGE".
     * Any size that does match one of those will default to "EXTRA LARGE" and print a warning message to the console.
     * @param ice The amount of ice in the drink.
     * @param variety The variety of the drink. <i>E.g. Water, Coca-Cola, Sprite Zero, etc.</i> 
     */
    public static void AddDrink(String size, String ice, String variety){
        orderItemArray.add(new DrinkItem(size, ice, variety));
    };
    /**
     * Class that PizzaItem and DrinkItem inherit from.
     */
    private static class OrderItem {
        float cost;
        String size;
        /**
         * @return The cost of the item.
         */
        float getCost() {return cost;}
        /**
         * @return The size of the item.
         */
        String getSize() {return size;}
    }
    /**
     * A class representing a pizza with a size, crust, number of toppings, and cost.
     */
    private static class PizzaItem extends OrderItem {
        String crust;
        String[] toppings;
        final static float sCost = 5.99f;
        final static float mCost = 7.99f;
        final static float lCost = 9.99f;
        final static float xLCost = 12.99f;

        /**
         * Creates an instance of the PizzaItem class. The cost of the item is calculated based on the size of the pizza.
         * @param size The size of the pizza. The valid sizes are (case insensitive): "SMALL", "MEDIUM", "LARGE", "EXTRA-LARGE".
         * Any size that does match one of those will default to "EXTRA LARGE" and print a warning message to the console.
         * @param crust The type of the crust
         * @param toppings The toppings on the pizza
         */
        PizzaItem(String size, String crust, String[] toppings) {
            this.size = size.toUpperCase();
            this.crust = crust;
            this.toppings = toppings;
            switch(this.size) {
                case "SMALL":
                    this.cost = sCost;
                    break;
                case "MEDIUM":
                    this.cost = mCost;
                    break;
                case "LARGE":
                    this.cost = lCost;
                    break;
                case "EXTRA-LARGE":
                    this.cost = xLCost;
                    break;
                default:
                    System.out.println("Invalid size, defaulting to EXTRA-LARGE");
                    this.size = "EXTRA-LARGE";
                    this.cost = xLCost;
                    break;
            }
        }
        /**
         * @return The type of crust of the pizza
         */
        String getCrust() {return crust;}
        /**
         * @return The toppings on the pizza in an array
         */
        String[] getToppings() {return toppings; }

        /**
         * @return Returns a String in the following format:
         * <p>Pizza: <i>size</i>, <i>crust</i>, <i>toppings</i>, $<i>cost</i></p>
         */
        public String toString() {
            String out = "Pizza: " + size + ", " + crust + ", ";
            for (String t : toppings) {
                out += t + ", ";
            }
            out += "$" + cost;
            return out; 
        }
    }
    /**
     * A class representing a drink with a size, amount of ice, variety, and cost.
     */
    private static class DrinkItem extends OrderItem {
        String ice, variety;
        final static float sCost = 1.00f;
        final static float mCost = 1.80f;
        final static float lCost = 2.00f;
        final static float xLCost = 2.50f;

        /**
         * Creates an instance of the DrinkItem class. The cost of the item is calculated based on the size of the drink.
         * @param size The size of the drink. The valid sizes are (case insensitive): "SMALL", "MEDIUM", "LARGE", "EXTRA-LARGE".
         * Any size that does match one of those will default to "EXTRA LARGE" and print a warning message to the console.
         * @param ice The amount of ice in the drink. <i>E.g. Light, Regular</i>
         * @param variety The variety of the drink. <i>E.g. Water, Coca-Cola, Sprite Zero, etc.</i> 
         */
        DrinkItem(String size, String ice, String variety) {
            this.size = size.toUpperCase();
            this.ice = ice;
            this.variety = variety;
            switch(this.size) {
                case "SMALL":
                    this.cost = sCost;
                    break;
                case "MEDIUM":
                    this.cost = mCost;
                    break;
                case "LARGE":
                    this.cost = lCost;
                    break;
                case "EXTRA-LARGE":
                    this.cost = xLCost;
                    break;
                default:
                    System.out.println("Invalid size, defaulting to EXTRA-LARGE");
                    this.size = "EXTRA-LARGE";
                    this.cost = xLCost;
                    break;
            }
        }

        /**
         * @return The amount of ice in the drink. <i>E.g. Light, Regular</i>
         */
        String getIce() {return ice;}
        /**
         * @return The variety of the drink. <i>E.g. Water, Coca-Cola, Sprite Zero, etc.</i> 
         */
        String getVariety() {return variety;}

        /**
         * @return Returns a String in the following format:
         * <p>Drink: <i>size</i>, <i>variety</i>, <i>ice</i> ice, $<i>cost</i></p>
         */
        public String toString() {
            String out = "Drink: " + size + ", " + variety + ", " + ice + " ice, $" + cost;
            return out;
        }
    }
}