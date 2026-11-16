package com.babeh.designpattern.decorator;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
class ProdukServiceDb implements ProdukService {
    final AtomicInteger hit = new AtomicInteger();   // tambahan: hitung akses "database"
    public Produk cari(Long id) {
        hit.incrementAndGet();
        return new Produk(id, "produk-" + id);
    }
}
