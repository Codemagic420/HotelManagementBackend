package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.repository.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests of GuestService.
 * TYPE: pure UNIT test. No Spring, no DB: the repository is a Mockito @Mock, the service is @InjectMocks.
 * PATTERN: Arrange (when(...).thenReturn) -> Act (call service) -> Assert (AssertJ) + verify() calls on the mock.
 * MockitoExtension is strict: a stub that is never used fails the test (UnnecessaryStubbingException).
 * BREAK IT: change a stubbed return value or expected value, or make the service skip the repository call (verify fails).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GuestService Unit Tests")
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private GuestService guestService;

    private Guest testGuest;

    // Arrange: build a sample guest (Jane Smith) reused by every test.
    @BeforeEach
    void setUp() {
        testGuest = new Guest();
        testGuest.setGuestId(1L);
        testGuest.setFirstName("Jane");
        testGuest.setLastName("Smith");
        testGuest.setEmail("jane@example.com");
        testGuest.setPhone("555-1234");
        testGuest.setCreditCardLast4("4242");
    }

    // Checks: service returns what the repository returns (2 guests) and calls findAll() exactly once.
    // Fails if: service doesn't call the repository, or calls it twice (times(1)).
    @Test
    @DisplayName("Should retrieve all guests")
    void testFindAll() {
        // ARRANGE: Setup test data with mock repository
        Guest guest2 = new Guest();
        guest2.setGuestId(2L);
        guest2.setEmail("john@example.com");
        when(guestRepository.findAll()).thenReturn(Arrays.asList(testGuest, guest2));

        // ACT: Call service method
        List<Guest> result = guestService.findAll();

        // ASSERT: Verify results and mock interactions
        assertThat(result).hasSize(2).contains(testGuest, guest2);
        verify(guestRepository, times(1)).findAll();
    }

    // Checks: found guest comes back with the right first/last name.
    // Fails if: service looks up the wrong ID (stub is for ID 1) or returns empty.
    @Test
    @DisplayName("Should retrieve guest by ID")
    void testFindById() {
        // ARRANGE: Setup mock to return guest
        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));

        // ACT: Call service
        Optional<Guest> result = guestService.findById(1L);

        // ASSERT: Verify guest is present with correct data
        assertThat(result)
                .isPresent()
                .hasValueSatisfying(guest -> {
                    assertThat(guest.getFirstName()).isEqualTo("Jane");
                    assertThat(guest.getLastName()).isEqualTo("Smith");
                });
    }

    // Checks: saved guest keeps email and phone.
    // Fails if: service doesn't pass the guest to repository.save().
    @Test
    @DisplayName("Should save guest successfully")
    void testSave() {
        when(guestRepository.save(testGuest)).thenReturn(testGuest);

        Guest result = guestService.save(testGuest);

        assertThat(result)
                .isNotNull()
                .extracting("email", "phone")
                .containsExactly("jane@example.com", "555-1234");
    }

    // WEAK: tests the Guest setter/getter and a '@' check in the test itself.
    // It does NOT test any service validation, so it passes whatever the service does.
    @Test
    @DisplayName("Should validate email format")
    void testEmailValidation() {
        testGuest.setEmail("valid@example.com");
        assertThat(testGuest.getEmail()).contains("@");

        testGuest.setEmail("invalid-email");
        assertThat(testGuest.getEmail()).doesNotContain("@");
    }

    // WEAK: checks the model stores exactly 4 digits (only 'last 4' of the card, PCI idea).
    // Does not stop a longer number from being set; fails only if the getter/setter breaks.
    @Test
    @DisplayName("Should store only last 4 credit card digits")
    void testCreditCardPCICompliance() {
        testGuest.setCreditCardLast4("4242");
        assertThat(testGuest.getCreditCardLast4()).hasSize(4);

        testGuest.setCreditCardLast4("0000");
        assertThat(testGuest.getCreditCardLast4()).isEqualTo("0000");
    }

    // Checks: update(1, newData) finds the guest, saves, and returns the updated name/phone.
    // Note: the mocked save() returns updatedGuest, so the result mostly reflects the stub.
    // Fails if: the service doesn't call findById/save (strict stubs complain).
    @Test
    @DisplayName("Should update guest information")
    void testUpdateGuest() {
        Guest updatedGuest = new Guest();
        updatedGuest.setGuestId(1L);
        updatedGuest.setFirstName("Jane Marie");
        updatedGuest.setPhone("555-5678");

        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));
        when(guestRepository.save(any(Guest.class))).thenReturn(updatedGuest);

        Optional<Guest> result = guestService.update(1L, updatedGuest);

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(guest -> {
                    assertThat(guest.getFirstName()).isEqualTo("Jane Marie");
                    assertThat(guest.getPhone()).isEqualTo("555-5678");
                });
    }

    // Checks: delete(1) calls repository.deleteById(1) exactly once.
    // Fails if: the service doesn't delete or uses another ID.
    @Test
    @DisplayName("Should delete guest successfully")
    void testDelete() {
        doNothing().when(guestRepository).deleteById(1L);

        guestService.delete(1L);

        verify(guestRepository, times(1)).deleteById(1L);
    }

    // WEAK: only checks the test's own sample data has names; no service code involved.
    @Test
    @DisplayName("Should require first and last name")
    void testNameValidation() {
        assertThat(testGuest.getFirstName()).isNotBlank();
        assertThat(testGuest.getLastName()).isNotBlank();
    }

    // Checks: a Guest can be created without phone (phone is null).
    // Model test only; fails if the Guest constructor sets a default phone.
    @Test
    @DisplayName("Should handle optional phone number")
    void testPhoneNumberOptional() {
        Guest guestNoPhone = new Guest();
        guestNoPhone.setFirstName("Bob");
        guestNoPhone.setLastName("Jones");
        guestNoPhone.setEmail("bob@example.com");

        // Phone is nullable
        assertThat(guestNoPhone.getPhone()).isNull();
    }
}
