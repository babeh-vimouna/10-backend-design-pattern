package com.babeh.designpattern.decorator;

import org.springframework.stereotype.Service;

@Service
class KatalogService {
  private final ProdukService produk;   // tidak tahu ada decorator

  KatalogService(ProdukService produk) { this.produk = produk; }   // tambahan: constructor

  Produk detail(Long id) { return produk.cari(id); }

  ProdukService produk() { return produk; }   // tambahan: untuk test
}
