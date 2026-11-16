package com.babeh.designpattern.strategy;

class Pelanggan {
    private final boolean member, mahasiswa, harbolnas;
    Pelanggan(boolean member, boolean mahasiswa, boolean harbolnas) {
        this.member = member; this.mahasiswa = mahasiswa; this.harbolnas = harbolnas;
    }
    boolean isMember() { return member; }
    boolean isMahasiswa() { return mahasiswa; }
    boolean isHarbolnas() { return harbolnas; }
}
