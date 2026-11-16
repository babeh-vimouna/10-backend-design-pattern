package com.babeh.designpattern.strategy;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
class HitungHarga {
  private final List<StrategiDiskon> semua;
  HitungHarga(List<StrategiDiskon> semua) { this.semua = semua; }

  BigDecimal total(Pelanggan p, BigDecimal harga) {
    return semua.stream()
        .filter(s -> s.berlaku(p)).findFirst()
        .map(s -> s.hitung(harga)).orElse(harga);
  }

  List<StrategiDiskon> daftar() { return semua; }   // tambahan: untuk test
}
