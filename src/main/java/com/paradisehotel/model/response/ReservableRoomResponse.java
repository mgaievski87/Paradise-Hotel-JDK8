package com.paradisehotel.model.response;


import com.paradisehotel.entity.RoomType;
import com.paradisehotel.model.Links;

public class ReservableRoomResponse {

    private Long id;
    private Integer roomNumber;
    private Integer price;
    private Boolean isAvailable;
    private RoomType roomType;
    private String description;
    private Links links;
    private String resMsg = "ok";

    public ReservableRoomResponse() {
    }

    public ReservableRoomResponse(Integer roomNumber, Integer price, Boolean isAvailable, RoomType roomType, String description) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.isAvailable = isAvailable;
        this.roomType = roomType;
        this.description = description;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Long getId() {

        return id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public Integer getPrice() {
        return price;
    }

    public Links getLinks() {
        return links;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
