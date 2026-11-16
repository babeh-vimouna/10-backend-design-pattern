package com.babeh.designpattern.adapter;

import com.babeh.designpattern.common.HasilBayar;
import com.babeh.designpattern.common.PembayaranGateway;
import com.babeh.designpattern.common.Tagihan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "pembayaran.provider", havingValue = "xendit")
class XenditAdapter implements PembayaranGateway {
    /** simulasi respons client luar (format milik Xendit) */
    record Invoice(String invoiceId, String status) {}

    Invoice createInvoice(String externalId, long amount) {
        return new Invoice("xd-" + externalId, "PAID");
    }

    @Override
    public HasilBayar bayar(Tagihan t) {
        var inv = createInvoice(t.id(), t.jumlah());
        return new HasilBayar(inv.invoiceId(), "PAID".equals(inv.status()), "xendit");
    }
}
