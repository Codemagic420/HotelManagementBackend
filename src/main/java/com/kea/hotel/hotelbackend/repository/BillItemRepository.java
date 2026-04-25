package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {
}
