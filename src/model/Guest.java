package model;

public abstract class Guest {
    protected int id;
    protected String name;
    protected String contact;

    public Guest() {}

    public Guest(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    // Abstract methods
    public abstract double calculateDiscount(double amount);
    public abstract String getGuestType();


    @Override
    public String toString() {
        return getGuestType() + "{id=" + id + ", name='" + name + "', contact='" + contact + "'}";
    }
}