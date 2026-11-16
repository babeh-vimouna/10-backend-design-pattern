package com.babeh.designpattern.observer;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class KurangiStok {
  @EventListener
  void on(PesananDibuat e) { /* kurangi stok */ }
}
