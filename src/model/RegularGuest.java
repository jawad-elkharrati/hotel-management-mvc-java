package model;


public class RegularGuest extends Guest {

    public RegularGuest() {
        super();
    }

    public RegularGuest(String name, String contact) {
        super(name, contact);
    }

    @Override
    public double calculateDiscount(double amount) {
        return amount ;
    }

    @Override
    public String getGuestType() {
        return "REGULAR";
    }
}