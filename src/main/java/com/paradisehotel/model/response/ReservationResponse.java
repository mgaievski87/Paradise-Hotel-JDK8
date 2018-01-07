package com.paradisehotel.model.response;

import java.time.LocalDate;

public class ReservationResponse {

    private Long id;
    private Integer roomNumber;
    private LocalDate checkin;
    private LocalDate checkout;
    private String resMsg;


    public ReservationResponse() {
    }

    public ReservationResponse(Long id, LocalDate checkin, LocalDate checkout, Integer roomNumber) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }
}
