package com.kea.hotel.hotelbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill_item")
@Data
@NoArgsConstructor
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billItemId;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(nullable = false)
    private String itemType;

    @Column(nullable = false)
    private String description;

    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private LocalDateTime postedAt;
    private BigDecimal amount;
}
