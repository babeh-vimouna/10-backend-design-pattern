package com.babeh.designpattern.circuitbreaker;

record HasilBayar(String idTransaksi, boolean sukses, String status) {
    static HasilBayar tertunda(String id) { return new HasilBayar(id, false, "TERTUNDA"); }   // tambahan
}
