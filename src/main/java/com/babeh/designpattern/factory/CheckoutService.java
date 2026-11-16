package com.babeh.designpattern.factory;

import com.babeh.designpattern.common.HasilBayar;
import com.babeh.designpattern.common.Tagihan;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
    private final PembayaranFactory factory;

    CheckoutService(PembayaranFactory factory) {
        this.factory = factory;
    }

    public HasilBayar bayar(MetodeBayar metode, Tagihan t) {  // pilihan pelanggan
        return factory.ambil(metode).bayar(t);
    }
}
