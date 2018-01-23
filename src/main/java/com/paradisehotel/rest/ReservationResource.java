package com.paradisehotel.rest;

import com.paradisehotel.convertor.Filters;
import com.paradisehotel.convertor.RoomEntityListPage;
import com.paradisehotel.convertor.RoomEntityToReservableRoomResponseConverter;
import com.paradisehotel.entity.ClientEntity;
import com.paradisehotel.entity.ReservationEntity;
import com.paradisehotel.entity.RoomEntity;
import com.paradisehotel.model.request.ReservationRequest;
import com.paradisehotel.model.response.ReservableRoomResponse;
import com.paradisehotel.model.response.ReservationResponse;
import com.paradisehotel.repository.ClientRepository;
import com.paradisehotel.repository.PageableRoomRepository;
import com.paradisehotel.repository.ReservationRepository;
import com.paradisehotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;

import static com.paradisehotel.rest.ResourceConstants.ROOM_RESERVATION_V1;

@RestController
@RequestMapping(ROOM_RESERVATION_V1)
@CrossOrigin
public class ReservationResource {

    @Autowired
    PageableRoomRepository pageableRoomRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ConversionService conversionService;


    @RequestMapping(path="", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<ReservableRoomResponse> getAvailableRooms(
            @RequestParam(value = "checkin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkin,
            @RequestParam(value = "checkout")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkout,
            String showParam,
            Pageable pageable){
        //----Validation----
        if(checkin == null || checkout == null){
            ReservableRoomResponse reservableRoomResponse = new ReservableRoomResponse();
            reservableRoomResponse.setResMsg("Please make sure that you specify check-in and check-out dates correctly.");
            return new PageImpl<>(Arrays.asList(reservableRoomResponse));
        }
        if(!checkin.isBefore(checkout)){
            ReservableRoomResponse reservableRoomResponse = new ReservableRoomResponse();
            reservableRoomResponse.setResMsg("Checkout date is supposed to be after checking date. PLease correct the dates.");
            return new PageImpl<>(Arrays.asList(reservableRoomResponse));
        }
        //----Validation End----

        //Return available and unavailable rooms
        if(showParam.toLowerCase().equals("all")) {
            Page<RoomEntity> roomEntityList = pageableRoomRepository.findAll(pageable);
            return roomEntityList.map(new RoomEntityToReservableRoomResponseConverter(checkin, checkout));
        }

        //Filtering out unavailable rooms and returning only available ones.
        List<RoomEntity> filteredRoomEntityList = Filters.getAvailableRoomList(roomRepository, checkin, checkout);

        if(pageable == null)
            return new PageImpl<>(filteredRoomEntityList).map(new RoomEntityToReservableRoomResponseConverter());

        return new RoomEntityListPage(filteredRoomEntityList, pageable).getPage().map(new RoomEntityToReservableRoomResponseConverter());
    }

    @RequestMapping(path = "/{roomId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RoomEntity> getRoomById(
            @PathVariable
            Long roomId){

                RoomEntity roomEntity = roomRepository.findById(roomId);

                return new ResponseEntity<RoomEntity>(roomEntity, HttpStatus.OK);
    }

    @RequestMapping(path="", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ReservationResponse> createReservation(
                    @RequestBody
                    ReservationRequest reservationRequest){

        ReservationResponse reservationResponse;
        RoomEntity roomEntity = roomRepository.findById(reservationRequest.getRoomId());
        //Validation
        if(roomEntity == null) {
            reservationResponse = new ReservationResponse();
            reservationResponse.setResMsg("Invalid RoomId");
            return new ResponseEntity<ReservationResponse>(reservationResponse, HttpStatus.BAD_REQUEST);
        }
        if(!reservationRequest.getCheckin().isBefore(reservationRequest.getCheckout())){
            reservationResponse = new ReservationResponse();
            reservationResponse.setResMsg("Checkout date is supposed to be after checking date. Please correct the dates.");
            return new ResponseEntity<ReservationResponse>(reservationResponse, HttpStatus.OK);
        }
        for(ReservationEntity existingReservation : roomEntity.getReservationEntityList())
            if(!reservationRequest.getCheckin().isBefore(existingReservation.getCheckin())
                    && reservationRequest.getCheckin().isBefore(existingReservation.getCheckout())
                    || reservationRequest.getCheckout().isAfter(existingReservation.getCheckin())
                    && !reservationRequest.getCheckout().isAfter(existingReservation.getCheckout())
                    || reservationRequest.getCheckin().isBefore(existingReservation.getCheckin())
                    && reservationRequest.getCheckout().isAfter(reservationRequest.getCheckout())){
                reservationResponse = new ReservationResponse();
                reservationResponse.setRoomNumber(roomEntity.getRoomNumber());
                reservationResponse.setResMsg("Room #"+roomEntity.getRoomNumber()+" is not available for this period");
                return new ResponseEntity<ReservationResponse>(reservationResponse, HttpStatus.OK);
            }
        //End - Validation

        ReservationEntity reservationEntity = conversionService.convert(reservationRequest, ReservationEntity.class);
        reservationRepository.save(reservationEntity);

        roomEntity.addReservationEntity(reservationEntity);
        roomRepository.save(roomEntity);

        ClientEntity clientEntity = conversionService.convert(reservationRequest, ClientEntity.class);
        clientEntity.addReservationEntity(reservationEntity);
        clientRepository.save(clientEntity);

        reservationEntity.setRoomEntity(roomEntity);
        reservationEntity.setClientEntity(clientEntity);

        reservationResponse = conversionService.convert(reservationEntity, ReservationResponse.class);
        //reservationResponse.setRoomNumber(roomEntity.getRoomNumber());
        reservationResponse.setResMsg("Room #"+roomEntity.getRoomNumber()+" is booked for you");
        return new ResponseEntity<>(reservationResponse, HttpStatus.CREATED);
    }

    @RequestMapping(path="", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ReservableRoomResponse> updateReservation(
                    @RequestBody
                    ReservationRequest reservationRequest){

        return new ResponseEntity<>(new ReservableRoomResponse(), HttpStatus.OK);
    }

    @RequestMapping(path = "/{reservationId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteReservation(
            @PathVariable
            long reservationId){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
