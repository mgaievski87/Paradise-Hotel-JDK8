package com.paradisehotel.convertor;

import com.paradisehotel.entity.ReservationEntity;
import com.paradisehotel.model.response.ReservationResponse;
import org.springframework.core.convert.converter.Converter;

public class ReservationEntityToReservationResponseConverter implements Converter<ReservationEntity, ReservationResponse> {
    @Override
    public ReservationResponse convert(ReservationEntity source){
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setCheckin(source.getCheckin());
        reservationResponse.setCheckout(source.getCheckout());
        if(source.getRoomEntity() != null)
        reservationResponse.setId(source.getRoomEntity().getId());

        reservationResponse.setRoomNumber(source.getRoomEntity().getRoomNumber());

        return reservationResponse;
    }
}
