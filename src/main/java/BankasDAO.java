import java.sql.*;

public class BankasDAO {

    private final String URL = "jdbc:mysql://localhost:3306/bank";
    private final String user = "root";
    private final String passwd = "";
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;
    private String query;
    private ResultSet rs;

    public void createTable(String query) {
        try {
            conn = DriverManager.getConnection(URL, user, passwd);
            System.out.println("Prisijungta prie duomenų bazės.");
            stmt = conn.createStatement();
            stmt.execute(query);
            System.out.println("Lentelė sukurta sėkmingai.");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Nuo duomenų bazės atsijungta.");
        }
    }

    public void createAccount(String name, int value) {
        try {
            conn = DriverManager.getConnection(URL, user, passwd);
            query = "INSERT INTO user_accounts (client_name, account_value) VALUES (?, ?);";
            System.out.println("Prisijungta prie duomenų bazės.");
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, value);
            pstmt.executeUpdate();
            System.out.println("Sąskaita sukurta sėkmingai.");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Nuo duomenų bazės atsijungta.");
        }
    }

    public void printAllBankAccounts(String query) {
        try {
            conn = DriverManager.getConnection(URL, user, passwd);
            System.out.println("Prisijungta prie duomenų bazės.");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                String holder = rs.getString(2);
                double value = rs.getDouble(3);

                System.out.printf("Sąskaitos ID: %2d, Savininkas %21s Likutis: %15.2f EUR \n", id, holder, value);
            }
            System.out.println("Visas banko klientų sąrašas atspausdintas.");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Nuo duomenų bazės atsijungta.");
        }
    }


    public void moneyTransfer(int sender_id, int receiver_id, double sum) throws SQLException {
        Account sender = returnSendersDataAndCheckIfExists(sender_id);
        Account receiver = returnReceiversDataAndCheckIfExists(receiver_id);
        if ((sender.isDoExists() && receiver.isDoExists()) && (sender.getValue() >= sum)) {
            try {

                double senders_balance_after = sender.getValue() - sum;
                double receivers_balance_after = receiver.getValue() + sum;


                conn = DriverManager.getConnection(URL, user, passwd);
                conn.setAutoCommit(false);


                pstmt = conn.prepareStatement("UPDATE user_accounts SET account_value = ? WHERE account_id = ?");
                pstmt.setDouble(1, senders_balance_after);
                pstmt.setInt(2, sender_id);
                pstmt.execute();

                pstmt = conn.prepareStatement("UPDATE user_accounts SET account_value = ? WHERE account_id = ?");
                pstmt.setDouble(1, receivers_balance_after);
                pstmt.setInt(2, receiver_id);
                pstmt.execute();

                pstmt = conn.prepareStatement("INSERT INTO transactions (sender_id, sender_name, receiver_id, receiver_name, trans_value, total_after) VALUES(?,?,?,?,?,?)");
                pstmt.setInt(1, sender.getID());
                pstmt.setString(2, sender.getName());
                pstmt.setInt(3, receiver.getID());
                pstmt.setString(4, receiver.getName());
                pstmt.setDouble(5, sum);
                pstmt.setDouble(6, receivers_balance_after);
                pstmt.execute();

                conn.commit();



            } catch (SQLException throwables) {
                throwables.printStackTrace();
                conn.rollback();

            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            } else {
            if  (!receiver.isDoExists()) {
                System.out.println("Gavėjas pagal pateiktą ID nerastas.");
            }  if (!sender.isDoExists()) {
                System.out.println("Siuntėjas pagal pateiktą ID nerastas.");
            } else if (sender.getValue() < sum) {
                System.out.println("Siutėjo sąskaitos likutis nepakankamas pervedimui.");
            }
        }


    }


    public Account returnSendersDataAndCheckIfExists(int sender_id) {
        Account sender_Account = new Account();
        try {
            conn = DriverManager.getConnection(URL, user, passwd);
            query = "SELECT * FROM user_accounts WHERE account_id = ?";
            pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setInt(1, sender_id);
            ResultSet rs = pstmt.executeQuery();
            rs.beforeFirst();
            rs.next();
            sender_Account.setID(rs.getInt(1));
            sender_Account.setName(rs.getString(2));
            sender_Account.setValue(rs.getDouble(3));
            if (rs.wasNull()) {
                sender_Account.setDoExists(false);
            } else {
                sender_Account.setDoExists(true);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return sender_Account;
    }

    public Account returnReceiversDataAndCheckIfExists(int receiver_id) {
        Account receiver_Account = new Account();
        try {
            conn = DriverManager.getConnection(URL, user, passwd);
            query = "SELECT * FROM user_accounts WHERE account_id = ?";
            pstmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setInt(1, receiver_id);
            ResultSet rs = pstmt.executeQuery();
            rs.beforeFirst();
            rs.next();
            receiver_Account.setID(rs.getInt(1));
            receiver_Account.setName(rs.getString(2));
            receiver_Account.setValue(rs.getDouble(3));
            if (rs.wasNull()) {
                receiver_Account.setDoExists(false);
            } else {
                receiver_Account.setDoExists(true);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return receiver_Account;
    }
}

