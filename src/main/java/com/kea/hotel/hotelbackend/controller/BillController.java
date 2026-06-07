package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.Bill;
import com.kea.hotel.hotelbackend.model.BillItem;
import com.kea.hotel.hotelbackend.service.BillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mysql/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public Page<Bill> getAllBills(@PageableDefault(size = 20) Pageable pageable) {
        return billService.getAllBills(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        return billService.getBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reservation/{reservationId}")
    public List<Bill> getBillByReservationId(@PathVariable Long reservationId) {
        return billService.getBillByReservationId(reservationId);
    }

    @PostMapping
    public Bill createBill(@RequestBody Bill bill) {
        return billService.createBill(bill);
    }

    @PostMapping("/{billId}/items")
    public ResponseEntity<Void> addItemToBill(@PathVariable Long billId, @RequestBody BillItem item) {
        billService.addItemToBill(billId, item);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
