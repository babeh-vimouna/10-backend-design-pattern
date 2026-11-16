package com.babeh.designpattern.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RepositoryApp.class)
class RepositoryTest {
    @Autowired PesananRepository repo;
    @Autowired PesananService service;

    @Test
    void derivedQueryFindByPelangganIdAndStatus() {
        repo.save(new Pesanan(7L, Status.AKTIF, BigDecimal.TEN));
        repo.save(new Pesanan(7L, Status.BATAL, BigDecimal.ONE));
        repo.save(new Pesanan(8L, Status.AKTIF, BigDecimal.ONE));

        var hasil = service.pesananAktif(7L);

        assertThat(hasil).hasSize(1);
        assertThat(hasil.get(0).status).isEqualTo(Status.AKTIF);
        assertThat(hasil.get(0).pelangganId).isEqualTo(7L);
    }
}
