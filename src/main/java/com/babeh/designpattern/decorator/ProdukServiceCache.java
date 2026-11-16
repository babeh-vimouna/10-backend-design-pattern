package com.babeh.designpattern.decorator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ProdukServiceCache implements ProdukService {
  private final ProdukService asli;   // yang ke database
  private final Map<Long, Produk> cache =
      new ConcurrentHashMap<>();

  ProdukServiceCache(ProdukService asli) { this.asli = asli; }   // tambahan: constructor

  public Produk cari(Long id) {
    return cache.computeIfAbsent(id, asli::cari);
  }

  ProdukService asli() { return asli; }   // tambahan: untuk test
}
