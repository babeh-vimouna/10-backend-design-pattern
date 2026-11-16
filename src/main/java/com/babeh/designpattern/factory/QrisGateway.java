package com.babeh.designpattern.factory;

import com.babeh.designpattern.common.HasilBayar;
import com.babeh.designpattern.common.PembayaranGateway;
import com.babeh.designpattern.common.Tagihan;
import org.springframework.stereotype.Component;

@Component("qris")
class QrisGateway implements PembayaranGateway {
    @Override
    public HasilBayar bayar(Tagihan t) {
        return new HasilBayar("qris-" + t.id(), true, "qris");
    }
}
