package com.babeh.designpattern.decorator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class ProdukConfig {
  @Bean @Primary
  ProdukService produkService(ProdukServiceDb db) {
    return new ProdukServiceCache(      // decorator luar
        new ProdukServiceLog(db));      // wrap yang asli
  }
}
