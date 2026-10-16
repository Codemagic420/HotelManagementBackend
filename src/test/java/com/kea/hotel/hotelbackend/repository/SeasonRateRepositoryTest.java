package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.RoomType;
import com.kea.hotel.hotelbackend.model.SeasonRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CRUD tests for SeasonRateRepository (price per night per room type and season).
 * TYPE: integration test (real repository + in-memory H2 DB, no mocks).
 * HOW: @SpringBootTest loads the app; @Transactional rolls back after every test, so created rows never leak.
 * Seed data (cleaners, rooms, ...) comes from DataInitializer, which runs at startup.
 * @BeforeEach creates a RoomType first, because every SeasonRate needs a room type (foreign key).
 * BREAK IT: change an expected value, or set validFrom after validTo (TC-SR6 fails).
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("SeasonRateRepositoryTest")
class SeasonRateRepositoryTest {
    @Autowired
    private SeasonRateRepository seasonRateRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    private RoomType testRoomType;

    // Arrange: create a fresh RoomType that each test attaches its SeasonRate to.
    @BeforeEach
    void setUp() {
        testRoomType = new RoomType();
        testRoomType.setName("Test Room Type");
        testRoomType.setMaxOccupancy(2);
        testRoomType = roomTypeRepository.save(testRoomType);
    }

    // Checks: findAll() works and returns a list (not null).
    // WEAK: an empty list also passes, so this almost cannot fail.
    @Test
    @DisplayName("TC-SR1: Find all season rates")
    void testFindAll() {
        List<SeasonRate> rates = seasonRateRepository.findAll();
        assertThat(rates).isNotNull();
    }

    // Checks: CREATE - saved rate gets an ID, season and price.
    // Fails if: expected season/price differ, or save() throws.
    @Test
    @DisplayName("TC-SR2: Save new season rate")
    void testSaveNewRate() {
        SeasonRate newRate = new SeasonRate();
        newRate.setRoomType(testRoomType);
        newRate.setSeason("HIGH");
        newRate.setPricePerNight(new BigDecimal("250.00"));
        newRate.setValidFrom(LocalDate.of(2026, 6, 1));
        newRate.setValidTo(LocalDate.of(2026, 8, 31));

        SeasonRate saved = seasonRateRepository.save(newRate);

        assertThat(saved).isNotNull();
        assertThat(saved.getRateId()).isNotNull();
        assertThat(saved.getSeason()).isEqualTo("HIGH");
        assertThat(saved.getPricePerNight()).isEqualByComparingTo(new BigDecimal("250.00"));
    }

    // Checks: READ - save a rate, then find it by its ID.
    // Fails if: expected season ('LOW') differs from the saved one.
    @Test
    @DisplayName("TC-SR3: Find rate by ID")
    void testFindById() {
        SeasonRate rate = new SeasonRate();
        rate.setRoomType(testRoomType);
        rate.setSeason("LOW");
        rate.setPricePerNight(new BigDecimal("150.00"));
        rate.setValidFrom(LocalDate.of(2026, 1, 1));
        rate.setValidTo(LocalDate.of(2026, 3, 31));
        rate = seasonRateRepository.save(rate);

        Optional<SeasonRate> found = seasonRateRepository.findById(rate.getRateId());

        assertThat(found).isPresent();
        assertThat(found.get().getSeason()).isEqualTo("LOW");
    }

    // Checks: UPDATE - price changes from 300.00 to 350.00.
    // Fails if: expected price is changed.
    @Test
    @DisplayName("TC-SR4: Update season rate")
    void testUpdateRate() {
        SeasonRate rate = new SeasonRate();
        rate.setRoomType(testRoomType);
        rate.setSeason("PEAK");
        rate.setPricePerNight(new BigDecimal("300.00"));
        rate.setValidFrom(LocalDate.of(2026, 7, 1));
        rate.setValidTo(LocalDate.of(2026, 7, 31));
        rate = seasonRateRepository.save(rate);

        rate.setPricePerNight(new BigDecimal("350.00"));
        SeasonRate updated = seasonRateRepository.save(rate);

        assertThat(updated.getPricePerNight()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    // Checks: DELETE - rate is gone after deleteById.
    // Fails if: the deleteById line is removed.
    @Test
    @DisplayName("TC-SR5: Delete season rate")
    void testDeleteRate() {
        SeasonRate rate = new SeasonRate();
        rate.setRoomType(testRoomType);
        rate.setSeason("OFF");
        rate.setPricePerNight(new BigDecimal("100.00"));
        rate.setValidFrom(LocalDate.of(2026, 12, 1));
        rate.setValidTo(LocalDate.of(2026, 12, 31));
        rate = seasonRateRepository.save(rate);
        Long rateId = rate.getRateId();

        seasonRateRepository.deleteById(rateId);

        Optional<SeasonRate> deleted = seasonRateRepository.findById(rateId);
        assertThat(deleted).isEmpty();
    }

    // Checks: business rule - price > 0 and validFrom is before validTo.
    // Fails if: price is 0/negative, or the dates are swapped.
    // Note: asserts on the object the test built itself, so it checks the test data, not a DB rule.
    @Test
    @DisplayName("TC-SR6: Season rate has valid price")
    void testValidPrice() {
        SeasonRate rate = new SeasonRate();
        rate.setRoomType(testRoomType);
        rate.setSeason("MID");
        rate.setPricePerNight(new BigDecimal("200.00"));
        rate.setValidFrom(LocalDate.of(2026, 4, 1));
        rate.setValidTo(LocalDate.of(2026, 5, 31));
        rate = seasonRateRepository.save(rate);

        assertThat(rate.getPricePerNight()).isGreaterThan(BigDecimal.ZERO);
        assertThat(rate.getValidFrom()).isBefore(rate.getValidTo());
    }
}
