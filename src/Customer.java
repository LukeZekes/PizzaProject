import java.io.*;

public class Customer {
    final int DEFAULT_ORDER_SIZE = 10;
    final static String path = "customers.txt";
    public String firstName, lastName, phoneNum, email, fullAddress, streetAddress, city, state, ZIP, password, orderComments;
    public int id; //-1 if guest
    OrderItem[] cartArray;
//    public static int numCustomers = 0;
    private static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    private Customer(String fn, String ln, String pn, String e, String sa, String c, String s, String z, String pw, boolean isNew) throws IOException {
        if(isNew) //Guest
            id = incrementNumCustomers()[1];
        else
            id = -1;
        cartArray = new OrderItem[0];
        firstName = fn;
        lastName = ln;
        phoneNum = pn;
        email = e;
        streetAddress = sa;
        city = c;
        state = s;
        ZIP = z;
//        fullAddress = String.format("%s\n%s,%s\n%s", streetAddress, city, state, ZIP);
        fullAddress = String.format("%s,%s,%s,%s", streetAddress, city, state, ZIP);
        password = pw;
        orderComments = "";
    }
    public static Customer CreateAccount(String fn, String ln, String pn, String e, String sa, String c, String s, String z, String pw) throws IOException {
        Customer customer = new Customer(fn, ln, pn, e, sa, c, s, z, pw, true);
        try {
            SaveCustomer(customer);
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
        return customer;
    }
    public static Customer CreateGuestAccount(String fn, String ln, String pn, String sa, String c, String s, String z) throws IOException {
        return new Customer(fn, ln, pn, null, sa, c, s, z, null, false);
    }

    private static void SaveCustomer(Customer customer) throws IOException {
        StringBuilder sb = new StringBuilder();
        String info = String.format("%d,%s,%s,%s,%s,%s,%s", customer.id, customer.firstName, customer.lastName,
                customer.phoneNum, customer.email, customer.password, customer.fullAddress, customer.password);
        sb.append(info);
        sb.append("\n");
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
        writer.write(sb.toString());
        writer.close();
    }

    //Returns true if there exists a customer with and id of customerID
    public static boolean DeleteCustomer(int customerID) throws IOException {
        int matchID = customerID;
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        //Check if customer is in file
        while((line = reader.readLine()) != null) {
            if(Integer.parseInt(line.split(",")[0]) == matchID) {
                found = true;
                int[] _decrementedNum = decrementNumCustomers();
                sb.append(_decrementedNum[0]);
                sb.append(",");
                sb.append(_decrementedNum[1]);
                sb.append("\n");
                break;
            }
        }
        if(found) {
            reader = new BufferedReader(new FileReader(path));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (Integer.parseInt(line.split(",")[0]) != matchID) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(sb.toString());
            writer.close();
            return true;
        } else
            return false;
    }
    //Increments the current and total number of customers and updates customers.txt to reflect it
    //Returns an array, where [0] is current number of customers and [1] is total number
    private static int[] incrementNumCustomers() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        StringBuilder sb = new StringBuilder();
        String line;
        int[] numCustomers = getNumCustomers();
        sb.append(numCustomers[0] + 1);
        sb.append(",");
        sb.append(numCustomers[1] + 1);
        sb.append("\n");
        //Copy the rest of the lines
        reader.readLine(); //Starts copying at second line
        while((line = reader.readLine()) != null) {
            //System.out.println(line);
            sb.append(line);
            sb.append("\n");
        }
        reader.close();
        //Override file
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        bufferedWriter.write(sb.toString());
        bufferedWriter.close();
        return new int[]{numCustomers[0] + 1, numCustomers[1] + 1};
    }
    //Decrements the number of current customers and updates customers.txt to reflect it, won't go below zero
    //Returns an array, where [0] is current number of customers and [1] is total number
    private static int[] decrementNumCustomers() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        StringBuilder sb = new StringBuilder();
        String line;
        int[] numCustomers = getNumCustomers();

        if(numCustomers[0] <= 0 || numCustomers[1] <= 0)
            return new int[]{0, 0};
        sb.append(numCustomers[0] - 1);
        sb.append(",");
        sb.append(numCustomers[1]);
//        sb.append("\n");
        //Copy the rest of the lines
        reader.readLine(); //Starts copying at second line
        while((line = reader.readLine()) != null) {
            //System.out.println(line);
            sb.append("\n");
            sb.append(line);
        }
        reader.close();
        //Override file
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        bufferedWriter.write(sb.toString());
        bufferedWriter.close();
        return new int[]{numCustomers[0] - 1, numCustomers[1]};
    }
    //Gets the current number of customers stored in customers.txt, and the total number of non-guest customers that have been stored (including deleted entires)
    //getNumCustomers()[0] = current number of customers, getNumCustomers()[1] = total number of customers
    private static int[] getNumCustomers() throws IOException {
        try {
            BufferedReader file = new BufferedReader(new FileReader(path));
            String[] line = file.readLine().split(",");
            file.close();
            //System.out.println(line);
            //if(line == null || line.equals("") || line.getBytes()[0] == b) {
            /*if (!isInteger(line[0])) {
                //Write 0 to first line of file and return 0 as number of customers
                FileWriter writer = new FileWriter(path);
//            writer.write("0, 0\n");
                writer.write("0,0");
                writer.close();
                return new int[]{0, 0};
            } else*/
            return new int[]{Integer.parseInt(line[0]), Integer.parseInt(line[1])};
        } catch (NullPointerException nullPointerException) {
            FileWriter writer = new FileWriter(path);
//          writer.write("0, 0\n");
            writer.write("0,0");
            writer.close();
            return new int[]{0, 0};
        } catch (NumberFormatException numberFormatException) {
            FileWriter writer = new FileWriter(path);
//          writer.write("0, 0\n");
            writer.write("0, 0");
            writer.close();
            return new int[]{0, 0};
        }
    }


    public void NewOrder() {
        cartArray = new OrderItem[DEFAULT_ORDER_SIZE];
    }
    public void AddOrderComments(String comment) {
        orderComments = comment;
    }
}
