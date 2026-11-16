package com.babeh.designpattern.singleton;

import org.springframework.stereotype.Service;

@Service
class DiskonService {
  private final KonfigurasiToko config;  // instance yang sama di semua service

  DiskonService(KonfigurasiToko config) {
    this.config = config;
  }

  boolean wajar(int persen) {
    return persen <= config.batasDiskon();
  }

  KonfigurasiToko config() { return config; }   // tambahan: untuk test
}
