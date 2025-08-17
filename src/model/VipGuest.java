package model;

public class VipGuest extends Guest {
    private double discountRate;

    public VipGuest() {
        super();
        this.discountRate = 0.15; // 15% default
    }

    public VipGuest(String name, String contact) {
        super(name, contact);
        this.discountRate = 0.15;
    }

    public VipGuest(String name, String contact, double discountRate) {
        super(name, contact);
        this.discountRate = discountRate;
    }

    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }

    @Override
    public double calculateDiscount(double amount) {
        return amount * discountRate;
    }

    @Override
    public String getGuestType() {
        return "VIP";
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", discountRate=" + (discountRate * 100) + "%}");
    }
}