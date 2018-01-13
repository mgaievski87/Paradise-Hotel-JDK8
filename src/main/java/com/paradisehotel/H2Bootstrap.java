package com.paradisehotel;

import com.paradisehotel.entity.RoomEntity;
import com.paradisehotel.entity.RoomType;
import com.paradisehotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class H2Bootstrap implements CommandLineRunner {

    @Autowired
    RoomRepository roomRepository;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Bootstrapping data: ");

        roomRepository.save(new RoomEntity(405, "200", RoomType.TWIN));
        roomRepository.save(new RoomEntity(406, "150", RoomType.SINGLE));
        roomRepository.save(new RoomEntity(407, "180", RoomType.DOUBLE));
        roomRepository.save(new RoomEntity(408, "180", RoomType.DOUBLE));
        roomRepository.save(new RoomEntity(409, "180", RoomType.DOUBLE));
        roomRepository.save(new RoomEntity(410, "150", RoomType.SINGLE));
        roomRepository.save(new RoomEntity(411, "300", RoomType.PRESIDENTIAL));
        roomRepository.save(new RoomEntity(412, "250", RoomType.FAMILY));
        roomRepository.save(new RoomEntity(413, "250", RoomType.FAMILY));
        roomRepository.save(new RoomEntity(414, "250", RoomType.FAMILY));
        roomRepository.save(new RoomEntity(415, "150", RoomType.SINGLE));
        roomRepository.save(new RoomEntity(416, "180", RoomType.DOUBLE));
        roomRepository.save(new RoomEntity(417, "180", RoomType.DOUBLE));
        roomRepository.save(new RoomEntity(418, "200", RoomType.TWIN));
        roomRepository.save(new RoomEntity(419, "200", RoomType.TWIN));

        Iterable<RoomEntity> itr = roomRepository.findAll();

        System.out.println("Printing out data: ");

        for(RoomEntity room: itr){
            System.out.println(room.getRoomNumber());
        }
    }
}
