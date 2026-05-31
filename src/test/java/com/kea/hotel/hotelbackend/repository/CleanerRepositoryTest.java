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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("CleanerRepositoryTest")
class CleanerRepositoryTest {
    @Autowired
    private CleanerRepository cleanerRepository;

    @Test
    @DisplayName("TC-C1: Find all cleaners")
    void testFindAll() {
        List<Cleaner> cleaners = cleanerRepository.findAll();
        assertThat(cleaners).isNotNull().isNotEmpty();
    }

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
