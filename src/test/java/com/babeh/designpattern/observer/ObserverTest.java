package com.babeh.designpattern.observer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest(classes = {ObserverApp.class, ObserverTest.Tambahan.class})
class ObserverTest {

    /** listener baru, ditambahkan tanpa menyentuh OrderService */
    @Component
    static class Rekam {
        final List<String> thread = new ArrayList<>();
        final List<Boolean> adaTx = new ArrayList<>();
        volatile boolean lambat, error;

        @EventListener
        void on(PesananDibuat e) throws Exception {
            thread.add(Thread.currentThread().getName());
            adaTx.add(TransactionSynchronizationManager.isActualTransactionActive());
            if (lambat) Thread.sleep(400);
            if (error) throw new IllegalStateException("listener rusak");
        }
    }

    @Configuration
    static class Tambahan {
        @org.springframework.context.annotation.Bean Rekam rekam() { return new Rekam(); }
    }

    @Autowired OrderService order;
    @Autowired Rekam rekam;
    @Autowired TransactionTemplate tx;

    @Test
    void listenerBerjalanDiThreadDanTransaksiPublisher() {
        rekam.thread.clear(); rekam.adaTx.clear();
        var threadPublisher = Thread.currentThread().getName();

        tx.executeWithoutResult(s -> order.checkout(new Pesanan(1L)));

        assertThat(rekam.thread).containsExactly(threadPublisher);
        assertThat(rekam.adaTx).containsExactly(true);   // di dalam transaksi checkout
    }

    @Test
    void listenerLambatMemblokirCheckout() {
        rekam.lambat = true;
        try {
            long t0 = System.nanoTime();
            order.checkout(new Pesanan(2L));
            long ms = (System.nanoTime() - t0) / 1_000_000;
            System.out.println(">>> checkout dengan listener 400 ms memakan " + ms + " ms");
            assertThat(ms).isGreaterThanOrEqualTo(400);
        } finally { rekam.lambat = false; }
    }

    @Test
    void errorDiListenerMerambatKeCheckout() {
        rekam.error = true;
        try {
            assertThatThrownBy(() -> order.checkout(new Pesanan(3L)))
                .isInstanceOf(IllegalStateException.class).hasMessage("listener rusak");
        } finally { rekam.error = false; }
    }

    @Test
    void listenerBaruTerpanggilTanpaMengubahOrderService() {
        rekam.thread.clear();
        order.checkout(new Pesanan(4L));
        assertThat(rekam.thread).hasSize(1);   // Rekam = listener tambahan; OrderService tidak diubah
    }
}
