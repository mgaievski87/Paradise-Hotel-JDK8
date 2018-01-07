package com.paradisehotel.repository;


import com.paradisehotel.entity.RoomEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepository extends CrudRepository<RoomEntity, Long> {

    RoomEntity findById(Long id);

}
