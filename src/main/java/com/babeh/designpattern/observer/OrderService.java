package com.babeh.designpattern.observer;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

record PesananDibuat(Long pesananId) {}

@Service
class OrderService {
  private final ApplicationEventPublisher events;
  OrderService(ApplicationEventPublisher events) { this.events = events; }

  void checkout(Pesanan p) {
    // ...simpan pesanan...
    events.publishEvent(new PesananDibuat(p.getId()));
  }
}
