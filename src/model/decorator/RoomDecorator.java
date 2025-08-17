package model.decorator;

import model.RoomComponent;

public abstract class RoomDecorator implements RoomComponent {
    protected RoomComponent room;
    protected double decorationPrice;

    public RoomDecorator(RoomComponent room, double decorationPrice) {
        this.room = room;
        this.decorationPrice = decorationPrice;
    }

    // Delegate basic room properties to the wrapped room
    @Override
    public String getNumber() {
        return room.getNumber();
    }

    @Override
    public String getType() {
        return room.getType();
    }

    @Override
    public String getStatus() {
        return room.getStatus();
    }

    @Override
    public int getId() {
        return room.getId();
    }

    @Override
    public boolean isAvailable() {
        return room.isAvailable();
    }

    @Override
    public boolean isOccupied() {
        return room.isOccupied();
    }

    // Common price calculation - adds decoration price to base room price
    @Override
    public double getPrice() {
        return room.getPrice() + decorationPrice;
    }

    // Description method - child classes will override if needed
    @Override
    public abstract String getDescription();

    // Getter for decoration price
    public double getDecorationPrice() {
        return decorationPrice;
    }
}
