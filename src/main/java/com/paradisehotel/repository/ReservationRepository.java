package com.paradisehotel.repository;

import com.paradisehotel.entity.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Long>{

}
