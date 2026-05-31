package com.kea.hotel.hotelbackend.repository;

import com.kea.hotel.hotelbackend.model.InventoryItem;
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
@DisplayName("InventoryItemRepositoryTest")
class InventoryItemRepositoryTest {
    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Test
    @DisplayName("TC-II1: Find all inventory items")
    void testFindAll() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        assertThat(items).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("TC-II2: Save new inventory item")
    void testSaveNewItem() {
        InventoryItem item = new InventoryItem();
        item.setName("Unique Item");
        item.setUnitPrice(new BigDecimal("50.00"));
        item.setActive(true);

        InventoryItem saved = inventoryItemRepository.save(item);

        assertThat(saved).isNotNull();
        assertThat(saved.getInventoryItemId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Unique Item");
    }

    @Test
    @DisplayName("TC-II3: Find item by ID")
    void testFindById() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        if (items.isEmpty()) return;
        Long itemId = items.get(0).getInventoryItemId();

        Optional<InventoryItem> found = inventoryItemRepository.findById(itemId);

        assertThat(found).isPresent();
        assertThat(found.get().getInventoryItemId()).isEqualTo(itemId);
    }

    @Test
    @DisplayName("TC-II4: Update inventory item")
    void testUpdateItem() {
        InventoryItem item = new InventoryItem();
        item.setName("Update Test Item");
        item.setUnitPrice(new BigDecimal("100.00"));
        item.setActive(true);
        item = inventoryItemRepository.save(item);

        item.setUnitPrice(new BigDecimal("120.00"));
        InventoryItem updated = inventoryItemRepository.save(item);

        assertThat(updated.getUnitPrice()).isEqualByComparingTo(new BigDecimal("120.00"));
    }

    @Test
    @DisplayName("TC-II5: Delete inventory item")
    void testDeleteItem() {
        InventoryItem item = new InventoryItem();
        item.setName("Delete Test Item");
        item.setUnitPrice(new BigDecimal("75.00"));
        item.setActive(true);
        item = inventoryItemRepository.save(item);
        Long itemId = item.getInventoryItemId();

        inventoryItemRepository.deleteById(itemId);

        Optional<InventoryItem> deleted = inventoryItemRepository.findById(itemId);
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("TC-II6: Inventory item has valid price")
    void testValidPrice() {
        List<InventoryItem> items = inventoryItemRepository.findAll();

        for (InventoryItem item : items) {
            assertThat(item.getUnitPrice()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
            assertThat(item.getName()).isNotBlank();
        }
    }
}
