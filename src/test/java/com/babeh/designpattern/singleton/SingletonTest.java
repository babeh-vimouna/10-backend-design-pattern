package com.babeh.designpattern.singleton;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {SingletonApp.class, SingletonTest.DuaService.class})
class SingletonTest {

    @Configuration
    static class DuaService {
        @Bean DiskonService diskonService2(KonfigurasiToko c) { return new DiskonService(c); }
    }

    @Autowired KonfigurasiToko toko;
    @Autowired DiskonService diskonService;
    @Autowired DiskonService diskonService2;

    @Test
    void duaBeanDiskonServiceMemegangInstanceYangSama() {
        assertThat(diskonService).isNotSameAs(diskonService2);
        assertThat(diskonService.config()).isSameAs(toko);
        assertThat(diskonService2.config()).isSameAs(toko);
        assertThat(diskonService.wajar(30)).isTrue();
        assertThat(diskonService.wajar(31)).isFalse();
    }

    @Test
    void scopePrototypeMemberiInstanceBeda() {
        try (var ctx = new AnnotationConfigApplicationContext()) {
            ctx.registerBean(KonfigurasiToko.class, bd -> bd.setScope("prototype"));
            ctx.registerBean("a", DiskonService.class);
            ctx.registerBean("b", DiskonService.class);
            ctx.refresh();
            var a = ctx.getBean("a", DiskonService.class);
            var b = ctx.getBean("b", DiskonService.class);
            assertThat(a.config()).isNotSameAs(b.config());
        }
    }

    @Test
    void singletonHanyaSatuPerContainerBukanPerJvm() {
        try (var c1 = new AnnotationConfigApplicationContext(KonfigurasiToko.class);
             var c2 = new AnnotationConfigApplicationContext(KonfigurasiToko.class)) {
            assertThat(c1.getBean(KonfigurasiToko.class)).isNotSameAs(c2.getBean(KonfigurasiToko.class));
        }
    }
}
