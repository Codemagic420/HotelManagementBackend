package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.ExtraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CRUD tests for ExtraServiceRepository (hotel extras like spa, breakfast).
 * TYPE: integration test (real repository + in-memory H2 DB, no mocks).
 * HOW: @SpringBootTest loads the app; @Transactional rolls back after every test, so created rows never leak.
 * Seed data (cleaners, rooms, ...) comes from DataInitializer, which runs at startup.
 * BREAK IT: change an expected value, or make a seeded service have price 0 (TC-ES6 fails).
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ExtraServiceRepositoryTest")
class ExtraServiceRepositoryTest {
    @Autowired
    private ExtraServiceRepository extraServiceRepository;

    // Checks: seeded extra services exist.
    // Fails if: table is empty.
    @Test
    @DisplayName("TC-ES1: Find all extra services")
    void testFindAll() {
        List<ExtraService> services = extraServiceRepository.findAll();
        assertThat(services).isNotNull().isNotEmpty();
    }

    // Checks: CREATE - saved service gets an ID and keeps its name.
    // Fails if: expected name differs, or save() throws.
    @Test
    @DisplayName("TC-ES2: Save new extra service")
    void testSaveNewService() {
        ExtraService service = new ExtraService();
        service.setName("Unique Extra Service");
        service.setUnitPrice(new BigDecimal("50.00"));
        service.setPriceUnit("PER_NIGHT");
        service.setActive(true);

        ExtraService saved = extraServiceRepository.save(service);

        assertThat(saved).isNotNull();
        assertThat(saved.getExtraServiceId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Unique Extra Service");
    }

    // Checks: READ by ID.
    // Note: returns early (passes silently) if the table is empty.
    @Test
    @DisplayName("TC-ES3: Find service by ID")
    void testFindById() {
        List<ExtraService> services = extraServiceRepository.findAll();
        if (services.isEmpty()) return;
        Long serviceId = services.get(0).getExtraServiceId();

        Optional<ExtraService> found = extraServiceRepository.findById(serviceId);

        assertThat(found).isPresent();
        assertThat(found.get().getExtraServiceId()).isEqualTo(serviceId);
    }

    // Checks: UPDATE - unit price changes from 75.00 to 85.00.
    // isEqualByComparingTo compares BigDecimal by value (85.0 == 85.00).
    // Fails if: expected price is changed.
    @Test
    @DisplayName("TC-ES4: Update extra service")
    void testUpdateService() {
        ExtraService service = new ExtraService();
        service.setName("Update Test Service");
        service.setUnitPrice(new BigDecimal("75.00"));
        service.setPriceUnit("PER_DAY");
        service.setActive(true);
        service = extraServiceRepository.save(service);

        service.setUnitPrice(new BigDecimal("85.00"));
        ExtraService updated = extraServiceRepository.save(service);

        assertThat(updated.getUnitPrice()).isEqualByComparingTo(new BigDecimal("85.00"));
    }

    // Checks: DELETE - service is gone after deleteById.
    // Fails if: the deleteById line is removed.
    @Test
    @DisplayName("TC-ES5: Delete extra service")
    void testDeleteService() {
        ExtraService service = new ExtraService();
        service.setName("Delete Test Service");
        service.setUnitPrice(new BigDecimal("40.00"));
        service.setPriceUnit("PER_ITEM");
        service.setActive(true);
        service = extraServiceRepository.save(service);
        Long serviceId = service.getExtraServiceId();

        extraServiceRepository.deleteById(serviceId);

        Optional<ExtraService> deleted = extraServiceRepository.findById(serviceId);
        assertThat(deleted).isEmpty();
    }

    // Checks: every service has price > 0 and non-blank name/price unit.
    // Fails if: any seeded service has price 0 or a blank name.
    @Test
    @DisplayName("TC-ES6: Extra service has valid price")
    void testValidPrice() {
        List<ExtraService> services = extraServiceRepository.findAll();

        for (ExtraService service : services) {
            assertThat(service.getUnitPrice()).isGreaterThan(BigDecimal.ZERO);
            assertThat(service.getName()).isNotBlank();
            assertThat(service.getPriceUnit()).isNotBlank();
        }
    }
}
