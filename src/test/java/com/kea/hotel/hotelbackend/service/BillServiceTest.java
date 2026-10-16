package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.model.*;
import com.kea.hotel.hotelbackend.repository.BillRepository;
import com.kea.hotel.hotelbackend.repository.BillItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// ========== TEST PLAN: which bill scenarios are covered ==========
// Factors: bill state (open / closed) x number of items x operation.
//
// | Scenario                          | Test method                          |
// |-----------------------------------|--------------------------------------|
// | Open bill, read all / read by ID  | testFindAll, testFindById            |
// | Open bill, create / update        | testSave, testUpdateBill             |
// | Open bill, add ONE item           | testAddItemToBillSavesItemForBill    |
// | Open bill, sum of 3 items         | testCalculateMultipleItemsTotal      |
// | Open -> closed (payment)          | testCloseBill                        |
// | Delete bill                       | testDeleteBill                       |
// | NOT covered yet                   | discounts, VIP guests, bulk items    |

/**
 * Unit tests of BillService.
 * TYPE: pure UNIT test. No Spring, no DB: the repository is a Mockito @Mock, the service is @InjectMocks.
 * PATTERN: Arrange (when(...).thenReturn) -> Act (call service) -> Assert (AssertJ) + verify() calls on the mock.
 * MockitoExtension is strict: a stub that is never used fails the test (UnnecessaryStubbingException).
 * BREAK IT: change a stubbed return value or expected value, or make the service skip the repository call (verify fails).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BillService Unit Tests")
class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private BillItemRepository billItemRepository;

    @InjectMocks
    private BillService billService;

    private Bill testBill;
    private Reservation testReservation;
    private BillItem testBillItem;

    // Arrange: open bill #1 (total 0, not closed) for reservation RES001 and one ROOM item (2 x 150 = 300).
    @BeforeEach
    void setUp() {
        testReservation = new Reservation();
        testReservation.setReservationId(1L);
        testReservation.setReferenceNo("RES001");

        testBill = new Bill();
        testBill.setBillId(1L);
        testBill.setReservation(testReservation);
        testBill.setOpenedAt(LocalDateTime.now());
        testBill.setClosedAt(null);
        testBill.setTotalAmount(new BigDecimal("0.00"));

        testBillItem = new BillItem();
        testBillItem.setBillItemId(1L);
        testBillItem.setBill(testBill);
        testBillItem.setItemType("ROOM");
        testBillItem.setDescription("2 nights at $150/night");
        testBillItem.setQuantity(2);
        testBillItem.setUnitPrice(new BigDecimal("150.00"));
        testBillItem.setLineTotal(new BigDecimal("300.00"));
    }

    // Checks: returns both bills and calls findAll() once.
    // Fails if: service doesn't use the repository.
    @Test
    @DisplayName("Should retrieve all bills")
    void testFindAll() {
        Bill bill2 = new Bill();
        bill2.setBillId(2L);

        when(billRepository.findAll()).thenReturn(Arrays.asList(testBill, bill2));

        List<Bill> result = billService.findAll();

        assertThat(result).hasSize(2);
        verify(billRepository, times(1)).findAll();
    }

    // Checks: getBillById(1) returns the stubbed bill.
    // Fails if: the service asks for another ID.
    @Test
    @DisplayName("Should retrieve bill by ID")
    void testFindById() {
        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));

        Optional<Bill> result = billService.getBillById(1L);

        assertThat(result).isPresent().contains(testBill);
    }

    // Checks: save returns the bill with ID 1.
    // Fails if: service doesn't call repository.save().
    @Test
    @DisplayName("Should save bill successfully")
    void testSave() {
        when(billRepository.save(testBill)).thenReturn(testBill);

        Bill result = billService.save(testBill);

        assertThat(result).isNotNull();
        assertThat(result.getBillId()).isEqualTo(1L);
    }

    // Checks: addItemToBill(1, item) looks up bill 1 and saves the item (both verified once).
    // Fails if: service skips the bill lookup or the item save.
    @Test
    @DisplayName("Should add bill item to bill")
    void testAddItemToBillSavesItemForBill() {
        // Arrange
        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));
        when(billItemRepository.save(any(BillItem.class))).thenReturn(testBillItem);

        // Act - Call service method to add item to bill
        billService.addItemToBill(1L, testBillItem);

        // Assert - Verify interactions occurred
        verify(billRepository, times(1)).findById(1L);
        verify(billItemRepository, times(1)).save(any(BillItem.class));
    }

    // Checks: 300 + 50 + 25 = 375 using BigDecimal.
    // WEAK: sum is done in the test, not by the service.
    @Test
    @DisplayName("Should calculate multiple bill items total correctly")
    void testCalculateMultipleItemsTotal() {
        BillItem item1 = new BillItem();
        item1.setLineTotal(new BigDecimal("300.00")); //room

        BillItem item2 = new BillItem();
        item2.setItemType("EXTRA_SERVICE");
        item2.setDescription("Room service");
        item2.setLineTotal(new BigDecimal("50.00"));

        BillItem item3 = new BillItem();
        item3.setItemType("MISC");
        item3.setDescription("Late checkout fee");
        item3.setLineTotal(new BigDecimal("25.00"));

        BigDecimal total = item1.getLineTotal()
                .add(item2.getLineTotal())
                .add(item3.getLineTotal());

        assertThat(total).isEqualByComparingTo(new BigDecimal("375.00"));
        
    }

    // State change: closedAt goes from null to a timestamp (bill closed).
    // WEAK: sets the field itself; no service method is called.
    @Test
    @DisplayName("Should mark bill as closed when payment received")
    void testCloseBill() {
        assertThat(testBill.getClosedAt()).isNull();

        testBill.setClosedAt(LocalDateTime.now());

        assertThat(testBill.getClosedAt()).isNotNull();
    }

    // WEAK: loops over ROOM / EXTRA_SERVICE / MISC and checks the setter stores them.
    // Does not check that invalid types are rejected.
    @Test
    @DisplayName("Should validate bill item types")
    void testBillItemTypeValidation() {
        String[] validTypes = {"ROOM", "EXTRA_SERVICE", "MISC"};

        for (String type : validTypes) {
            testBillItem.setItemType(type);
            assertThat(testBillItem.getItemType()).isEqualTo(type);
        }
    }

    // Checks: BigDecimal precision, 33.33 x 3 = 99.99 (no floating-point error).
    // Fails if: expected total is changed to 100.00.
    @Test
    @DisplayName("Should handle price calculations with precision")
    void testPriceCalculationPrecision() {
        BillItem item = new BillItem();
        item.setQuantity(3);
        item.setUnitPrice(new BigDecimal("33.33"));

        BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        assertThat(lineTotal).isEqualByComparingTo(new BigDecimal("99.99"));
    }

    // Checks: saving a bill with total 500.00 returns that total.
    // Note: mocked save() returns the same object, so this mostly checks the stub.
    @Test
    @DisplayName("Should update bill successfully")
    void testUpdateBill() {
        Bill updatedBill = new Bill();
        updatedBill.setBillId(1L);
        updatedBill.setTotalAmount(new BigDecimal("500.00"));

        when(billRepository.save(any(Bill.class))).thenReturn(updatedBill);

        Bill result = billService.save(updatedBill);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("totalAmount", new BigDecimal("500.00"));
    }

    // Checks: deleteBill(1) calls repository.deleteById(1) once.
    // Fails if: the service doesn't delete or uses another ID.
    @Test
    @DisplayName("Should delete bill successfully")
    void testDeleteBill() {
        doNothing().when(billRepository).deleteById(1L);

        billService.deleteBill(1L);

        verify(billRepository, times(1)).deleteById(1L);
    }

    // WEAK: calls the MOCK repository directly (not billService), so no service code is tested.
    // To fix: call a service method such as findByReservationId and assert on that.
    @Test
    @DisplayName("Should retrieve bills by reservation")
    void testGetBillByReservationId() {
        when(billRepository.findById(any())).thenReturn(Optional.of(testBill));

        Optional<Bill> result = billRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getReservation().getReservationId()).isEqualTo(1L);
    }

    // Edge case: quantity -2 x price 100 = -200 (no validation blocks it).
    // Documents current behaviour; a real rule would reject negative quantity.
    @Test
    @DisplayName("Edge: negative quantity produces negative line total")
    void negativeQuantityLineTotal_isNegative() {
        BillItem item = new BillItem();
        item.setQuantity(-2);
        item.setUnitPrice(new BigDecimal("100"));
        BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        assertThat(lineTotal).isEqualByComparingTo(new BigDecimal("-200"));
    }

    // Edge case: price 0 x quantity 3 = 0 (free item).
    // Fails if: expected value is changed to non-zero.
    @Test
    @DisplayName("Edge: zero unit price yields zero line total")
    void zeroPriceLineTotal_isZero() {
        BillItem item = new BillItem();
        item.setQuantity(3);
        item.setUnitPrice(BigDecimal.ZERO);
        BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        assertThat(lineTotal).isEqualByComparingTo(BigDecimal.ZERO);
    }

    // Edge case: a Bill may exist with reservation = null.
    // Model test only; documents that the field is nullable.
    @Test
    @DisplayName("Edge: bill can have null reservation")
    void bill_canHaveNullReservation_reference() {
        Bill bill = new Bill();
        bill.setReservation(null);
        assertThat(bill.getReservation()).isNull();
    }
}
