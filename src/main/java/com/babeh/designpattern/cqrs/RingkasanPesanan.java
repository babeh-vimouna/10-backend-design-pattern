package com.babeh.designpattern.cqrs;

import java.math.BigDecimal;

interface RingkasanPesanan {   // tambahan: slide tidak mendefinisikan; projection interface
    Long getId();
    Status getStatus();
    BigDecimal getTotal();
}
