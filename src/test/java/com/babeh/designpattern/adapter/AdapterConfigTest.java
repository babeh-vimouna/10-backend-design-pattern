package com.babeh.designpattern.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.babeh.designpattern.common.Tagihan;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class AdapterConfigTest {
    static final Tagihan T = new Tagihan("INV-1", 50_000);

    @Nested
    @SpringBootTest(classes = AdapterApp.class, properties = "pembayaran.provider=midtrans")
    class Midtrans {
        @Autowired CheckoutService service;

        @Test void pakaiMidtrans() {
            var h = service.bayar(T);
            assertThat(h.via()).isEqualTo("midtrans");
            assertThat(h.idTransaksi()).isEqualTo("mt-INV-1");
            assertThat(h.sukses()).isTrue();
        }
    }

    @Nested
    @SpringBootTest(classes = AdapterApp.class, properties = "pembayaran.provider=xendit")
    class Xendit {
        @Autowired CheckoutService service;

        @Test void pakaiXendit() {
            var h = service.bayar(T);
            assertThat(h.via()).isEqualTo("xendit");
            assertThat(h.idTransaksi()).isEqualTo("xd-INV-1");
        }
    }

    @Nested
    @SpringBootTest(classes = AdapterApp.class, properties = "pembayaran.provider=ovo")
    class Ovo {
        @Autowired CheckoutService service;

        @Test void pakaiOvo() {
            var h = service.bayar(T);
            assertThat(h.via()).isEqualTo("ovo");
            assertThat(h.idTransaksi()).isEqualTo("ovo-INV-1");
        }
    }

    @Test
    void propertiKosongGagalStartup() {
        new ApplicationContextRunner().withUserConfiguration(AdapterApp.class).run(ctx -> {
            assertThat(ctx).hasFailed();
            System.out.println(">>> KOSONG: " + root(ctx.getStartupFailure()));
        });
    }

    @Test
    void propertiSalahGagalStartup() {
        new ApplicationContextRunner().withUserConfiguration(AdapterApp.class)
            .withPropertyValues("pembayaran.provider=stripe").run(ctx -> {
                assertThat(ctx).hasFailed();
                System.out.println(">>> SALAH: " + root(ctx.getStartupFailure()));
            });
    }

    static String root(Throwable t) {
        return t.getMessage();
    }
}
