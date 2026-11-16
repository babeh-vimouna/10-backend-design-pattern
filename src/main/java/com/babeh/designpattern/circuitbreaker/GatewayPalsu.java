package com.babeh.designpattern.circuitbreaker;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/** tambahan: gateway simulasi yang bisa dibuat gagal/pulih dari test */
@Component
class GatewayPalsu implements PembayaranGateway {
    volatile boolean gagal = false;
    volatile long lambatMs = 0;
    final AtomicInteger panggilan = new AtomicInteger();

    public HasilBayar bayar(Tagihan t) {
        panggilan.incrementAndGet();
        if (lambatMs > 0) {
            try { Thread.sleep(lambatMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        if (gagal) throw new IllegalStateException("gateway down");
        return new HasilBayar(t.id(), true, "LUNAS");
    }
}
