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

    @BeforeEach
    void setUp() {
        testRoomType = new RoomType();
        testRoomType.setName("Test Room Type");
        testRoomType.setMaxOccupancy(2);
        testRoomType = roomTypeRepository.save(testRoomType);
    }

    @Test
    @DisplayName("TC-SR1: Find all season rates")
    void testFindAll() {
        List<SeasonRate> rates = seasonRateRepository.findAll();
        assertThat(rates).isNotNull();
    }

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
