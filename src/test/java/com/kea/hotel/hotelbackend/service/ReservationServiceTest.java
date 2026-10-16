package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.*;
import com.kea.hotel.hotelbackend.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests of ReservationService.
 * TYPE: pure UNIT test. No Spring, no DB: the repository is a Mockito @Mock, the service is @InjectMocks.
 * PATTERN: Arrange (when(...).thenReturn) -> Act (call service) -> Assert (AssertJ) + verify() calls on the mock.
 * MockitoExtension is strict: a stub that is never used fails the test (UnnecessaryStubbingException).
 * BREAK IT: change a stubbed return value or expected value, or make the service skip the repository call (verify fails).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationService Unit Tests")
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation testReservation;
    private RoomType testRoomType;
    private Guest testGuest;

    // Arrange: build a Suite room type (max 4 guests), a guest, and reservation RES001 (5 nights, 150/night, CONFIRMED).
    @BeforeEach
    void setUp() {
        testRoomType = new RoomType();
        testRoomType.setRoomTypeId(1L);
        testRoomType.setName("Suite");
        testRoomType.setMaxOccupancy(4);

        testGuest = new Guest();
        testGuest.setGuestId(1L);
        testGuest.setFirstName("John");
        testGuest.setLastName("Doe");
        testGuest.setEmail("john@example.com");

        testReservation = new Reservation();
        testReservation.setReservationId(1L);
        testReservation.setReferenceNo("RES001");
        testReservation.setCheckInDate(LocalDate.of(2024, 5, 20));
        testReservation.setCheckOutDate(LocalDate.of(2024, 5, 25));
        testReservation.setNights(5);
        testReservation.setNumGuests(2);
        testReservation.setRoomType(testRoomType);
        testReservation.setGuest(testGuest);
        testReservation.setBookedNightlyPrice(new BigDecimal("150.00"));
        testReservation.setStatus("CONFIRMED");
    }

    // Checks: returns both reservations and calls findAll() once.
    // Fails if: service doesn't use the repository.
    @Test
    @DisplayName("Should retrieve all reservations")
    void testFindAll() {
        Reservation res2 = new Reservation();
        res2.setReservationId(2L);
        res2.setReferenceNo("RES002");

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(testReservation, res2));

        List<Reservation> result = reservationService.findAll();

        assertThat(result).hasSize(2).contains(testReservation, res2);
        verify(reservationRepository, times(1)).findAll();
    }

    // Checks: reservation 1 comes back with reference RES001 and status CONFIRMED.
    // Fails if: stubbed data or expected values differ.
    @Test
    @DisplayName("Should retrieve reservation by ID")
    void testFindById() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        Optional<Reservation> result = reservationService.findById(1L);

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(res -> {
                    assertThat(res.getReferenceNo()).isEqualTo("RES001");
                    assertThat(res.getStatus()).isEqualTo("CONFIRMED");
                });
    }

    // Checks: saved reservation keeps reference, number of guests and nights.
    // Fails if: service doesn't pass it to repository.save() or expected values change.
    @Test
    @DisplayName("Should save new reservation successfully")
    void testSave() {
        when(reservationRepository.save(testReservation)).thenReturn(testReservation);

        Reservation result = reservationService.save(testReservation);

        assertThat(result)
                .isNotNull()
                .extracting("referenceNo", "numGuests", "nights")
                .containsExactly("RES001", 2, 5);
    }

    // Boundary values: 1, 4 and 7 nights (shortest stay, normal, a week).
    // WEAK: nights are set by the test itself, so the service calculation is not tested.
    // Fails if: check-out is not after check-in or expected nights differ.
    @ParameterizedTest
    @CsvSource({
            "2024-06-01,2024-06-02,1",
            "2024-06-01,2024-06-05,4",
            "2026-01-01,2026-01-08,7"
    })
    @DisplayName("Should calculate correct number of nights for boundary date ranges")
    void testNightCalculation(LocalDate checkIn, LocalDate checkOut, int expectedNights) {
        Reservation res = new Reservation();
        res.setCheckInDate(checkIn);
        res.setCheckOutDate(checkOut);
        res.setNights(expectedNights);

        assertThat(res.getCheckOutDate()).isAfter(res.getCheckInDate());
        assertThat(res.getNights()).isEqualTo(expectedNights);
    }

    // WEAK: loops over valid statuses and checks the setter stores them.
    // Does not check that invalid statuses are rejected.
    @Test
    @DisplayName("Should validate reservation status values")
    void testReservationStatusValidation() {
        String[] validStatuses = {"PENDING", "CONFIRMED", "CANCELLED", "CHECKED_IN", "CHECKED_OUT"};

        for (String status : validStatuses) {
            testReservation.setStatus(status);
            assertThat(testReservation.getStatus()).isEqualTo(status);
        }
    }

    // State change: update(1, ...) moves CONFIRMED -> CHECKED_IN.
    // Note: mocked save() returns updatedRes, so the result mostly reflects the stub.
    // Fails if: service doesn't call findById/save.
    @Test
    @DisplayName("Should update reservation status")
    void testUpdateReservationStatus() {
        Reservation updatedRes = new Reservation();
        updatedRes.setReservationId(1L);
        updatedRes.setReferenceNo("RES001");
        updatedRes.setStatus("CHECKED_IN");

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedRes);

        Optional<Reservation> result = reservationService.update(1L, updatedRes);

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(res -> assertThat(res.getStatus()).isEqualTo("CHECKED_IN"));
    }

    // Boundary: 4 guests = max allowed (ok), 5 guests = over the limit.
    // WEAK: the test compares numbers itself; the service doesn't enforce the rule here.
    @Test
    @DisplayName("Should validate room occupancy doesn't exceed max")
    void testOccupancyValidation() {
        testReservation.setNumGuests(4); // Should match or be less than roomType maxOccupancy
        assertThat(testReservation.getNumGuests()).isLessThanOrEqualTo(testRoomType.getMaxOccupancy());

        // Invalid case
        testReservation.setNumGuests(5);
        assertThat(testReservation.getNumGuests()).isGreaterThan(testRoomType.getMaxOccupancy());
    }

    // Checks: total = nightly price x nights (150.00 x 5 = 750.00).
    // WEAK: calculation is done inside the test, not by a service method.
    @Test
    @DisplayName("Should calculate total reservation price correctly")
    void testTotalReservationPrice() {
        BigDecimal nightlyRate = new BigDecimal("150.00");
        int nights = 5;
        BigDecimal expectedTotal = nightlyRate.multiply(BigDecimal.valueOf(nights));

        testReservation.setBookedNightlyPrice(nightlyRate);
        testReservation.setNights(nights);

        BigDecimal actualTotal = testReservation.getBookedNightlyPrice()
                .multiply(BigDecimal.valueOf(testReservation.getNights()));

        assertThat(actualTotal).isEqualByComparingTo(expectedTotal);
    }

    // Checks: dates set to tomorrow..+5 days are in the future and check-out is after check-in.
    // WEAK: only checks the test's own dates; no service rule is tested.
    @Test
    @DisplayName("Should handle future date validation")
    void testFutureDateValidation() {
        LocalDate today = LocalDate.now();
        testReservation.setCheckInDate(today.plusDays(1));
        testReservation.setCheckOutDate(today.plusDays(5));

        assertThat(testReservation.getCheckInDate()).isAfter(today);
        assertThat(testReservation.getCheckOutDate()).isAfter(testReservation.getCheckInDate());
    }

   // Checks: delete(1) calls repository.deleteById(1) exactly once.
   // Fails if: the service doesn't delete or uses another ID.
   @Test
    @DisplayName("Should delete reservation successfully")
    void testDelete() {
    // Arrange
    doNothing().when(reservationRepository).deleteById(1L);

    // Act
    reservationService.delete(1L);

    // Assert
    verify(reservationRepository, times(1)).deleteById(1L);
}
}
