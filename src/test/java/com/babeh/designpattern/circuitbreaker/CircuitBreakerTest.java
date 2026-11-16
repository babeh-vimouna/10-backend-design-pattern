package com.babeh.designpattern.circuitbreaker;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CircuitBreakerApp.class)
@ActiveProfiles("cb")   // application-cb.yml = konfigurasi slide, persis
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CircuitBreakerTest {
    static final Tagihan T = new Tagihan("INV-1", 1000);

    @Autowired PembayaranService service;
    @Autowired GatewayPalsu gateway;
    @Autowired CircuitBreakerRegistry registry;

    CircuitBreaker cb;

    @BeforeEach void siap() { cb = registry.circuitBreaker("gateway"); }

    @Test
    void konfigurasiTerbacaPersisDariYml() {
        var c = cb.getCircuitBreakerConfig();
        assertThat(c.getSlidingWindowSize()).isEqualTo(10);
        assertThat(c.getMinimumNumberOfCalls()).isEqualTo(5);
        assertThat(c.getFailureRateThreshold()).isEqualTo(50f);
        assertThat(c.getPermittedNumberOfCallsInHalfOpenState()).isEqualTo(3);
        assertThat(c.getWaitIntervalFunctionInOpenState().apply(1)).isEqualTo(10_000L);
        assertThat(AopUtils.isAopProxy(service)).as("service diproxy annotation").isTrue();
    }

    @Test
    void openSetelahLimaGagalDanFallbackDipakai() {
        gateway.gagal = true;
        for (int i = 1; i <= 4; i++) {
            assertThat(service.bayar(T).status()).isEqualTo("TERTUNDA");   // fallback juga untuk exception biasa
            assertThat(cb.getState()).as("setelah %d gagal", i).isEqualTo(State.CLOSED);
        }
        service.bayar(T);
        assertThat(cb.getState()).isEqualTo(State.OPEN);

        int sebelum = gateway.panggilan.get();
        var hasil = service.bayar(T);   // saat OPEN: ditolak, gateway tidak disentuh
        assertThat(hasil).isEqualTo(HasilBayar.tertunda("INV-1"));
        assertThat(gateway.panggilan.get()).isEqualTo(sebelum);
    }

    private void bukaBreaker() {
        gateway.gagal = true;
        for (int i = 0; i < 5; i++) service.bayar(T);
        assertThat(cb.getState()).isEqualTo(State.OPEN);
    }

    @Test
    void halfOpenSetelah10DetikLaluPulihJadiClosed() throws Exception {
        bukaBreaker();
        Thread.sleep(9_000);
        service.bayar(T);
        assertThat(cb.getState()).as("9 detik: masih OPEN").isEqualTo(State.OPEN);
        Thread.sleep(1_200);
        assertThat(cb.getState()).as("tanpa panggilan, state belum berpindah").isEqualTo(State.OPEN);

        gateway.gagal = false;
        int sebelum = gateway.panggilan.get();
        assertThat(service.bayar(T).status()).isEqualTo("LUNAS");   // panggilan pertama memicu HALF_OPEN
        assertThat(cb.getState()).isEqualTo(State.HALF_OPEN);
        service.bayar(T);
        assertThat(cb.getState()).isEqualTo(State.HALF_OPEN);
        service.bayar(T);   // panggilan uji ke-3 -> evaluasi
        assertThat(gateway.panggilan.get() - sebelum).isEqualTo(3);
        assertThat(cb.getState()).isEqualTo(State.CLOSED);
    }

    @Test
    void halfOpenGagalKembaliOpen() throws Exception {
        bukaBreaker();
        Thread.sleep(10_200);
        int sebelum = gateway.panggilan.get();
        for (int i = 0; i < 3; i++) service.bayar(T);   // gateway masih gagal
        assertThat(gateway.panggilan.get() - sebelum).isEqualTo(3);
        assertThat(cb.getState()).isEqualTo(State.OPEN);
    }

    @Test
    void halfOpenHanyaMengizinkanTigaPanggilanUjiBersamaan() throws Exception {
        bukaBreaker();
        Thread.sleep(10_200);
        gateway.gagal = false;
        gateway.lambatMs = 500;
        int sebelum = gateway.panggilan.get();

        var pool = Executors.newFixedThreadPool(6);
        List<Future<HasilBayar>> f = new ArrayList<>();
        for (int i = 0; i < 6; i++) f.add(pool.submit(() -> service.bayar(T)));
        int lunas = 0, tertunda = 0;
        for (var x : f) { if (x.get().status().equals("LUNAS")) lunas++; else tertunda++; }
        pool.shutdown();

        System.out.println(">>> HALF_OPEN, 6 panggilan bersamaan: ke gateway=" + (gateway.panggilan.get() - sebelum)
            + " lunas=" + lunas + " fallback=" + tertunda);
        assertThat(gateway.panggilan.get() - sebelum).isEqualTo(3);
        assertThat(tertunda).isEqualTo(3);
    }

    // ---- pembanding: tanpa minimumNumberOfCalls
    @Test
    void tanpaMinimumNumberOfCallsTidakTripPada9Gagal() {
        var reg = CircuitBreakerRegistry.of(CircuitBreakerConfig.custom()
            .slidingWindowSize(10).failureRateThreshold(50).build());   // minimumNumberOfCalls bawaan
        var b = reg.circuitBreaker("tanpa-min");
        System.out.println(">>> minimumNumberOfCalls efektif (window 10, tidak diset) = "
            + b.getCircuitBreakerConfig().getMinimumNumberOfCalls());
        for (int i = 1; i <= 9; i++) {
            b.onError(0, java.util.concurrent.TimeUnit.MILLISECONDS, new RuntimeException("x"));
            assertThat(b.getState()).as("setelah %d gagal", i).isEqualTo(State.CLOSED);
        }
        b.onError(0, java.util.concurrent.TimeUnit.MILLISECONDS, new RuntimeException("x"));
        System.out.println(">>> setelah gagal ke-10: " + b.getState());
    }

    @Test
    void tanpaWindowDanMinimumBawaanButuh100() {
        var reg = CircuitBreakerRegistry.of(CircuitBreakerConfig.custom().failureRateThreshold(50).build());
        var b = reg.circuitBreaker("default");
        System.out.println(">>> default: window=" + b.getCircuitBreakerConfig().getSlidingWindowSize()
            + " min=" + b.getCircuitBreakerConfig().getMinimumNumberOfCalls());
        for (int i = 0; i < 99; i++) b.onError(0, java.util.concurrent.TimeUnit.MILLISECONDS, new RuntimeException("x"));
        assertThat(b.getState()).isEqualTo(State.CLOSED);
        b.onError(0, java.util.concurrent.TimeUnit.MILLISECONDS, new RuntimeException("x"));
        assertThat(b.getState()).isEqualTo(State.OPEN);
    }
}
