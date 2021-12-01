import java.io.*;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;


public class Customer {
    final int DEFAULT_ORDER_SIZE = 10;
    final static PhoneNumberFormat DEFAULT_PHONE_NUMBER_FORMAT = PhoneNumberFormat.NATIONAL;
    final static String path = "customers.txt";
    public String firstName, lastName, phoneNum, email, fullAddress, streetAddress, city, state, ZIP, password;
    public int id; //-1 if guest
    
    //#region Creating/Retrieving Account
    /**
     * Private constructor used when creating a new account or creating a guest account.
     * <b>DO NOT CALL TO CREATE A CUSTOMER!</b> Use <i>createAccount()</i> or <i>createGuestAccount()</i>.
     * @param fn First name
     * @param ln Last name
     * @param pn Phone number
     * @param e Email address
     * @param sa Street Address
     * @param c City
     * @param s State
     * @param z ZIP code
     * @param pw Password
     * @param isGuest True if this is only a guest account, false if this is a new account to be saved
     * @throws IOException
     */
    private Customer(String fn, String ln, String pn, String e, String sa, String c, String s, String z, String pw, boolean isGuest) throws IOException {
        if(isGuest) {
            id = -1;
        }
        else {
            id = incrementNumCustomers()[0];
        }
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
    }
    /**
     * Private constructor used when retrieving an account from the database.
     * <b>DO NOT CALL TO CREATE A CUSTOMER!</b> Use <i>retrieveAccount()</i> or <i>createGuestAccount()</i>.
     * @param fn First name
     * @param ln Last name
     * @param pn Phone number
     * @param e Email address
     * @param sa Street Address
     * @param c City
     * @param s State
     * @param z ZIP code
     * @param pw Password
     * @param existingID ID of customer's account
     * @t
     */
    private Customer(String fn, String ln, String pn, String e, String sa, String c, String s, String z, String pw, int existingID) throws IOException {
        firstName = fn;
        lastName = ln;
        phoneNum = pn;
        email = e;
        streetAddress = sa;
        city = c;
        state = s;
        ZIP = z;
        fullAddress = String.format("%s,%s,%s,%s", streetAddress, city, state, ZIP);
        password = pw;
        id = existingID;
    }
    
