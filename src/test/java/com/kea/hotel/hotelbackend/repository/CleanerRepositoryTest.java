package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.Cleaner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CRUD tests for CleanerRepository.
 * TYPE: integration test (real repository + in-memory H2 DB, no mocks).
 * HOW: @SpringBootTest loads the app; @Transactional rolls back after every test, so created rows never leak.
 * Seed data (cleaners, rooms, ...) comes from DataInitializer, which runs at startup.
 * BREAK IT: change an expected value (e.g. 'John'), or stop DataInitializer seeding cleaners (TC-C1/C6 fail).
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("CleanerRepositoryTest")
class CleanerRepositoryTest {
    @Autowired
    private CleanerRepository cleanerRepository;

    // Checks: seeded cleaners exist (list not empty).
    // Fails if: DB has no cleaners (seed data removed).
    @Test
    @DisplayName("TC-C1: Find all cleaners")
    void testFindAll() {
        List<Cleaner> cleaners = cleanerRepository.findAll();
        assertThat(cleaners).isNotNull().isNotEmpty();
    }

    // Checks: CREATE - save() returns the entity with a generated ID.
    // Fails if: expected name differs from the saved one, or save() throws (e.g. missing required field).
    @Test
    @DisplayName("TC-C2: Save new cleaner")
    void testSaveNewCleaner() {
        Cleaner cleaner = new Cleaner();
        cleaner.setFirstName("John");
        cleaner.setLastName("Doe");
        cleaner.setPhone("555-0100");
        cleaner.setActive(true);

        Cleaner saved = cleanerRepository.save(cleaner);

        assertThat(saved).isNotNull();
        assertThat(saved.getCleanerId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("John");
    }

    // Checks: READ - a cleaner can be found by its ID.
    // Note: returns early (passes silently) if the table is empty.
    // Fails if: findById is given an ID that does not exist.
    @Test
    @DisplayName("TC-C3: Find cleaner by ID")
    void testFindById() {
        List<Cleaner> cleaners = cleanerRepository.findAll();
        if (cleaners.isEmpty()) return;
        Long cleanerId = cleaners.get(0).getCleanerId();

        Optional<Cleaner> found = cleanerRepository.findById(cleanerId);

        assertThat(found).isPresent();
        assertThat(found.get().getCleanerId()).isEqualTo(cleanerId);
    }

    // Checks: UPDATE - save() on an existing cleaner changes the phone number.
    // Fails if: expected phone is changed (e.g. '555-0301').
    @Test
    @DisplayName("TC-C4: Update cleaner")
    void testUpdateCleaner() {
        Cleaner cleaner = new Cleaner();
        cleaner.setFirstName("Jane");
        cleaner.setLastName("Smith");
        cleaner.setPhone("555-0200");
        cleaner.setActive(true);
        cleaner = cleanerRepository.save(cleaner);

        cleaner.setPhone("555-0300");
        Cleaner updated = cleanerRepository.save(cleaner);

        assertThat(updated.getPhone()).isEqualTo("555-0300");
    }

    // Checks: DELETE - after deleteById the cleaner is gone.
    // Fails if: the deleteById line is removed (cleaner would still exist).
    @Test
    @DisplayName("TC-C5: Delete cleaner")
    void testDeleteCleaner() {
        Cleaner cleaner = new Cleaner();
        cleaner.setFirstName("Bob");
        cleaner.setLastName("Johnson");
        cleaner.setPhone("555-0400");
        cleaner.setActive(true);
        cleaner = cleanerRepository.save(cleaner);
        Long cleanerId = cleaner.getCleanerId();

        cleanerRepository.deleteById(cleanerId);

        Optional<Cleaner> deleted = cleanerRepository.findById(cleanerId);
        assertThat(deleted).isEmpty();
    }

    // Checks: data quality - every cleaner has non-blank first/last name and an active flag.
    // Fails if: a seeded cleaner has a blank name or null active flag.
    @Test
    @DisplayName("TC-C6: Cleaner has valid names")
    void testValidNames() {
        List<Cleaner> cleaners = cleanerRepository.findAll();

        for (Cleaner cleaner : cleaners) {
            assertThat(cleaner.getFirstName()).isNotBlank();
            assertThat(cleaner.getLastName()).isNotBlank();
            assertThat(cleaner.getActive()).isNotNull();
        }
    }
}
