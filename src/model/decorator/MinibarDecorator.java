package model.decorator;

import model.RoomComponent;

public class MinibarDecorator extends RoomDecorator {

    public MinibarDecorator(RoomComponent room) {
        super(room, 25.0);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", Minibar";
    }
    public RoomComponent getWrappedRoom() {
        return room;
    }
}