    /**
     * Creates a new customer account and saves it to the database
     * @param fn First name
     * @param ln Last name
     * @param pn Phone number
     * @param e Email address
     * @param sa Street address
     * @param c City
     * @param s State
     * @param z ZIP
     * @param pw Password
     * @return A Customer object with the included information, that has been saved to the database with a unique ID
     * @return null if an account with that phone number already exists
     * @throws IOException

     */
    public static Customer createAccount(String fn, String ln, String pn, String e, String sa, String c, String s, String z, String pw) throws IOException {
        try {
            //Check if phone number is valid
            if(!validatePhoneNumber(pn))
                throw new Exception("That is not a valid US phone number!");
            //Reformat phone number to standard format Customer.DEFAULT_PHONE_NUMBER_FORMAT: 1234567890 -> (123) 456 7890
            pn = reformatPhoneNumber(pn);
            //Check for account with same phone number            
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            reader.readLine();
            while((line = reader.readLine()) != null) {
                if(line.split(",")[3].equals(pn)) {
                    //An account already has this phone number
                    reader.close();
                    throw new Exception("An account with the phone number already exists");
                }
            }
            reader.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        Customer customer = new Customer(fn, ln, pn, e, sa, c, s, z, pw, false);
        saveCustomer(customer);
        return customer;
    }
    /**
     * Creates a guest customer account 
     * @param fn First name
     * @param ln Last name
     * @param pn Phone number
     * @param sa Street address
     * @param c City
     * @param s State
     * @param z ZIP
     * @return A Customer object with the included information, and an ID of -1
     * @throws IOException
     */
    public static Customer createGuestAccount(String fn, String ln, String pn, String sa, String c, String s, String z) throws IOException {
        return new Customer(fn, ln, pn, null, sa, c, s, z, null, true);
    }
 
    /**
     * Searches for an account in the database using a phone number and password
     * @param phoneNum
     * @param password
     * @return A Customer object with the account's information and ID
     * @throws Exception if no customer is found with that information
     */
    public static Customer retrieveAccount(String phoneNum, String password) throws Exception {
        if(!validatePhoneNumber(phoneNum))
            throw new Exception("That is not a valid US phone number.");

        phoneNum = reformatPhoneNumber(phoneNum);
        //Locate customer
        BufferedReader reader = new BufferedReader(new FileReader(path));
        boolean found = false;
        String line;
        reader.readLine();
        while((line = reader.readLine()) != null) {
            String linePhoneNum = line.split(",")[3];
            String linePassword = line.split(",")[5];
            if(linePhoneNum.equals(phoneNum) && linePassword.equals(password)) {
                found = true;
                break;
            }
        }
        reader.close();
        if(found) {
            //Build a customer object from this data
            System.out.println(line);
            String[] data = line.split(",");
            Customer customer = new Customer(data[1], data[2], data[3], data[4], data[6], data[7], data[8], data[9], data[5], Integer.parseInt(data[0]));
            return customer;
        }
        else {
            throw new Exception("Customer with phone number " + phoneNum + " not found!");
        }
    }
    //#endregion
    
    //#region Modify Database/Num Customers
    /**
     * Saves a Customer to the database as a string in the format:
     * <p>id,first name,last name,phone number,email,password,full address</p>
     * @param customer The Customer object to be saved
     * @throws IOException
     */
    private static void saveCustomer(Customer customer) throws IOException {
        StringBuilder sb = new StringBuilder();
        String info = String.format("%d,%s,%s,%s,%s,%s,%s", customer.id, customer.firstName, customer.lastName,
                customer.phoneNum, customer.email, customer.password, customer.fullAddress);
        sb.append(info);
        sb.append("\n");
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
        writer.write(sb.toString());
        writer.close();
    }
    /**
     * Attempts to delete the account with the specified ID
     * @param customerID The ID number of the account to delete
     * @return True if successful, false if customer could not be found
     * @throws IOException
     */
    public static boolean deleteAccount(int customerID) throws IOException {
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
        reader.close();
        if(found) {
            reader = new BufferedReader(new FileReader(path));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (Integer.parseInt(line.split(",")[0]) != matchID) {
                    sb.append(line);
                    sb.append("\n");
                }
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(sb.toString());
            writer.close();
            return true;
        } else
            return false;
    }
    
    /**
     * Increments the current and total number of customers and updates customers.txt to reflect these new values.
     * @return An array of integers, where [0] is the current number of stored customers and [1] is the total number of customers
     * that have ever been stored
     * @throws IOException
     */
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

    /**
     * Decrements the number of current customers and updates customers.txt to reflect these new values.
     * If for some reason either value attempts to go below zero, the file will be reset so that both values are zero and there are no accounts saved
     * @return An array of integers, where [0] is the current number of stored customers and [1] is the total number of customers
     * @throws IOException
     */
    private static int[] decrementNumCustomers() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        StringBuilder sb = new StringBuilder();
        String line;
        int[] numCustomers = getNumCustomers();

        if(numCustomers[0] <= 0 || numCustomers[1] <= 0) {
            sb.append("0,0");
            reader.close();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
            bufferedWriter.write(sb.toString());
            bufferedWriter.close();
            return new int[]{0, 0};
        }

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
    /**
     * Gets the <b>current</b> number of customers stored in customers.txt, and the <b>total</b> number of customers
     * that have been stored (including deleted entires).
     * @return an array of integers, where [0] = current number of customers,[1] = total number of customers
     * @throws IOException
     */
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
    //#endregion

    /**
     * Validates that a provided phone number is, in fact, a valid phone number.
     * <p>Uses Google's <i>libphonenumber</i> library</p>
     * @param phoneNumber The number to be validated
     * @return True if the number is a valid phone number
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        try {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "US"); 
        if(phoneNumberUtil.isValidNumber(number)) {
            return true;
        }
        else {
            System.out.println("Error: " + phoneNumber + " is not a valid US phone number.");
            return false;
        }
        } catch(NumberParseException e) {
            System.out.println("Error: " + phoneNumber + " is not a valid US phone number.");
            return false;
        }
    }
    /**
     * Reformats the provided number to Customer.DEFAULT_PHONE_NUMBER_FORMAT, and returns the modified string
     * @param phoneNumber The number to be reformatted
     * @return A new string consisting of phoneNumber reformatted to Customer.DEFAULT_PHONE_NUMBER_FORMAT.
     * Returns phoneNumber if phoneNumber is not a valid phone number
     */
    public static String reformatPhoneNumber(String phoneNumber) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "US");
            return phoneNumberUtil.format(number, DEFAULT_PHONE_NUMBER_FORMAT);
        } catch(NumberParseException e) {
            System.out.println(phoneNumber + " is not a valid US phone number.");
            return phoneNumber;
        }
    }
}
