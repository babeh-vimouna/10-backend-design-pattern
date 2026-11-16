package com.babeh.designpattern.decorator;

import java.util.ArrayList;
import java.util.List;

class ProdukServiceLog implements ProdukService {
    final ProdukService asli;
    final List<String> log = new ArrayList<>();
    ProdukServiceLog(ProdukService asli) { this.asli = asli; }
    public Produk cari(Long id) {
        log.add("cari " + id);
        return asli.cari(id);
    }
}
