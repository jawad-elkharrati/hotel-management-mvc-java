package model;

public class GuestFactory {

    public static Guest createGuest(GuestType type, String name, String contact,double discountRate) {
        switch (type) {
            case VIP:
                return new VipGuest(name, contact,discountRate);
            case REGULAR:
                return new RegularGuest(name, contact);
            default:
                throw new IllegalArgumentException("Unknown guest type: " + type);
        }
    }

    public static Guest createVipGuest(String name, String contact, double discountRate) {
        return new VipGuest(name, contact, discountRate);
    }

    public static Guest createGuest(String typeString, String name, String contact,double discountRate) {
        try {
            GuestType type = GuestType.valueOf(typeString.toUpperCase());
            return createGuest(type, name, contact,discountRate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid guest type: " + typeString);
        }
    }

    // Add parameterless method for creating default/empty guests
    public static Guest createGuest() {
        return new RegularGuest(); // or create a default guest implementation
    }

    // Alternative: Create guest with just ID (useful for database operations)
    public static Guest createGuestWithId(int id) {
        Guest guest = new RegularGuest();
        guest.setId(id);
        return guest;
    }
}