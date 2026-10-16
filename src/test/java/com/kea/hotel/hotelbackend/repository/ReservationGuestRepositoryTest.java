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

/**
 * Read-only tests for ReservationGuestRepository (link table: which guests belong to a reservation).
 * TYPE: integration test (real repository + in-memory H2 DB, no mocks).
 * HOW: @SpringBootTest loads the app; @Transactional rolls back after every test, so created rows never leak.
 * Seed data (cleaners, rooms, ...) comes from DataInitializer, which runs at startup.
 * BREAK IT: remove the seeded reservation-guest rows (TC-RG1 fails) or seed a row with a null flag/ID.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ReservationGuestRepositoryTest")
class ReservationGuestRepositoryTest {
    @Autowired
    private ReservationGuestRepository reservationGuestRepository;

    // Checks: seeded reservation-guest links exist.
    // Fails if: table is empty.
    @Test
    @DisplayName("TC-RG1: Find all reservation guests")
    void testFindAll() {
        List<ReservationGuest> guests = reservationGuestRepository.findAll();
        assertThat(guests).isNotNull().isNotEmpty();
    }

    // Checks: every row has isPrimary set (true/false, never null).
    // Fails if: a row has isPrimary = null.
    @Test
    @DisplayName("TC-RG2: Reservation guest has valid primary flag")
    void testValidPrimaryFlag() {
        List<ReservationGuest> guests = reservationGuestRepository.findAll();

        for (ReservationGuest guest : guests) {
            assertThat(guest.getIsPrimary()).isNotNull();
        }
    }

    // Checks: every row has an ID (composite key present).
    // Fails if: a row has a null ID.
    @Test
    @DisplayName("TC-RG3: Reservation guest has valid ID")
    void testValidId() {
        List<ReservationGuest> guests = reservationGuestRepository.findAll();

        for (ReservationGuest guest : guests) {
            assertThat(guest.getId()).isNotNull();
        }
    }
}
