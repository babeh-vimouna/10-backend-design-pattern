package com.babeh.designpattern.circuitbreaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
class PembayaranService {
  private final PembayaranGateway gateway;   // tambahan: field + constructor
  PembayaranService(PembayaranGateway gateway) { this.gateway = gateway; }

  @CircuitBreaker(name = "gateway",
                  fallbackMethod = "bayarNanti")
  HasilBayar bayar(Tagihan t) {
    return gateway.bayar(t);   // bisa gagal/timeout
  }

  HasilBayar bayarNanti(Tagihan t, Throwable e) {
    return HasilBayar.tertunda(t.id());
  }
}
