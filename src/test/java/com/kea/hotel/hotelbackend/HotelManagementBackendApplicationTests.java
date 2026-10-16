package com.kea.hotel.hotelbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * SMOKE TEST: does the whole Spring app start?
 * HOW: @SpringBootTest boots the full context (H2 DB, security, all beans) with the 'test' profile.
 * BREAK IT: add a bean with a missing dependency, a bad property, or a broken @Entity -> context fails to load.
 */
@SpringBootTest
@ActiveProfiles("test")
class HotelManagementBackendApplicationTests {

    // Empty body on purpose: the test passes if startup succeeds, fails if startup throws.
    @Test
    void contextLoads() {
    }

}
