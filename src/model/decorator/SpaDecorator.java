package model.decorator;

import model.RoomComponent;

public class SpaDecorator extends RoomDecorator {

    public SpaDecorator(RoomComponent room) {
        super(room, 50.0);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", Spa Access";
    }
    public RoomComponent getWrappedRoom() {
        return room;
    }
}