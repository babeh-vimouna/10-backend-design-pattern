package com.babeh.designpattern.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.babeh.designpattern.common.Tagihan;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

class FactoryConfigTest {
    static final Tagihan T = new Tagihan("INV-1", 50_000);

    @Nested
    @SpringBootTest(classes = FactoryApp.class, properties = "pembayaran.gopay.aktif=true")
    class GoPayAktif {
        @Autowired CheckoutService service;

        @Test void gopayJalan() {
            assertThat(service.bayar(MetodeBayar.GOPAY, T).via()).isEqualTo("gopay");
            assertThat(service.bayar(MetodeBayar.QRIS, T).via()).isEqualTo("qris");
        }
    }

    @Nested
    @SpringBootTest(classes = FactoryApp.class)
    class GoPayNonaktif {
        @Autowired CheckoutService service;

        @Test void gopayDitolak() {
            assertThatThrownBy(() -> service.bayar(MetodeBayar.GOPAY, T))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tidak aktif: GOPAY");
            assertThat(service.bayar(MetodeBayar.QRIS, T).sukses()).isTrue();
        }

        @Test void ovoTidakAdaBean() {
            assertThatThrownBy(() -> service.bayar(MetodeBayar.OVO, T))
                .hasMessage("tidak aktif: OVO");
        }
    }
}
