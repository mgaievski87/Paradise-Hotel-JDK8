package com.paradisehotel.model.response;


import com.paradisehotel.model.Links;

public class ReservableRoomResponse {

    private Long id;
    private Integer roomNumber;
    private Integer price;
    private Boolean isAvailable;
    private Links links;
    private String resMsg = "ok";

    public ReservableRoomResponse() {
    }

    public ReservableRoomResponse(Integer roomNumber, Integer price, Boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.isAvailable = isAvailable;
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

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }
}
