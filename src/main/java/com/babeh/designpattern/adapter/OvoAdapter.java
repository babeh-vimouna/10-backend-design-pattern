package com.babeh.designpattern.adapter;

import com.babeh.designpattern.common.HasilBayar;
import com.babeh.designpattern.common.PembayaranGateway;
import com.babeh.designpattern.common.Tagihan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "pembayaran.provider", havingValue = "ovo")
class OvoAdapter implements PembayaranGateway {
    /** simulasi respons client luar (format milik OVO) */
    record Respon(String refNo, int code) {}

    Respon push(String billId, long amount) {
        return new Respon("ovo-" + billId, 0);
    }

    @Override
    public HasilBayar bayar(Tagihan t) {
        var r = push(t.id(), t.jumlah());
        return new HasilBayar(r.refNo(), r.code() == 0, "ovo");
    }
}
