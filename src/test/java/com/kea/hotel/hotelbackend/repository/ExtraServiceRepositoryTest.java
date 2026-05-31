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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("ExtraServiceRepositoryTest")
class ExtraServiceRepositoryTest {
    @Autowired
    private ExtraServiceRepository extraServiceRepository;

    @Test
    @DisplayName("TC-ES1: Find all extra services")
    void testFindAll() {
        List<ExtraService> services = extraServiceRepository.findAll();
        assertThat(services).isNotNull().isNotEmpty();
    }

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
