package com.paradisehotel.config;

import com.paradisehotel.convertor.ReservationEntityToReservationResponseConverter;
import com.paradisehotel.convertor.ReservationRequestToClientEntityConverter;
import com.paradisehotel.convertor.ReservationRequestToReservationEntityConverter;
import com.paradisehotel.convertor.RoomEntityToReservableRoomResponseConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ConversionConfig {

    private Set<Converter> getConverers() {
        Set<Converter> converters = new HashSet<>();
        converters.add(new RoomEntityToReservableRoomResponseConverter());
        converters.add(new ReservationRequestToReservationEntityConverter());
        converters.add(new ReservationEntityToReservationResponseConverter());
        converters.add(new ReservationRequestToClientEntityConverter());

        return converters;
    }
    @Bean
    public ConversionService conversionService(){
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(getConverers());
        bean.afterPropertiesSet();
        return bean.getObject();
    }
}
