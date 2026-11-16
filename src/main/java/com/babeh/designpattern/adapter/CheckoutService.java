package com.babeh.designpattern.adapter;

import com.babeh.designpattern.common.HasilBayar;
import com.babeh.designpattern.common.PembayaranGateway;
import com.babeh.designpattern.common.Tagihan;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
    private final PembayaranGateway gateway;  // interface, bukan Midtrans

    public CheckoutService(PembayaranGateway gateway) {
        this.gateway = gateway;
    }

    public HasilBayar bayar(Tagihan t) {
        return gateway.bayar(t);
    }
}
