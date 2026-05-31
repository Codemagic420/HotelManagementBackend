package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.ReservationGuest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ReservationGuestRepositoryTest")
class ReservationGuestRepositoryTest {
    @Autowired
    private ReservationGuestRepository reservationGuestRepository;

    @Test
    @DisplayName("TC-RG1: Find all reservation guests")
    void testFindAll() {
        List<ReservationGuest> guests = reservationGuestRepository.findAll();
        assertThat(guests).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("TC-RG2: Reservation guest has valid primary flag")
    void testValidPrimaryFlag() {
        List<ReservationGuest> guests = reservationGuestRepository.findAll();

        for (ReservationGuest guest : guests) {
            assertThat(guest.getIsPrimary()).isNotNull();
        }
    }

    @Test
    @DisplayName("TC-RG3: Reservation guest has valid ID")
    void testValidId() {
        List<ReservationGuest> guests = reservationGuestRepository.findAll();

        for (ReservationGuest guest : guests) {
            assertThat(guest.getId()).isNotNull();
        }
    }
}
