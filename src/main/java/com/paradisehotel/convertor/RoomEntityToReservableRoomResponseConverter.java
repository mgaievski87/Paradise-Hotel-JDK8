package com.paradisehotel.convertor;

import com.paradisehotel.entity.ReservationEntity;
import com.paradisehotel.entity.RoomEntity;
import com.paradisehotel.model.Links;
import com.paradisehotel.model.Self;
import com.paradisehotel.model.response.ReservableRoomResponse;
import com.paradisehotel.rest.ResourceConstants;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public class RoomEntityToReservableRoomResponseConverter implements Converter<RoomEntity, ReservableRoomResponse> {
    private LocalDate checkin;
    private LocalDate checkout;

    public RoomEntityToReservableRoomResponseConverter() {
    }

    public RoomEntityToReservableRoomResponseConverter(LocalDate checkin, LocalDate checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }

    @Override
    public ReservableRoomResponse convert(RoomEntity source) {

        ReservableRoomResponse reservationResponse = new ReservableRoomResponse();
        reservationResponse.setRoomNumber(source.getRoomNumber());
        reservationResponse.setPrice(Integer.valueOf(source.getPrice()));

        Links links = new Links();
        Self self= new Self();
        self.setRef(ResourceConstants.ROOM_RESERVATION_V1 + "/" + source.getId());
        links.setSelf(self);
        reservationResponse.setLinks(links);

        reservationResponse.setId(source.getId());

        reservationResponse.setAvailable(true);
        //if empty constructor was called,
        // then 'isAvailable' field will be set to 'true' for all of the rooms
        if(this.checkin != null && this.checkout != null) {
            for (ReservationEntity reservationEntity : source.getReservationEntityList()) {
                if(!this.checkin.isBefore(reservationEntity.getCheckin()) && this.checkin.isBefore(reservationEntity.getCheckout())
                        || this.checkout.isAfter(reservationEntity.getCheckin()) && !this.checkout.isAfter(reservationEntity.getCheckout())
                        || this.checkin.isBefore(reservationEntity.getCheckin()) && this.checkout.isAfter(reservationEntity.getCheckout()))
                    reservationResponse.setAvailable(false);
            }
        }
        return reservationResponse;
    }
}
