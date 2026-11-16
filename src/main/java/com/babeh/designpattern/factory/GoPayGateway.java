package com.babeh.designpattern.factory;

import com.babeh.designpattern.common.HasilBayar;
import com.babeh.designpattern.common.PembayaranGateway;
import com.babeh.designpattern.common.Tagihan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component("gopay")
@ConditionalOnProperty("pembayaran.gopay.aktif")
class GoPayGateway implements PembayaranGateway {
    @Override
    public HasilBayar bayar(Tagihan t) {
        return new HasilBayar("gopay-" + t.id(), true, "gopay");
    }
}
