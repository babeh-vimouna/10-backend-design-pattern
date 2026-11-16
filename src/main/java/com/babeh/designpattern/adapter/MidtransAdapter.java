package com.babeh.designpattern.adapter;

import com.babeh.designpattern.common.HasilBayar;
import com.babeh.designpattern.common.PembayaranGateway;
import com.babeh.designpattern.common.Tagihan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "pembayaran.provider", havingValue = "midtrans")
class MidtransAdapter implements PembayaranGateway {
    /** simulasi respons client luar (format milik Midtrans) */
    record Respon(String transactionId, String transactionStatus) {}

    Respon charge(String orderId, long grossAmount) {
        return new Respon("mt-" + orderId, "settlement");
    }

    @Override
    public HasilBayar bayar(Tagihan t) {
        var r = charge(t.id(), t.jumlah());
        return new HasilBayar(r.transactionId(), "settlement".equals(r.transactionStatus()), "midtrans");
    }
}
