package com.babeh.designpattern.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootTest(classes = StrategiApp.class)
class StrategiTest {
    static final BigDecimal SERATUS_RIBU = new BigDecimal("100000");

    @Autowired HitungHarga hitung;
    @Autowired DiskonMember member;
    @Autowired DiskonMahasiswa mahasiswa;

    static BigDecimal rp(BigDecimal b) { return b.setScale(0); }

    @Test
    void hargaPerStrategi() {
        assertThat(rp(hitung.total(new Pelanggan(true, false, false), SERATUS_RIBU))).isEqualTo(new BigDecimal("90000"));
        assertThat(rp(hitung.total(new Pelanggan(false, true, false), SERATUS_RIBU))).isEqualTo(new BigDecimal("85000"));
        assertThat(rp(hitung.total(new Pelanggan(false, false, true), SERATUS_RIBU))).isEqualTo(new BigDecimal("80000"));
        assertThat(hitung.total(new Pelanggan(false, false, false), SERATUS_RIBU)).isEqualTo(SERATUS_RIBU);
    }

    @Test
    void urutanListTanpaOrderStabilAntarContext() {
        var urutan = hitung.daftar().stream().map(s -> s.getClass().getSimpleName()).toList();
        System.out.println(">>> URUTAN TANPA @Order: " + urutan);
        for (int i = 0; i < 5; i++) {
            try (var ctx = new SpringApplicationBuilder(StrategiApp.class).web(org.springframework.boot.WebApplicationType.NONE).run()) {
                var lagi = ctx.getBean(HitungHarga.class).daftar().stream().map(s -> s.getClass().getSimpleName()).toList();
                assertThat(lagi).isEqualTo(urutan);
            }
        }
    }

    @Test
    void pelangganMemenuhiBanyakSyaratYangMenangStrategiPertamaDiList() {
        var semua = new Pelanggan(true, true, true);
        var hasilAsli = rp(hitung.total(semua, SERATUS_RIBU));
        System.out.println(">>> member+mahasiswa+harbolnas, urutan bawaan Spring -> " + hasilAsli);

        // urutan dibalik -> hasil beda: urutan list menentukan hasil
        var terbalik = new HitungHarga(List.of(
            hitung.daftar().stream().filter(s -> s.getClass().getSimpleName().equals("DiskonHarbolnas")).findFirst().get(),
            mahasiswa, member));
        assertThat(rp(terbalik.total(semua, SERATUS_RIBU))).isEqualTo(new BigDecimal("80000"));
        assertThat(hasilAsli).isIn(new BigDecimal("90000"), new BigDecimal("85000"), new BigDecimal("80000"));
    }
}
