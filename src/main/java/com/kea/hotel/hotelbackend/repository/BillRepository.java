package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
