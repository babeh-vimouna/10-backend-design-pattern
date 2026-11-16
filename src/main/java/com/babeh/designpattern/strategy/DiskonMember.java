package com.babeh.designpattern.strategy;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
class DiskonMember implements StrategiDiskon {
  public boolean berlaku(Pelanggan p) { return p.isMember(); }

  public BigDecimal hitung(BigDecimal harga) {
    return harga.multiply(new BigDecimal("0.90"));
  }
}
