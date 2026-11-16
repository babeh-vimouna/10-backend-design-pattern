package com.babeh.designpattern.strategy;

import java.math.BigDecimal;

interface StrategiDiskon {
  boolean berlaku(Pelanggan p);
  BigDecimal hitung(BigDecimal harga);
}
