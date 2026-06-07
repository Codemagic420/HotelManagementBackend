package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.BillItem;
import com.kea.hotel.hotelbackend.service.BillItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mysql/bill-items")
public class BillItemController {

    private final BillItemService service;

    public BillItemController(BillItemService service) {
        this.service = service;
    }

    @GetMapping
    public Page<BillItem> getAllBillItems(
            @RequestParam(required = false) String itemType,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(itemType, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillItem> getBillItemById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BillItem createBillItem(@RequestBody BillItem billItem) {
        return service.save(billItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillItem> updateBillItem(@PathVariable Long id, @RequestBody BillItem updated) {
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillItem(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
