package com.kea.hotel.hotelbackend.mongodb.repository;

import com.kea.hotel.hotelbackend.mongodb.document.AiInteraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AiInteractionRepository extends MongoRepository<AiInteraction, String> {
    Page<AiInteraction> findByBotType(String botType, Pageable pageable);
}
