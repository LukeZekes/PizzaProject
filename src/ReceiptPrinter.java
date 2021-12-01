import java.io.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;



/**
 * Class that takes customer and order information to produce a receipt 
 */
public class ReceiptPrinter {
    /**
     * Creates a receipt and stores it in the file specified at outputFilePath.
     * @param customer The Customer object containing information about the user's account.
     * @param order The OrderMenu object containing information about the order.
     * @param outputFilePath The path of the file that the receipt will be output to.
     */
    public static void printReceipt(Customer customer, String outputFilePath) {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb, Locale.US);
        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        final String centerPadding = "";

        
        float total = 0;
        final float TAX = 0.08f;
        //Heading
        sb.append(centerPadding + "Mom and Pop's Pizza Shop\n");
        sb.append("Receipt\n");
        //Customer details
        sb.append("Customer details:\n");
        sb.append("Name: " + customer.firstName + " " + customer.lastName + "\n");
        sb.append("Address: " + customer.fullAddress + "\n");
        sb.append("Phone number: " + customer.phoneNum + "\n");

        //Order information
        sb.append("Payment for:\n");
        for(int i = 0; i < OrderMenu.orderItemArray.size(); i++) {
            // OrderMenu.OrderItem temp = OrderMenu.orderItemArray.get(i);
            sb.append(i + 1 + " ");
            if(OrderMenu.orderItemArray.get(i) instanceof OrderMenu.PizzaItem) {
                OrderMenu.PizzaItem item = (OrderMenu.PizzaItem) OrderMenu.orderItemArray.get(i);
                // OrderMenu.PizzaItem item = (OrderMenu.PizzaItem) temp;
                fmt.format("Pizza: %s, %s, ", item.getSize(), item.getCrust());
                ArrayList<String> toppings = item.getToppings();
                if(toppings.size() <= 3) {
                    for (int j = 0; j < toppings.size(); j++) {
                        sb.append(toppings.get(j));
                        if(j < toppings.size() - 1) {
                            sb.append(", ");
                        }
                        else {
                            sb.append(" - ");
                        }
                    }
                }
                //Reformat cost to look like actual money
                sb.append("" + moneyFormat.format(item.cost) + "\n");
                total += item.getCost();
            }
            else {
                OrderMenu.DrinkItem item = (OrderMenu.DrinkItem) OrderMenu.orderItemArray.get(i);                
                fmt.format("Drink: %s, %s ice, %s - %s%n", item.getSize(), item.getIce(), item.getVariety(), moneyFormat.format(item.getCost()));
                total += item.getCost();
            }

        }

        //Tax and total
        float orderTax = total * TAX;
        fmt.format("Tax(%s%%) - %s%n", TAX * 100, moneyFormat.format(orderTax));
        fmt.format("Total - %s%n", moneyFormat.format(total += orderTax));

        //Footer
        Date currentDate = new Date();
        String dateStr = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        fmt.format("Date: %s", dateStr);
        fmt.close();
        System.out.println(sb.toString());
    }
}