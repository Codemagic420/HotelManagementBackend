package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.dto.BillCreateUpdateDTO;
import com.kea.hotel.hotelbackend.dto.BillResponseDTO;
import com.kea.hotel.hotelbackend.model.Bill;
import com.kea.hotel.hotelbackend.model.BillItem;
import com.kea.hotel.hotelbackend.service.BillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public Page<BillResponseDTO> getAllBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort) {
        Sort sortOrder = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] parts = sort.split(",");
            String field = parts[0];
            Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
            sortOrder = Sort.by(direction, field);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return billService.getAllBills(pageable).map(this::mapToResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Long id) {
        return billService.getBillById(id)
                .map(this::mapToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reservation/{reservationId}")
    public List<BillResponseDTO> getBillByReservationId(@PathVariable Long reservationId) {
        return billService.getBillByReservationId(reservationId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BillResponseDTO createBill(@RequestBody BillCreateUpdateDTO dto) {
        Bill bill = mapToEntity(dto);
        return mapToResponseDTO(billService.createBill(bill));
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

    private BillResponseDTO mapToResponseDTO(Bill bill) {
        return new BillResponseDTO(
                bill.getBillId(),
                bill.getTotalAmount(),
                bill.getOpenedAt(),
                bill.getClosedAt(),
                bill.getReservation() != null ? bill.getReservation().getReservationId() : null
        );
    }

    private Bill mapToEntity(BillCreateUpdateDTO dto) {
        Bill bill = new Bill();
        bill.setTotalAmount(dto.getTotalAmount());
        return bill;
    }
}
