package com.paradisehotel.convertor;

import com.paradisehotel.entity.ClientEntity;
import com.paradisehotel.model.request.ReservationRequest;
import org.springframework.core.convert.converter.Converter;

public class ReservationRequestToClientEntityConverter implements Converter<ReservationRequest, ClientEntity> {
    @Override
    public ClientEntity convert(ReservationRequest source) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFirstName(source.getClientFirstName());
        clientEntity.setLastName(source.getClientLastName());
        clientEntity.setEmail(source.getClientEmail());
        clientEntity.setPhone(source.getClientPhone());
        return clientEntity;
    }
}
