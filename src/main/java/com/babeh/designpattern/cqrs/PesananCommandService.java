package com.babeh.designpattern.cqrs;

import org.springframework.stereotype.Service;

@Service
class PesananCommandService {          // tulis: aturan bisnis
  private final PesananWriteRepository repo;
  PesananCommandService(PesananWriteRepository repo) { this.repo = repo; }   // tambahan

  void batalkan(Long id) {
    var p = repo.findById(id).orElseThrow();   // tambahan: isi body
    p.status = Status.BATAL;
    repo.save(p);
  }
}
