package com.babeh.designpattern.factory;

import com.babeh.designpattern.common.PembayaranGateway;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PembayaranFactory {
    private final Map<String, PembayaranGateway> gateways;  // key = nama bean

    PembayaranFactory(Map<String, PembayaranGateway> g) {
        gateways = g;
    }

    public PembayaranGateway ambil(MetodeBayar metode) {
        var g = gateways.get(metode.name().toLowerCase());
        if (g == null) throw new IllegalArgumentException("tidak aktif: " + metode);
        return g;
    }
}
