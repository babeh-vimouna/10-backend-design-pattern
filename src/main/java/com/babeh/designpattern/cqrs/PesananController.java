package com.babeh.designpattern.cqrs;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
class PesananController {
  private final PesananCommandService command;
  private final PesananQueryService query;

  PesananController(PesananCommandService command, PesananQueryService query) {   // tambahan
    this.command = command; this.query = query;
  }

  @DeleteMapping("/pesanan/{id}")
  void batal(@PathVariable Long id) { command.batalkan(id); }

  @GetMapping("/pelanggan/{id}/pesanan")
  List<RingkasanPesanan> daftar(@PathVariable Long id) {
    return query.daftar(id);
  }
}
