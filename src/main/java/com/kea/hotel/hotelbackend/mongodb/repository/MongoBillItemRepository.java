package com.kea.hotel.hotelbackend.mongodb.repository;

import com.kea.hotel.hotelbackend.mongodb.document.MongoBillItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoBillItemRepository extends MongoRepository<MongoBillItem, String> {
    List<MongoBillItem> findByBillId(Long billId);
}
