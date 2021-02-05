public class Account {
    private int ID;
    private String name;
    private double value;
    private boolean doExists;

    public Account(int ID, String name, double value, boolean ifExists) {
        this.ID = ID;
        this.name = name;
        this.value = value;
        this.doExists = ifExists;
    }

    public Account() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isDoExists() {
        return doExists;
    }

    public void setDoExists(boolean doExists) {
        this.doExists = doExists;
    }
}
