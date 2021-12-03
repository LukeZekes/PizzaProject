import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Class that takes customer and order information to produce a receipt 
 */
public class ReceiptPrinter {
    public static int width;
    public static final String name = "Mom and Pop's Pizza Shop";
    public static final float TAX = 0.08f;

    /**
     * Creates a receipt and stores it in the file specified at outputFilePath.
     * @param customer The Customer object containing information about the user's account.
     * @param outputFilePath The path of the file that the receipt will be output to.
     * @throws IOException if there is an issue writing to file
     */
    public static void printReceipt(Customer customer, String outputFilePath) throws IOException {
        new File(outputFilePath).createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        StringBuilder sb = new StringBuilder();
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        float total = 0; //Needs to be calculated early so that the cost column can be the appropriate width
        for(OrderMenu.OrderItem item: OrderMenu.orderItemArray) {
            total += item.getCost();
        }

        int wCol1 = Math.max("No.".length(), String.valueOf(OrderMenu.orderItemArray.size()).length());
        int wCol2 = 50;
        int wCol3 = Math.max("Cost".length(), String.valueOf(moneyFormat.format(total)).length());
        width = wCol1 + wCol2 + wCol3 + 4;

        //#region Header
        sb.append(alignCenter("Receipt") + "\n\n");
        //#endregion

        //#region Customer Details
        sb.append(name + (alignCenter("") + "Customer details:\n").substring(name.length()));
        sb.append(getPadding(name.length()) + (alignCenter("") + "Name: " + customer.firstName + ", " + customer.lastName + "\n").substring(name.length()));
        sb.append(getPadding(name.length()) + (alignCenter("") + "Address: " + customer.streetAddress + "\n").substring(name.length()));
        sb.append(getPadding(name.length()) + (alignCenter("") + customer.city + ", " + customer.state + "\n").substring(name.length()));
        sb.append(getPadding(name.length()) + (alignCenter("") + "Phone no.: " + customer.phoneNum + "\n").substring(name.length()));
        //#endregion

        //#region Order Information
        sb.append("Payment for:\n");
        //Print table header
        
        String tableBorder = "";
        for(int i = 0; i < wCol1 + wCol2 + wCol3 + 4; i++)
            tableBorder += "-";
        sb.append(tableBorder + "\n");
        sb.append(getCellText("No.", wCol1));
        sb.append(getCellText("Item", wCol2));
        sb.append(getCellText("Cost", wCol3) + "|");
        sb.append("\n");
        sb.append(tableBorder + "\n");
        for(int i = 0; i < OrderMenu.orderItemArray.size(); i++) {
            sb.append(getCellText(i + 1 + "", wCol1));
            if(OrderMenu.orderItemArray.get(i) instanceof OrderMenu.PizzaItem) {
                OrderMenu.PizzaItem item = (OrderMenu.PizzaItem) OrderMenu.orderItemArray.get(i);
                ArrayList<String> toppings = item.getToppings();
                String fString = "Pizza: %s, %s, ";
                
                for (int j = 0; j < toppings.size(); j++) {
                    fString += toppings.get(j);
                    if(j < toppings.size() - 1) {
                        fString += ", ";
                    }
                }
                sb.append(getCellText(String.format(fString, item.getSize(), item.getCrust(), toppings), wCol2));
                //Reformat cost to look like actual money
                sb.append(getCellText(moneyFormat.format(item.cost), wCol3) + "|\n");
            }
            else {
                OrderMenu.DrinkItem item = (OrderMenu.DrinkItem) OrderMenu.orderItemArray.get(i);
                sb.append(getCellText(String.format("Drink: %s, %s ice, %s", item.getSize(), item.getIce(), item.getVariety(), moneyFormat.format(item.getCost())), wCol2));
                
                sb.append(getCellText(moneyFormat.format(item.cost), wCol3) + "|\n");
            }

        }
        sb.append(tableBorder + "\n");
        //#endregion
        
        //#region Tax and Total
        float orderTax = total * TAX;

        sb.append(getCellText("", wCol1));
        sb.append(getCellText(alignRight("Total", wCol2), wCol2));
        sb.append(getCellText(moneyFormat.format(total), wCol3) + "|\n");
        sb.append(tableBorder + "\n");

        sb.append(getCellText("", wCol1));
        sb.append(getCellText(alignRight(String.format("Tax(%s%%)", TAX * 100), wCol2), wCol2));
        sb.append(getCellText(moneyFormat.format(orderTax), wCol3) + "|\n");
        sb.append(tableBorder + "\n");

        sb.append(getCellText("", wCol1));
        sb.append(getCellText(alignRight("Total + tax", wCol2), wCol2));
        sb.append(getCellText(moneyFormat.format(total + orderTax), wCol3) + "|\n");
        sb.append(tableBorder + "\n");


        //#endregion
        
        //#region Footer
        String dateStr = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        sb.append("Date:" + dateStr);
        //#endregion
        writer.write(sb.toString());
        writer.close();
        System.out.println(sb.toString());
    }

    /**
     * Returns a string that is aligned to the center of the receipt.
     * @param str The string to be aligned.
     * @return A string that is padded on the left to center the text in the width of the receipt.
     */
    private static String alignCenter(String str) {
        int paddingSize = (width - str.length()) / 2;
        String newStr = "";
        for(int i = 0; i < paddingSize; i++)
            newStr += " ";
        newStr += str;
        return newStr;
    }

    /**
     * Returns a string that is aligned to the right of an area of text with width <i>areaWidth</i>.
     * @param str The string to be aligned.
     * @param areaWidth The width of the area that the string will be aligned to.
     * @return A string that is padded on the left to right-justify the text in an area with width <i>areaWidth</i>.
     */
    private static String alignRight(String str, int areaWidth) {
        int paddingSize = areaWidth - str.length();
        String newStr = "";
        for(int i = 0; i < paddingSize; i++)
            newStr += " ";
        newStr += str;
        return newStr;
    }
    
    /**
     * Returns a String consisting of a number of spaces to be used as padding.
     * @param paddingSize The amount of characters long the returned String should be.
     * @return a String consisting of <i>paddingSize</i> spaces.
     */
    private static String getPadding(int paddingSize) {
        String newStr = "";
        for(int i =0; i < paddingSize; i++)
            newStr += " ";

        return newStr;
    }

    /**
     * Accepts text and the width of a cell, and returns a String representing that cell, plus a left border
     * @param str The text in the cell
     * @param width The width of the cell
     * @return A String consisting of a border character "|", <i>str</i> truncated to fit in the cell, and padding to reach <i>width</i>, 
     */
    private static String getCellText(String str, int width) {
        str = truncateString(str, width);
        int rightPadding = width - str.length();
        String newStr = "|" + str;
        for(int i = 0; i < rightPadding; i++) {
            newStr += " ";
        }
        return newStr;
    }

    /**
     * Truncates a String to fit within a specified width
     * @param str The String to be truncated
     * @param maxWidth The maximum width of the String
     * @return If <i>str</i>.length() <= <i>maxWidth</i>, then returns the original string.
     * <li>Otherwise, return a truncated version of the string with "..." at the end, so that
     *  the entire String including the ellipsis, has width <i>maxWidth</i>.</li>
     */
    private static String truncateString(String str, int maxWidth) {
        if(str.length() <= maxWidth)
            return str;
        String newStr = "";
        newStr += str.substring(0, maxWidth - 3) + "...";
        return newStr;
    }
}