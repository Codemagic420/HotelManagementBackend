package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.RoomCleaningTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCleaningTaskRepository extends JpaRepository<RoomCleaningTask, Long> {
    Page<RoomCleaningTask> findByTaskStatus(String taskStatus, Pageable pageable);
}
