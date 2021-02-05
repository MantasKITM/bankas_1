import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        createDefaultTables();
//        createDefaultAccounts();
        callPrintAllBankAccounts();
        try {
            moneyTransferByAccount();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void createDefaultTables() {
        BankasDAO obj = new BankasDAO();
        obj.createTable("CREATE TABLE IF NOT EXISTS transactions (trans_id INTEGER PRIMARY KEY AUTO_INCREMENT, tstamp TIMESTAMP, sender_id INTEGER, sender_name TEXT," +
                " receiver_id INTEGER, receiver_name TEXT, trans_value DOUBLE, total_after DOUBLE);");
        obj.createTable("CREATE TABLE IF NOT EXISTS user_accounts (account_id INTEGER PRIMARY KEY AUTO_INCREMENT, client_name TEXT, account_value INTEGER);");
    }
    private static void createDefaultAccounts() {
        BankasDAO obj = new BankasDAO();
        obj.createAccount("Džodronas Frenkas", 1110);
        obj.createAccount("Piteris Klarksonas", 1110);
        obj.createAccount("Frederikas Džonsonas", 1110);
        obj.createAccount("Abudabis Bretas", 1110);
        obj.createAccount("Lukas Skaivalkeris", 1110);
    }
    private static void callPrintAllBankAccounts() {
        BankasDAO obj = new BankasDAO();
        obj.printAllBankAccounts("SELECT * FROM user_accounts;");
    }
    private static void moneyTransferByAccount() throws SQLException {
        int sender, receiver;
        double sum;
        Scanner scanner = new Scanner(System.in);
        BankasDAO obj = new BankasDAO();
        System.out.println("<<< Pradedamas pervedimas >>>");
        System.out.println("Laukelyje nr.1 įveskite SIUNTĖJO sąskaitą paspauskite ENTER.");
        System.out.println("Laukelyje nr.2 įveskite GAVĖJO sąskaitą paspauskite ENTER.");
        System.out.println("Laukelyje nr.3 įveskite SUMĄ skaičiais kurią pervesite ir paspauskite ENTER.");
        sender = scanner.nextInt();
        receiver = scanner.nextInt();
        sum = scanner.nextDouble();
        System.out.printf("Vykdomas pervedimas: \n  Siuntėjo ID %2d >>>> Gavėjo ID %2d | SUMA: %10.2f  \n", sender, receiver, sum);
        obj.moneyTransfer(sender, receiver, sum);

    }
}
