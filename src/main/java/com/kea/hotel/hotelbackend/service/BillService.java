package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.Bill;
import com.kea.hotel.hotelbackend.model.BillItem;
import com.kea.hotel.hotelbackend.repository.BillRepository;
import com.kea.hotel.hotelbackend.repository.BillItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;

    public BillService(BillRepository billRepository, BillItemRepository billItemRepository) {
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
    }

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    public List<Bill> getBillByReservationId(Long reservationId) {
        return billRepository.findAll().stream()
                .filter(bill -> bill.getReservation() != null && bill.getReservation().getReservationId().equals(reservationId))
                .toList();
    }

    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    public void addItemToBill(Long billId, BillItem item) {
        billRepository.findById(billId).ifPresent(bill -> {
            item.setBill(bill);
            billItemRepository.save(item);
        });
    }

    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }
}
