package com.babeh.designpattern.decorator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DecoratorApp.class)
class DecoratorTest {
    @Autowired KatalogService katalog;
    @Autowired ProdukServiceDb db;

    @Test
    void katalogMendapatBeanTerbungkusCacheLogDb() {
        var luar = katalog.produk();
        assertThat(luar).isInstanceOf(ProdukServiceCache.class);
        var tengah = ((ProdukServiceCache) luar).asli();
        assertThat(tengah).isInstanceOf(ProdukServiceLog.class);
        assertThat(((ProdukServiceLog) tengah).asli).isSameAs(db);
    }

    @Test
    void cacheHitTidakMenyentuhLogDanDb() {
        var log = (ProdukServiceLog) ((ProdukServiceCache) katalog.produk()).asli();
        int dbAwal = db.hit.get(), logAwal = log.log.size();

        var a = katalog.detail(42L);
        var b = katalog.detail(42L);   // cache hit

        assertThat(b).isSameAs(a);
        assertThat(db.hit.get() - dbAwal).isEqualTo(1);
        assertThat(log.log.size() - logAwal).isEqualTo(1);
        katalog.detail(43L);
        assertThat(db.hit.get() - dbAwal).isEqualTo(2);
    }
}
