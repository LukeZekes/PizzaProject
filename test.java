import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        GenerateCustomers(5);
        Customer.DeleteCustomer(7);
        Customer.DeleteCustomer(8);
    }
    public static void GenerateCustomers(int num) throws IOException {
        Customer[] customers = new Customer[num];
        for(int i = 0; i < num; i++) {
            customers[i] = Customer.CreateAccount("Luke", "Zeches", i + "23456789", "zechesl@gmail.com", "350 Wickerberry Lane", "Roswell", "GA", "30075", "123pass");
        }
    }
}
