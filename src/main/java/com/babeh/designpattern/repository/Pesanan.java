package com.babeh.designpattern.repository;

import jakarta.persistence.*;
import java.math.BigDecimal;

enum Status { AKTIF, BATAL }

@Entity
class Pesanan {
    @Id @GeneratedValue Long id;
    Long pelangganId;
    @Enumerated(EnumType.STRING) Status status;
    BigDecimal total;

    protected Pesanan() {}
    Pesanan(Long pelangganId, Status status, BigDecimal total) {
        this.pelangganId = pelangganId; this.status = status; this.total = total;
    }
    Long getId() { return id; }
}
