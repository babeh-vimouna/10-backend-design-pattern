package com.babeh.designpattern.singleton;

import org.springframework.stereotype.Component;

@Component   // scope bawaan: singleton
class KonfigurasiToko {
  private final int batasDiskon = 30;
  int batasDiskon() { return batasDiskon; }
}
