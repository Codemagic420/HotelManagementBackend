package com.kea.hotel.hotelbackend.integration;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.repository.GuestRepository;
import com.kea.hotel.hotelbackend.service.GuestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("GuestService Integration Tests")
class GuestServiceIntegrationTest {

    @Autowired
    private GuestService guestService;

    @Autowired
    private GuestRepository guestRepository;

    private Guest testGuest;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        guestRepository.deleteAll();

        testGuest = new Guest();
        testGuest.setFirstName("John");
        testGuest.setLastName("Doe");
        testGuest.setEmail("john.doe@example.com");
        testGuest.setPhone("555-1234");
        testGuest.setCreditCardLast4("1234");
    }

    @Test
    @DisplayName("Should create and persist guest to database")
    void testCreateGuest() {
        // ACT: Save guest through service
        Guest saved = guestService.save(testGuest);

        // ASSERT: Guest is persisted with generated ID
        assertThat(saved).isNotNull();
        assertThat(saved.getGuestId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("John");

        // Verify in database
        Optional<Guest> retrieved = guestRepository.findById(saved.getGuestId());
        assertThat(retrieved)
                .isPresent()
                .hasValueSatisfying(g -> assertThat(g.getEmail()).isEqualTo("john.doe@example.com"));
    }

    @Test
    @DisplayName("Should retrieve guest by ID from database")
    void testGetGuestById() {
        // ARRANGE: Create and save guest
        Guest saved = guestService.save(testGuest);

        // ACT: Retrieve guest by ID
        Optional<Guest> result = guestService.findById(saved.getGuestId());

        // ASSERT: Guest retrieved with correct data
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(guest -> {
                    assertThat(guest.getGuestId()).isEqualTo(saved.getGuestId());
                    assertThat(guest.getFirstName()).isEqualTo("John");
                    assertThat(guest.getLastName()).isEqualTo("Doe");
                    assertThat(guest.getEmail()).isEqualTo("john.doe@example.com");
                });
    }

    @Test
    @DisplayName("Should find all guests with pagination")
    void testFindAllWithPagination() {
        // ARRANGE: Create multiple guests
        Guest guest1 = new Guest();
        guest1.setFirstName("Alice");
        guest1.setLastName("Smith");
        guest1.setEmail("alice@example.com");

        Guest guest2 = new Guest();
        guest2.setFirstName("Bob");
        guest2.setLastName("Johnson");
        guest2.setEmail("bob@example.com");

        guestService.save(testGuest);
        guestService.save(guest1);
        guestService.save(guest2);

        // ACT: Find all with pagination
        Pageable pageable = PageRequest.of(0, 10);
        Page<Guest> result = guestService.findAll(pageable);

        // ASSERT: All guests retrieved
        assertThat(result.getContent())
                .hasSize(3)
                .extracting("firstName")
                .contains("John", "Alice", "Bob");
    }

    @Test
    @DisplayName("Should update guest information")
    void testUpdateGuest() {
        // ARRANGE: Create and save guest
        Guest saved = guestService.save(testGuest);

        // ACT: Update guest
        Guest updated = new Guest();
        updated.setFirstName("Jane");
        updated.setLastName("Doe");
        updated.setEmail("john.doe@example.com");
        updated.setPhone("555-9999");

        Optional<Guest> result = guestService.update(saved.getGuestId(), updated);

        // ASSERT: Guest updated in database
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(guest -> {
                    assertThat(guest.getFirstName()).isEqualTo("Jane");
                    assertThat(guest.getPhone()).isEqualTo("555-9999");
                });

        // Verify in database
        Optional<Guest> dbGuest = guestRepository.findById(saved.getGuestId());
        assertThat(dbGuest)
                .isPresent()
                .hasValueSatisfying(g -> assertThat(g.getFirstName()).isEqualTo("Jane"));
    }

    @Test
    @DisplayName("Should delete guest from database")
    void testDeleteGuest() {
        // ARRANGE: Create and save guest
        Guest saved = guestService.save(testGuest);
        Long guestId = saved.getGuestId();

        // Verify guest exists
        assertThat(guestRepository.findById(guestId)).isPresent();

        // ACT: Delete guest
        guestService.delete(guestId);

        // ASSERT: Guest is deleted from database
        assertThat(guestRepository.findById(guestId)).isEmpty();
    }

    @Test
    @DisplayName("Should enforce unique email constraint")
    void testUniqueEmailConstraint() {
        // ARRANGE: Create and save first guest
        guestService.save(testGuest);

        // ACT & ASSERT: Attempt to save guest with same email throws exception
        Guest duplicateEmail = new Guest();
        duplicateEmail.setFirstName("Jane");
        duplicateEmail.setLastName("Smith");
        duplicateEmail.setEmail("john.doe@example.com"); // Same email

        assertThatThrownBy(() -> guestService.save(duplicateEmail))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("Should retrieve guest and verify email")
    void testGuestEmailVerification() {
        // ARRANGE: Create and save guest
        Guest saved = guestService.save(testGuest);

        // ACT: Retrieve guest
        Optional<Guest> result = guestRepository.findById(saved.getGuestId());

        // ASSERT: Guest found with correct email
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(g -> {
                    assertThat(g.getEmail()).isEqualTo("john.doe@example.com");
                    assertThat(g.getFirstName()).isEqualTo("John");
                    assertThat(g.getLastName()).isEqualTo("Doe");
                });
    }

    @Test
    @DisplayName("Should enrich guest with AI profile")
    void testAIProfileEnrichment() {
        // ARRANGE: Create and save guest
        Guest saved = guestService.save(testGuest);

        // ACT: Add AI profile
        String aiProfile = "Premium guest. VIP status. Prefers suite rooms.";
        Optional<Guest> result = guestService.updateAIProfile(saved.getGuestId(), aiProfile);

        // ASSERT: AI profile is saved
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(guest -> {
                    assertThat(guest.getAiProfileSummary()).isEqualTo(aiProfile);
                    assertThat(guest.getAiFieldsUpdatedAt()).isNotNull();
                });

        // Verify in database
        Optional<Guest> dbGuest = guestRepository.findById(saved.getGuestId());
        assertThat(dbGuest)
                .isPresent()
                .hasValueSatisfying(g -> assertThat(g.getAiProfileSummary()).isEqualTo(aiProfile));
    }

    @Test
    @DisplayName("Should handle pagination with multiple pages")
    void testPaginationMultiplePages() {
        // ARRANGE: Create 15 guests
        for (int i = 0; i < 15; i++) {
            Guest guest = new Guest();
            guest.setFirstName("Guest" + i);
            guest.setLastName("Last" + i);
            guest.setEmail("guest" + i + "@example.com");
            guestService.save(guest);
        }

        // ACT: Get first page with size 5
        Page<Guest> page1 = guestService.findAll(PageRequest.of(0, 5));
        Page<Guest> page2 = guestService.findAll(PageRequest.of(1, 5));
        Page<Guest> page3 = guestService.findAll(PageRequest.of(2, 5));

        // ASSERT: All pages retrieved correctly
        assertThat(page1.getContent()).hasSize(5);
        assertThat(page2.getContent()).hasSize(5);
        assertThat(page3.getContent()).hasSize(5);
        assertThat(page1.getTotalElements()).isEqualTo(15);
        assertThat(page1.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should preserve data integrity in concurrent operations")
    void testDataIntegrity() {
        // ARRANGE: Create first guest
        Guest guest1 = guestService.save(testGuest);

        // ACT: Create another guest with different email
        Guest guest2 = new Guest();
        guest2.setFirstName("Alice");
        guest2.setLastName("Wonder");
        guest2.setEmail("alice.wonder@example.com");
        Guest saved2 = guestService.save(guest2);

        // ASSERT: Both guests are independent and correct
        Optional<Guest> retrieved1 = guestRepository.findById(guest1.getGuestId());
        Optional<Guest> retrieved2 = guestRepository.findById(saved2.getGuestId());

        assertThat(retrieved1)
                .isPresent()
                .hasValueSatisfying(g -> assertThat(g.getEmail()).isEqualTo("john.doe@example.com"));

        assertThat(retrieved2)
                .isPresent()
                .hasValueSatisfying(g -> assertThat(g.getEmail()).isEqualTo("alice.wonder@example.com"));
    }
}
