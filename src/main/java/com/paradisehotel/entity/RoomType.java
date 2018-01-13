package com.paradisehotel.entity;

public enum RoomType {
    SINGLE("A room which has single bed facility."),
    DOUBLE("A room which has double bed facility."),
    TWIN("A room which has two single bed separated by a center table."),
    FAMILY("Two rooms which shares a common door, mostly used by families."),
    PRESIDENTIAL("Presidential Suite features two lavish bedrooms with king-sized beds, a marble bathroom with a Jacuzzi, expansive areas for living and dining.");

    private final String description;

    RoomType(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
