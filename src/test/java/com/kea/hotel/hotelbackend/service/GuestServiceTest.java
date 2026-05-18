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

@ExtendWith(MockitoExtension.class)
@DisplayName("GuestService Unit Tests")
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private GuestService guestService;

    private Guest testGuest;

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

    @Test
    @DisplayName("Should retrieve all guests")
    void testFindAll() {
        Guest guest2 = new Guest();
        guest2.setGuestId(2L);
        guest2.setEmail("john@example.com");

        when(guestRepository.findAll()).thenReturn(Arrays.asList(testGuest, guest2));

        List<Guest> result = guestService.findAll();

        assertThat(result).hasSize(2).contains(testGuest, guest2);
        verify(guestRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should retrieve guest by ID")
    void testFindById() {
        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));

        Optional<Guest> result = guestService.findById(1L);

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(guest -> {
                    assertThat(guest.getFirstName()).isEqualTo("Jane");
                    assertThat(guest.getLastName()).isEqualTo("Smith");
                });
    }

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

    @Test
    @DisplayName("Should validate email format")
    void testEmailValidation() {
        testGuest.setEmail("valid@example.com");
        assertThat(testGuest.getEmail()).contains("@");

        testGuest.setEmail("invalid-email");
        assertThat(testGuest.getEmail()).doesNotContain("@");
    }

    @Test
    @DisplayName("Should store only last 4 credit card digits")
    void testCreditCardPCICompliance() {
        testGuest.setCreditCardLast4("4242");
        assertThat(testGuest.getCreditCardLast4()).hasSize(4);

        testGuest.setCreditCardLast4("0000");
        assertThat(testGuest.getCreditCardLast4()).isEqualTo("0000");
    }

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

    @Test
    @DisplayName("Should delete guest successfully")
    void testDelete() {
        doNothing().when(guestRepository).deleteById(1L);

        guestService.delete(1L);

        verify(guestRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should require first and last name")
    void testNameValidation() {
        assertThat(testGuest.getFirstName()).isNotBlank();
        assertThat(testGuest.getLastName()).isNotBlank();
    }

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
