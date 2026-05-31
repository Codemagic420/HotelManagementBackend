package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.Bill;
import com.kea.hotel.hotelbackend.model.BillItem;
import com.kea.hotel.hotelbackend.service.BillService;
import com.kea.hotel.hotelbackend.repository.ReservationRepository;
import com.kea.hotel.hotelbackend.model.Bill;
import com.kea.hotel.hotelbackend.model.Reservation;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;
    private final ReservationRepository reservationRepository;

    public BillController(BillService billService, ReservationRepository reservationRepository) {
        this.billService = billService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<Bill> getAllBills() {
        return billService.getAllBills();
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
    public ResponseEntity<?> createBill(@RequestBody Map<String, Object> payload) {
        Object resIdObj = payload.get("reservationId");
        if (resIdObj == null) return ResponseEntity.badRequest().body("missing reservationId");

        Long resId = ((Number) resIdObj).longValue();
        java.util.Optional<Reservation> resOpt = reservationRepository.findById(resId);
        if (resOpt.isEmpty()) return ResponseEntity.badRequest().body("reservation not found");

        BigDecimal roomCharge = new BigDecimal(payload.getOrDefault("roomCharge", "0").toString());
        BigDecimal taxAmount = new BigDecimal(payload.getOrDefault("taxAmount", "0").toString());
        BigDecimal discount = new BigDecimal(payload.getOrDefault("discountAmount", "0").toString());

        if (roomCharge.compareTo(BigDecimal.ZERO) < 0 || taxAmount.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("negative amounts");
        }
        BigDecimal total = roomCharge.add(taxAmount).subtract(discount);
        if (discount.compareTo(roomCharge.add(taxAmount)) > 0) {
            return ResponseEntity.badRequest().body("discount exceeds amount");
        }

        Bill bill = new Bill();
        bill.setReservation(resOpt.get());
        bill.setRoomCharge(roomCharge);
        bill.setTaxAmount(taxAmount);
        bill.setDiscountAmount(discount);
        bill.setTotalAmount(total);
        Object status = payload.get("billStatus");
        if (status != null) bill.setBillStatus(status.toString());

        return ResponseEntity.ok(billService.createBill(bill));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBill(@PathVariable Long id, @RequestBody Map<String,Object> payload) {
        return billService.getBillById(id).map(b -> {
            if (payload.containsKey("roomCharge")) b.setRoomCharge(new BigDecimal(payload.get("roomCharge").toString()));
            if (payload.containsKey("taxAmount")) b.setTaxAmount(new BigDecimal(payload.get("taxAmount").toString()));
            if (payload.containsKey("discountAmount")) b.setDiscountAmount(new BigDecimal(payload.get("discountAmount").toString()));
            if (payload.containsKey("billStatus")) b.setBillStatus(payload.get("billStatus").toString());
            // recompute total
            BigDecimal total = b.getRoomCharge().add(b.getTaxAmount()).subtract(b.getDiscountAmount());
            b.setTotalAmount(total);
            billService.save(b);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payBill(@PathVariable Long id, @RequestBody Map<String,Object> payload) {
        return billService.getBillById(id).map(b -> {
            b.setBillStatus("PAID");
            b.setClosedAt(java.time.LocalDateTime.now());
            billService.save(b);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
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
