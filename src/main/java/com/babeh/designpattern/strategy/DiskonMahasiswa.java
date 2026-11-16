package com.babeh.designpattern.strategy;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
class DiskonMahasiswa implements StrategiDiskon {
  public boolean berlaku(Pelanggan p) { return p.isMahasiswa(); }

  public BigDecimal hitung(BigDecimal harga) {
    return harga.multiply(new BigDecimal("0.85"));
  }
}
